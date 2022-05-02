package com.example.storey.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.preferencesDataStore
import com.example.storey.R
import com.example.storey.data.local.datastore.UserPreferences
import com.example.storey.databinding.ActivitySplashBinding
import com.example.storey.ui.viewmodel.PrefViewModel
import com.example.storey.ui.viewmodel.PrefViewModelFactory

private val Context.dataStore by preferencesDataStore(name = "settings")

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(UserPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setAnimation()
        observeData()
        window.navigationBarColor = ContextCompat.getColor(this, R.color.dark_purple)
    }

    private fun observeData() {
        prefViewModel.getSession().observe(this, { userModel ->
            if (userModel.isLogin) {
                goToMain()
            } else {
                goToLogin()
            }
        })
    }

    @SuppressLint("Recycle")
    private fun setAnimation() {
        val logoFade = ObjectAnimator.ofFloat(binding.ivLogo, View.ALPHA, 1f).setDuration(1000L)
        val logoRotate =
            ObjectAnimator.ofFloat(binding.ivLogo, View.ROTATION, 360f).setDuration(2000L)
        val textFade = ObjectAnimator.ofFloat(binding.tvLogo, View.ALPHA, 1f).setDuration(1000L)

        AnimatorSet().apply {
            playTogether(logoFade, logoRotate, textFade)
            start()
        }
    }

    private fun goToLogin() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        handler.postDelayed(runnable, 3000)
    }

    private fun goToMain() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        handler.postDelayed(runnable, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}