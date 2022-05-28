package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.ui.home.HomeFragment
import com.example.flo.ui.locker.LockerFragment
import com.example.flo.ui.look.LookFragment
import com.example.flo.ui.song.SongActivity
import com.example.flo.ui.song.SongDatabase
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var song: Song = Song()   //초기화=> sharedpreference
    private var gson:Gson= Gson()
    private var mediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()
        inputDummyAlbums()


        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.mainPlayerCl.setOnClickListener{
            val editor=getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId",song.id)
            editor.apply()

           val intent=Intent(this, SongActivity::class.java)
            startActivity(intent)
        } //메인 액티비티에서 song 액티비티로 화면 전환


        initBottomNavigation()
        Log.d("MAIN/JWT_TO_SERVER",getJwt().toString())  //데이터가 잘 저장되었는지 확인
    }

    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun setMiniPlayer(song: Song){
       binding.titleTv.text=song.title
        binding.singerTv.text=song.singer
        binding.mainProgressSb.progress=(song.second*100000)/song.playTime

    }

    private fun getJwt(): String? {
        val spf = this.getSharedPreferences("auth" , AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt", "")
    }

    override fun onStart() {
        super.onStart()

        val spf=getSharedPreferences("song", MODE_PRIVATE)
        val songId= spf.getInt("songId",0)

        val songDB= SongDatabase.getInstance(this)!!

        song=if(songId==0){
            songDB.SongDao().getSong(1)
        }else{
            songDB.SongDao().getSong(songId)
        }

        Log.d("song ID",song.id.toString())
        setMiniPlayer(song)

    }



    private fun setPlayerStatus(isPlaying: Boolean) {
        song.isPlaying=isPlaying


        if (isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
        }

        else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
            if(mediaPlayer?.isPlaying==true){
                mediaPlayer?.pause()
            }
        }

    }


    private fun setPlayer(song: Song){
        val music=resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer=MediaPlayer.create(this,music)
        setPlayerStatus(song.isPlaying)

    }


    override fun onPause(){  //사용자가 초점을 잃었을 때 음악 정지
        super.onPause()
        setPlayerStatus(false)
        song.second=((binding.mainProgressSb.progress*song.playTime)/100)/1000 //음악이 몇 초까지 재생됐는지
        val sharedPreferences=getSharedPreferences("song", MODE_PRIVATE) //내부 저장소에 데이터 저장, 간단한 값 저장
        val editor=sharedPreferences.edit() //에디터
        val songJSon=gson.toJson(song)


        editor.putString("songData",songJSon)
        editor.apply()  //실제 저장공간에 저장됨

    }

    private fun inputDummySongs(){
        val songDB= SongDatabase.getInstance(this)!!
        val songs=songDB.SongDao().getSongs()

        if(songs.isNotEmpty()) return

        songDB.SongDao().insert(
            Song(
                "Feel My Rhythm",
                "Red Velvet",
                0,
                240,
                false,
                "music_feelmyrhythm",
                R.drawable.feeltherhythm,
                false,

            )
        )

        songDB.SongDao().insert(
            Song(
                "Lilac",
                "IU",
                0,
                240,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,

                )
        )

        songDB.SongDao().insert(
            Song(
                "Weekend",
                "Taeyeon",
                0,
                240,
                false,
                "music_weekend",
                R.drawable.img_album_exp6,
                false,

                )
        )

        songDB.SongDao().insert(
            Song(
                "Starlight",
                "TAEIL",
                0,
                240,
                false,
                "music_starlight",
                R.drawable.starlight,
                false,

                )
        )

        songDB.SongDao().insert(
            Song(
                "Buffering",
                "NCT DREAM",
                0,
                240,
                false,
                "music_buffering",
                R.drawable.buffering,
                false,

                )
        )

        val _songs=songDB.SongDao().getSongs()
        Log.d("DB data",_songs.toString())
    }
    private fun inputDummyAlbums(){
        val songDB= SongDatabase.getInstance(this)!!
        val albums=songDB.albumDao().getAlbums()
        Log.d("album data",albums.toString())
        if(albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(0,"Lilac","아이유", R.drawable.img_album_exp2)
        )

        songDB.albumDao().insert(
            Album(1,"Butter","방탄소년단", R.drawable.img_album_exp)
        )

        songDB.albumDao().insert(
            Album(2,"Next Level","에스파", R.drawable.img_album_exp3)
        )

        songDB.albumDao().insert(
            Album(3,"Boy In Luv","방탄소년단", R.drawable.img_album_exp4)
        )

        songDB.albumDao().insert(
            Album(4,"뿜뿜","모모랜드", R.drawable.img_album_exp5)
        )

        songDB.albumDao().insert(
            Album(5,"Weekend","태연", R.drawable.img_album_exp6)
        )

    }
}