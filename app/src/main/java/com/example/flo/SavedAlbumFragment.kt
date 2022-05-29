package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentLockerSavedalbumBinding


class SavedAlbumFragment : Fragment() {
    lateinit var binding: FragmentLockerSavedalbumBinding
    lateinit var albumDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedalbumBinding.inflate(inflater, container, false)

        albumDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.savedalbumTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val albumRVAdapter = AlbumLockerRVAdapter()
        binding.savedalbumTodayMusicAlbumRv.adapter = albumRVAdapter
        //리스너 객체 생성 및 전달

        albumRVAdapter.setMyItemClickListener(object : AlbumLockerRVAdapter.MyItemClickListener{
            override fun onRemoveSong(albumId: Int) {
                albumDB.albumDao().disLikedAlbum(getJwt(), albumId)
            }
        })



        albumRVAdapter.addAlbums(albumDB.albumDao().getLikedAlbum(getJwt()) as ArrayList)
    }

    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth" , AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }
}