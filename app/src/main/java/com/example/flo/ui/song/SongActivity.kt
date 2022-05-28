package com.example.flo.ui.song

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.R
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    //AppCompatActivity: 안드로이드에서 Activity의 기능들을 사용하도록 만든 클래스

    lateinit var binding: ActivitySongBinding
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos =0

    override fun onCreate(savedInstanceState: Bundle?) {  //onCreate: 액티비티가 생성될 때 처음으로 실행되는 함수
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater) //inflate: xml에 표기된 레이아웃을 메모리에 객체화
        setContentView(binding.root)  //xml에 있는 것들을 쓸 수 있게 함

        initPlayList()
        initSong()
        initClickListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() //미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer=null //미디어 플레이어 해제
    }

    private fun initPlayList(){
        songDB= SongDatabase.getInstance(this)!!
        songs.addAll(songDB.SongDao().getSongs())
    }


    private fun initClickListener(){
        binding.songDownIb.setOnClickListener {
            finish()  //Activity를 꺼줌
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songShuffleIv.setOnClickListener {
            setShuffleStatus(false)
        }
        binding.songShuffleIv2.setOnClickListener {
            setShuffleStatus(true)
        }

        binding.songRepeatIv.setOnClickListener {
            setRepeatStatus(false)
        }
        binding.songRepeatIv2.setOnClickListener {
            setRepeat1Status(false)
        }

        binding.songRepeatIv3.setOnClickListener {
            setRepeat1Status(true)
        }
        binding.songLikeIv.setOnClickListener {
            setLikeStatus(false)
        }
        binding.songLikeIv2.setOnClickListener {
            setLikeStatus(true)
        }

        binding.songUnlikeIv.setOnClickListener {
            setUnlikeStatus(false)
        }
        binding.songUnlikeIv2.setOnClickListener {
            setUnlikeStatus(true)
        }

        binding.songNextIv.setOnClickListener{
            moveSong(+1)
        }

        binding.songPreviousIv.setOnClickListener{
            moveSong(-1)
        }

        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }

    private fun initSong(){
       val spf=getSharedPreferences("song", MODE_PRIVATE)
        val songId=spf.getInt("songId",0)

        nowPos=getPlayingSongPosition(songId)

        Log.d("now Song Id",songs[nowPos].id.toString())
        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike=!isLike
        songDB.SongDao().updateIsLikeById(!isLike,songs[nowPos].id)

        if(!isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }


    private fun moveSong(direct: Int){
        if(nowPos + direct<0){
            Toast.makeText(this,"first song",Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos + direct>= songs.size){
            Toast.makeText(this,"last song",Toast.LENGTH_SHORT).show()
            return
            }

        nowPos+=direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer=null

        setPlayer(songs[nowPos])
    }


    private fun getPlayingSongPosition(songId: Int):Int{
        for(i in 0 until songs.size){
            if (songs[i].id==songId){
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song: Song){
        binding.songTitleTv.text = song.title
        binding.singerNameTv.text = song.singer
        binding.songStartTv.text=String.format("%02d:%02d",song.second/60,song.second%60)
        binding.songEndTv.text=String.format("%02d:%02d",song.playTime/60,song.playTime%60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songProgressSb.progress=(song.second*1000/song.playTime)

        val music=resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer=MediaPlayer.create(this,music)


        if(song.isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }


    fun setPlayerStatus(isPlaying: Boolean) {
        songs[nowPos].isPlaying=isPlaying
        timer.isPlaying=isPlaying


        if (isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        }

        else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying==true){
                mediaPlayer?.pause()
            }
        }

    }

    private fun startTimer(){
        timer=Timer(songs[nowPos].playTime,songs[nowPos].isPlaying)
        timer.start()
    }


    //타이머 클래스를 만들고 스레드 상속받음
    inner class Timer(private val playTime: Int,var isPlaying: Boolean=true):Thread(){

        private var second:Int=0
        private var mills: Float=0f

        override fun run(){
            super.run()
            try{
                while(true){
                    if(second>=playTime){
                        break
                    }
                    if(isPlaying){
                        sleep(50)
                        mills+=50

                        runOnUiThread{
                            binding.songProgressSb.progress=((mills/playTime)*100).toInt()
                        }
                        if(mills % 1000 ==0f){
                            runOnUiThread {
                                binding.songStartTv.text=String.format("%02d:%02d",second/60,second%60)
                            }
                            second++
                        }

                    }
                }

            }catch (e:InterruptedException){
                Log.d("Song","스레드가 죽었습니다.${e.message}")
            }

        }
    }

    override fun onPause(){  //사용자가 초점을 잃었을 때 음악 정지
        super.onPause()
        setPlayerStatus(false)
        songs[nowPos].second=((binding.songProgressSb.progress*songs[nowPos].playTime)/100)/1000 //음악이 몇 초까지 재생됐는지
        val sharedPreferences=getSharedPreferences("song", MODE_PRIVATE) //내부 저장소에 데이터 저장, 간단한 값 저장
        val editor=sharedPreferences.edit() //에디터

        editor.putInt("songId",songs[nowPos].id)
        editor.apply()  //실제 저장공간에 저장됨

    }


    fun setShuffleStatus(isPlaying: Boolean) {
        if (isPlaying) {
            binding.songShuffleIv.visibility = View.VISIBLE
            binding.songShuffleIv2.visibility = View.GONE
        } else {
            binding.songShuffleIv.visibility = View.GONE
            binding.songShuffleIv2.visibility = View.VISIBLE
        }
    }

    fun setRepeatStatus(isPlaying: Boolean) {
        if (!isPlaying) {
            binding.songRepeatIv2.visibility = View.VISIBLE
            binding.songRepeatIv.visibility = View.GONE
            binding.songRepeatIv3.visibility = View.GONE
        }
    }

    fun setRepeat1Status(isPlaying: Boolean) {
        if (isPlaying) {
            binding.songRepeatIv.visibility = View.VISIBLE
            binding.songRepeatIv2.visibility = View.GONE
            binding.songRepeatIv3.visibility = View.GONE
        }
        else{
            binding.songRepeatIv3.visibility = View.VISIBLE
            binding.songRepeatIv2.visibility = View.GONE
            binding.songRepeatIv.visibility = View.GONE
        }
    }



    fun setLikeStatus(isPlaying: Boolean) {
        if (isPlaying) {
            binding.songLikeIv.visibility = View.VISIBLE
            binding.songLikeIv2.visibility = View.GONE
        } else {
            binding.songLikeIv.visibility = View.GONE
            binding.songLikeIv2.visibility = View.VISIBLE
        }
    }
        fun setUnlikeStatus(isPlaying: Boolean) {
            if (isPlaying) {
                binding.songUnlikeIv.visibility = View.VISIBLE
                binding.songUnlikeIv2.visibility = View.GONE
            } else {
                binding.songUnlikeIv.visibility = View.GONE
                binding.songUnlikeIv2.visibility = View.VISIBLE
            }
        }}
