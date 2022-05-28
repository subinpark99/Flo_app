package com.example.flo.ui.album

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.DetailFragment
import com.example.flo.ui.song.SongFragment
import com.example.flo.VideoFragment

class AlbumVPAdapter(fragment: Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int =3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> SongFragment()
            1-> DetailFragment()
            else-> VideoFragment()
        }
    }
}