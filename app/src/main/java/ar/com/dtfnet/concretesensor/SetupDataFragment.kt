package ar.com.dtfnet.concretesensor


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ar.com.dtfnet.concretesensor.databinding.FragmentSetupDataBinding
import ar.com.dtfnet.concretesensor.setup.Setup
import ar.com.dtfnet.concretesensor.setup.SetupViewModel

/**
 * Fragmento donde se inicializan los datos de vinculo entre celular y sensor
  */
class SetupDataFragment : Fragment() {
    private var _binding: FragmentSetupDataBinding? = null
    private val binding get() = _binding!!

    private lateinit var setupViewModel: SetupViewModel

    private lateinit var sensor: TextView
    private lateinit var mixer: TextView
    private lateinit var user: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSetupDataBinding.inflate(inflater, container, false)
        val view = binding.root

        sensor = binding.sensorId
        mixer = binding.mixerId
        user = binding.userId

        setupViewModel = ViewModelProvider(this).get(SetupViewModel::class.java)

        var datos: LiveData<List<Setup>>
        datos = setupViewModel.allSetups

        var data: List<Setup>
        datos.observe(viewLifecycleOwner, Observer { data ->
            data?.let { updataSetupUI(data) }
        })

        binding.setupUpdateButton.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_setupDataFragment_to_measuredDataFragment)
        }

        return view
    }

    private fun updataSetupUI(setups: List<Setup>) {
        Log.i("*************Hara algo ? ", "No lo se")

        if (!setups.isEmpty()) {

            Log.i("**********Setup observado: ", setups.last().sensor_id.toString())
            Log.i("**********Mixer observado: ", setups.last().mixer_id.toString())
            Log.i("**********User observado: ", setups.last().user_id.toString())

            sensor.text = setups.last().sensor_id.toString()
            mixer.text = setups.last().mixer_id.toString()
            user.text = setups.last().user_id.toString()

        } else {
            Log.i("*************List: ", "is empty")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



