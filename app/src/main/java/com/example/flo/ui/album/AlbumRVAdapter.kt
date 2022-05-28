package com.example.flo.ui.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.Album
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumlist: ArrayList<Album>): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>(){

    interface  MyItemClickListener{
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)
    }

    private lateinit var myItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        myItemClickListener=itemClickListener
    }

    fun addItem(album: Album){
        albumlist.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position:Int){
        albumlist.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder { //뷰 홀더 생성할 때
         val binding: ItemAlbumBinding= ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { //뷰 홀더에 데이터를 바인딩 할 때 불러줌, recyclerview에서 position은 인덱스 id
        holder.bind(albumlist[position])
       holder.itemView.setOnClickListener { myItemClickListener.onItemClick(albumlist[position]) }
//        holder.binding.itemAlbumTitleTv.setOnClickListener{myItemClickListener.onRemoveAlbum(position)}
    }

    override fun getItemCount(): Int =albumlist.size

    inner class ViewHolder(val binding: ItemAlbumBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(album: Album){
            binding.itemAlbumTitleTv.text=album.title
            binding.itemAlbumSingerTv.text=album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)

        }
    }

}