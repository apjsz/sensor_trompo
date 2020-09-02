package ar.com.dtfnet.concretesensor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.com.dtfnet.concretesensor.databinding.FragmentInitDataBinding


/**
 * Fragmento inicial. Muestra la pantalla de inicio con información del sensor, camión y
 * conductor vinculados. También muestra, una vez cargado, el destino y datos sobre el producto.
 */


class InitDataFragment : Fragment() {
    private var _binding: FragmentInitDataBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInitDataBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

