package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentLookBinding

class LookFragment : Fragment(), LookView {
    private lateinit var binding: FragmentLookBinding
    private lateinit var floChartAdapter: SongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getSongs()
    }

    private fun initRecyclerView(result: FloChartResult) {
        floChartAdapter = SongRVAdapter(requireContext(), result)

        binding.lookFloChartRv.adapter = floChartAdapter
    }

    private fun getSongs() {
        val songService = SongService()
        songService.setLookView(this)

        songService.getSongs()

    }
    override fun onGetSongLoading() {

    }

    override fun onGetSongSuccess(code: Int, result: FloChartResult) {

        initRecyclerView(result)
    }

    override fun onGetSongFailure(code: Int, message: String) {

    }
}