package com.example.flo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")  //테이블 만들기
data class Song(
    var title:String="",
    var singer:String="",
    var second:Int=0,
    var playTime:Int=0,
    var isPlaying:Boolean=false,
    var music:String="",
    var coverImg: Int?= null,
    var isLike: Boolean= false
){
    @PrimaryKey(autoGenerate = true) var id:Int=0
}



//제목, 가수, 사진, 재생시간, 현재 재생시간, 재생되고 있는지(isPlaying),어떤 음악이 재생되는지