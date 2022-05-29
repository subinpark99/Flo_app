package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSavedBinding

class SavedFragment: Fragment() {

    lateinit var binding: FragmentSavedBinding
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSavedBinding.inflate(inflater, container, false)

        songDB= SongDatabase.getInstance(requireContext())!!


            return binding.root
       
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){

        binding.savedTodayMusicAlbumRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val songRVAdapter= SavedRVAdapter()

        songRVAdapter.setMyItemClickListener(object : SavedRVAdapter.MyItemClickListener {
            override fun onRemoveSong(songId: Int) {
                songDB.SongDao().updateIsLikeById(false,songId)
            }

        })
        binding.savedTodayMusicAlbumRv.adapter=songRVAdapter

        songRVAdapter.addSongs(songDB.SongDao().getLikedSongs(true)as ArrayList<Song>)
    }
}