package com.example.storey.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.storey.data.local.statics.StaticData
import com.example.storey.ui.fragment.LoginFragment
import com.example.storey.ui.fragment.RegisterFragment

class LoginPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return StaticData.TAB_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            StaticData.TAB_LOGIN_INDEX -> LoginFragment()
            else -> RegisterFragment()
        }
    }

}