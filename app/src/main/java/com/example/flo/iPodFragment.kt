package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentIpodBinding

class iPodFragment: Fragment() {

    lateinit var binding:FragmentIpodBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentIpodBinding.inflate(inflater,container,false)
        return binding.root
    }
}