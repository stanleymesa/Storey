package com.example.storey.ui.activity

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.storey.R
import com.example.storey.data.local.statics.StaticData
import com.example.storey.databinding.ActivityLoginBinding
import com.example.storey.ui.adapter.LoginPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setAnimation()
        setTabWithViewPager()
    }

    private fun setAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogo, View.ROTATION, 360f).apply {
            duration = 2000L
            repeatCount = ObjectAnimator.INFINITE
            start()
        }
    }

    private fun setTabWithViewPager() {
        val loginPagerAdapter = LoginPagerAdapter(this)
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        viewPager.adapter = loginPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                StaticData.TAB_LOGIN_INDEX -> tab.text = resources.getString(R.string.login)
                StaticData.TAB_REGISTER_INDEX -> tab.text = resources.getString(R.string.register)
            }
        }.attach()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}