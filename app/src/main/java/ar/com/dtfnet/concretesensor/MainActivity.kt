/*
Para evitar que el teléfono se desconecte del sensor hay que eliminarlo del listado del celular y
reconectarlo nuevamente.
Hay que agregar un indicador de teléfono conectado por bluetooth a sensor.
Modificarlo para que no apague la pantalla de forma automática.
 */

package ar.com.dtfnet.concretesensor

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ar.com.dtfnet.concretesensor.ble.BleDeviceAdapter
import ar.com.dtfnet.concretesensor.ble.BleDeviceData
import ar.com.dtfnet.concretesensor.ble.BluetoothLeService
import ar.com.dtfnet.concretesensor.ble.DeviceControlActivity
import ar.com.dtfnet.concretesensor.databinding.ActivityMainBinding
import ar.com.dtfnet.concretesensor.setup.Setup
import ar.com.dtfnet.concretesensor.setup.SetupRepository
import ar.com.dtfnet.concretesensor.setup.SetupViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), BleDeviceAdapter.OnItemClickListener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var view: View

    private lateinit var sensor: EditText
    private lateinit var mixer: EditText
    private lateinit var user: EditText

    private lateinit var repository: SetupRepository
    private lateinit var setupViewModel: SetupViewModel

    private var mHandler: Handler? = null
    private var mScanning: Boolean = false

    private var mBluetoothLeScanner: BluetoothLeScanner? = null
    private var mBluetoothManager: BluetoothManager? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null

    // Inicializo RecyclerView
    private val deviceList = ArrayList<BleDeviceData>()
    private val adapter = BleDeviceAdapter(deviceList, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        sensor = binding.sensorId
        mixer = binding.mixerId
        user = binding.userId

        setupViewModel = ViewModelProvider(this).get(SetupViewModel::class.java)

        var datos: LiveData<List<Setup>>
        datos = setupViewModel.allSetups

        var data: List<Setup>

        datos.observe(this, Observer { data ->
            data?.let { updataSetupUI(data) }
        })

        binding.setupButton.setOnClickListener {
            updateSetupValues()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        binding.progressBar.visibility = View.GONE

        mHandler = Handler()

        // Check the access to Location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_COARSE_LOCATION
                )
            }
        }

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show()
            finish()
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        mBluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = mBluetoothManager!!.adapter

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
    }

    // Permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_COARSE_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(ContentValues.TAG, "coarse location permission granted")
                } else {

                }
                return
            }
        }
    }

    // Return after importing the system Bluetooth page
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_ENABLE_BT ->{
                when (resultCode) {
                    AppCompatActivity.RESULT_OK ->{
                        mBluetoothLeScanner = mBluetoothManager!!.adapter.bluetoothLeScanner
                        scanLeDevice(true)
                    }
                    AppCompatActivity.RESULT_CANCELED ->{
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()

        // Initialize recyclerview adapter
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        binding.bleInitButton.onClick {
            initDeviceScan()
            binding.progressBar.visibility = View.VISIBLE
        }

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onPause() {
        super.onPause()
        if (mBluetoothAdapter!!.isEnabled) {
            scanLeDevice(false)
//            mLeDeviceListAdapter!!.clear()
        }
    }

    fun initDeviceScan() {
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }else{
            mBluetoothLeScanner = mBluetoothManager!!.adapter.bluetoothLeScanner
            scanLeDevice(true)
        }
    }

    private fun scanLeDevice(enable: Boolean) {
        when (enable) {
            true -> {
                mHandler!!.postDelayed({
                    mScanning = false
                    mBluetoothLeScanner!!.stopScan(mScanCallback)
                    binding.progressBar.visibility = View.GONE
                }, SCAN_PERIOD)
                mScanning = true
                mBluetoothLeScanner!!.startScan(mScanCallback)
            }
            false -> {
                mScanning = false
                mBluetoothLeScanner!!.stopScan(mScanCallback)
            }
        }
    }

    private val mScanCallback = object: ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            val deviceName = result?.device?.name
            val deviceAddress: String

            if (deviceName != null && deviceName.isNotEmpty()) {
                val idx: Int
                deviceAddress = result.device.address.toString()
                val item: BleDeviceData = BleDeviceData(deviceName, deviceAddress)
                idx = deviceList.indexOf(item)
                if (idx == -1 ) {
                    deviceList.add(item)
                }
                else {
                    deviceList.removeAt(idx)
                    deviceList.add(idx, item)
                }
                adapter.notifyDataSetChanged()

                // Automatic connection based on database sensor_id
                val sensorName = sensor.text.toString()
                if (deviceName.equals(sensorName)) {
                    Log.i("Sensor id ", sensorName)
                    startGatt(deviceName, deviceAddress)
                    mScanning = false
                }
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            Log.d(TAG, "onBatchScanResult:${results.toString()}")
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d(TAG, "onScanFailed:${errorCode}")
        }
    }

    override fun onItemClick(position: Int) {
        val ckickedItem: BleDeviceData = deviceList[position]
        val devName = deviceList[position].deviceName
        val devAddress = deviceList[position].deviceAddress.toString()

        Log.i(devName, devAddress)
        startGatt(devName, devAddress)
    }

    private fun startGatt(devName: String, devAddress: String) {
        val intent = Intent(this, DeviceControlActivity::class.java)
        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, devName)
        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, devAddress)
        if (mScanning) {
            mScanning = false
            mBluetoothLeScanner!!.stopScan(mScanCallback)
        }
        startActivity(intent)
    }

    private fun updateSetupValues() {
        var datos: Setup = Setup(1, sensor.text.toString(), mixer.text.toString(), user.text.toString())

        setupViewModel.delete()
        setupViewModel.insert(datos)

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun updataSetupUI(setups : List<Setup>) {

        if (!setups.isEmpty()) {

            sensor.text.clear()
            sensor.text.append(setups.last().sensor_id.toString())
            mixer.text.clear()
            mixer.text.append(setups.last().mixer_id.toString())
            user.text.clear()
            user.text.append(setups.last().user_id.toString())

        } else {

        }
    }

    companion object {
        private val TAG = BluetoothLeService::class.java.simpleName

        const val REQUEST_ENABLE_BT = 1 // BLE turn on postback ¿?
        const val PERMISSION_REQUEST_COARSE_LOCATION = 1

        // Stops scanning after 10 seconds.
        private val SCAN_PERIOD: Long = 10000
    }

}

