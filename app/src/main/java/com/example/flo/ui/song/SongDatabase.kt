package com.example.flo.ui.song

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flo.data.entities.UserDao
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Like
import com.example.flo.data.entities.Song
import com.example.flo.data.entities.User
import com.example.flo.ui.album.AlbumDao

@Database(entities = [Song::class, User::class, Like::class, Album::class],version=1)  //어떤 테이블이 데이터에 들어가 있는지
abstract class SongDatabase : RoomDatabase(){
    abstract fun SongDao(): SongDao
    abstract fun userDao(): UserDao
    abstract fun albumDao(): AlbumDao

    companion object{   //singleton 패턴, 내부 인스턴스는 객체를 여러개 만들어도 한번만 생성되게끔
        private var instance: SongDatabase? =null

        @Synchronized
        fun getInstance(context: Context): SongDatabase?{
            if(instance ==null){
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "user-databases"  //다른 데이터베이스랑 이름 겹치면 꼬임
                    ).allowMainThreadQueries().build()  //메인스레드에 해당하는 작업을 넘김
                }
            }
            return instance
        }
    }
}