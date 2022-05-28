package com.example.flo.ui.saved

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ItemSavedBinding

class SavedRVAdapter():
    RecyclerView.Adapter<SavedRVAdapter.ViewHolder>(){
    private val songs= ArrayList<Song>()

    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }
    private lateinit var mMyItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mMyItemClickListener=itemClickListener
    }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder { //뷰 홀더 생성할 때
            val binding: ItemSavedBinding = ItemSavedBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) { //뷰 홀더에 데이터를 바인딩 할 때 불러줌, recyclerview에서 position은 인덱스 id
            holder.bind(songs[position])
            holder.binding.savedListMoreIv.setOnClickListener {
                mMyItemClickListener.onRemoveSong((songs[position].id))
                removeSong(position)
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        fun addSongs(songs:ArrayList<Song>){
            this.songs.clear()
            this.songs.addAll(songs)
            notifyDataSetChanged()
        }

        @SuppressLint("NotifyDataSetChanged")
        private fun removeSong(position: Int){
            songs.removeAt(position)
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int =songs.size

        inner class ViewHolder(val binding:ItemSavedBinding): RecyclerView.ViewHolder(binding.root){

            fun bind(savedItem: Song){
                binding.savedListTitleTv.text=savedItem.title
                binding.savedListSingerTv.text=savedItem.singer
                binding.itemSavedAlbumCoverImgIv.setImageResource(savedItem.coverImg!!)

            }


        }


    }