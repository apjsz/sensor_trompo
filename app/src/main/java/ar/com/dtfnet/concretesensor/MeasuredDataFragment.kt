package ar.com.dtfnet.concretesensor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.com.dtfnet.concretesensor.databinding.FragmentMeasuredDataBinding


/**
 * A simple [Fragment] subclass.
 * Use the [MeasuredDataFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MeasuredDataFragment : Fragment() {
    private var _binding : FragmentMeasuredDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMeasuredDataBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}