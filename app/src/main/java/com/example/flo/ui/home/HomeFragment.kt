package com.example.flo.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.MainActivity
import com.example.flo.R
import com.example.flo.ui.song.SongDatabase
import com.example.flo.data.entities.Album
import com.example.flo.databinding.FragmentHomeBinding
import com.example.flo.ui.album.AlbumFragment
import com.example.flo.ui.album.AlbumRVAdapter
import com.example.flo.ui.banner.BannerFragment
import com.example.flo.ui.banner.BannerVPAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private  var albumDatas = ArrayList<Album>()
    private var currentPosition = 0
    private lateinit var songDB : SongDatabase

    val handler = Handler(Looper.getMainLooper()) {
        setPage()
        true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

//        binding.album1Iv.setOnClickListener {
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.main_frm, AlbumFragment()).commitAllowingStateLoss()
//        }

        songDB= SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(songDB.albumDao().getAlbums())

        val albumRVAdapter= AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter=albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }

        })

        val bannerVPAdapter = BannerVPAdapter(this)
        bannerVPAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerVPAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerVPAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val homePannelAdapter = HomeVPAdapter(this)
        binding.pannelVp.adapter = homePannelAdapter

        val thread = Thread(PagerRunnable())
        thread.start()

        TabLayoutMediator(binding.pannelTb, binding.pannelVp) { tab, position ->
        }.attach()


        return binding.root
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }


    fun setPage() {
        if (currentPosition == 3) currentPosition = 0
        binding.pannelVp.setCurrentItem(currentPosition, true)
        currentPosition += 1
    }


    inner class PagerRunnable : Runnable {
        override fun run() {
            while (true) {
                Thread.sleep(2500)
                handler.sendEmptyMessage(0)
            }
        }

    }
}

