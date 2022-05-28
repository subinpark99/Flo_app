package com.example.flo.ui.locker

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.ui.saved.SavedAlbumFragment
import com.example.flo.ui.saved.SavedFragment
import com.example.flo.iPodFragment

class LockerVPAdapter (fragment: Fragment): FragmentStateAdapter(fragment){
    override fun getItemCount(): Int =3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> SavedFragment()
            1-> iPodFragment()
            else-> SavedAlbumFragment()
        }
    }
}