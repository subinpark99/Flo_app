package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding

    private  val information= arrayListOf("저장한 곡", "iPod 음악","저장앨범")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter= LockerVPAdapter(this)
        binding.lockerVp.adapter=lockerAdapter

        TabLayoutMediator(binding.lockerTb,binding.lockerVp){
                tab,position->
            tab.text=information[position]
        }.attach()

        binding.lockerLogin.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        initViews()
    }

    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth" , AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }

    private fun initViews() {
        val jwt: Int = getJwt()

        if (jwt == 0){
            binding.lockerLogin.text = "로그인"

            binding.lockerLogin.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        else{
            binding.lockerLogin.text = "로그아웃"
            binding.lockerLogin.setOnClickListener {
                //로그아웃 진행
                logout()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    private fun logout() {  //뷰의 텍스트를 login으로 할 지 logout으로 할 지 결정해서 view를 초기화 시켜줌
        val spf = activity?.getSharedPreferences("auth" , AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()
        editor.remove("jwt")
        editor.apply()
    }
}