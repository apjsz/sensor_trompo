package ar.com.dtfnet.concretesensor.ble

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.com.dtfnet.concretesensor.R
import kotlinx.android.synthetic.main.listitem_device.view.*

class BleDeviceAdapter (private val deviceData: List<BleDeviceData>,
                        private val listener: OnItemClickListener
) : RecyclerView.Adapter<BleDeviceAdapter.DeviceViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.listitem_device,
            parent, false)
        return DeviceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val current = deviceData[position]

        holder.deviceName.setText(current.deviceName)
        holder.deviceAddress.setText(current.deviceAddress)
    }

    override fun getItemCount(): Int {
        return deviceData.size
    }

    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
           View.OnClickListener
    {
        val deviceName: TextView = itemView.device_name
        val deviceAddress: TextView = itemView.device_address

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


}