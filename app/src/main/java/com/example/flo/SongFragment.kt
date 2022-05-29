package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentDetailBinding
import com.example.flo.databinding.FragmentSongBinding

class SongFragment:Fragment() {
    lateinit var binding: FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSongBinding.inflate(inflater,container,false)

        binding.toggleBtnIv.setOnClickListener {
            setToggleStatus(false)
        }
        binding.toggleBtnIv2.setOnClickListener {
            setToggleStatus(true)
        }


        binding.songBufferingLayout.setOnClickListener {
            Toast.makeText(activity, "BUFFERING", Toast.LENGTH_SHORT).show()
        }  //Toast 메시지를 짧게 띄움

        binding.songArcadeLayout.setOnClickListener {
            Toast.makeText(activity, "Arcade", Toast.LENGTH_SHORT).show()
        }
        binding.songDriveLayout.setOnClickListener {
            Toast.makeText(activity, "DRIVE", Toast.LENGTH_SHORT).show()
        }
        binding.songFireAlarmLayout.setOnClickListener {
            Toast.makeText(activity, "FIREALARM", Toast.LENGTH_SHORT).show()
        }
        binding.songGoodbyeLayout.setOnClickListener {
            Toast.makeText(activity, "GOODBYE", Toast.LENGTH_SHORT).show()
        }
        binding.songItsyoursLayout.setOnClickListener {
            Toast.makeText(activity, "IT'S,YOURS", Toast.LENGTH_SHORT).show()
        }
        binding.songTeddybearLayout.setOnClickListener {
            Toast.makeText(activity, "TEDDYBEAR", Toast.LENGTH_SHORT).show()
        }
        return binding.root

    }

    fun setToggleStatus(isPlaying: Boolean) {
        if (isPlaying) {
            binding.toggleBtnIv.visibility = View.VISIBLE
            binding.toggleBtnIv2.visibility = View.GONE
        } else {
            binding.toggleBtnIv.visibility = View.GONE
            binding.toggleBtnIv2.visibility = View.VISIBLE
        }
    }


}