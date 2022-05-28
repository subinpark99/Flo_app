package com.example.flo.ui.locker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.Album
import com.example.flo.databinding.ItemLockerAlbumBinding


class AlbumLockerRVAdapter() : RecyclerView.Adapter<AlbumLockerRVAdapter.ViewHolder>() {
    private val albums = ArrayList<Album>()

    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener : MyItemClickListener){
        mItemClickListener = itemClickListener
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemLockerAlbumBinding = ItemLockerAlbumBinding.inflate(LayoutInflater.from(viewGroup.context),
            viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albums[position])
        holder.binding.itemLockerMoreIv.setOnClickListener {
            mItemClickListener.onRemoveSong(albums[position].id)
            removeSong(position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAlbums(albums: ArrayList<Album>){
        this.albums.clear()
        this.albums.addAll(albums)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeSong(position: Int){
        albums.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = albums.size

    inner class ViewHolder(val binding : ItemLockerAlbumBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(album: Album){
            binding.itemLockerTitleTv.text = album.title
            binding.itemLockerSingerTv.text = album.singer
            binding.itemlockerCoverImgIv.setImageResource(album.coverImg!!)
        }

    }

}