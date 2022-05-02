package com.example.storey.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.storey.R
import com.example.storey.data.local.statics.StaticData
import com.example.storey.data.remote.response.ListStoryItem
import com.example.storey.databinding.ActivityDetailBinding
import com.example.storey.helper.diffFromCurrentTime
import com.example.storey.ui.viewmodel.DetailViewModel
import java.util.*

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var storyItem: ListStoryItem
    private val detailViewModel by viewModels<DetailViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        getStoryItem()
        setToolbar()
        setDetail()
        binding.includeDetail.tvLocation.setOnClickListener(this)
    }

    private fun getStoryItem() {
        intent.getParcelableExtra<ListStoryItem>(StaticData.KEY_INTENT_DETAIL)?.let { storyItem ->
            this.storyItem = storyItem
        }
    }

    private fun setToolbar() {
        setSupportActionBar(binding.detailToolbar)
        supportActionBar?.title = storyItem.name
        binding.detailToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setDetail() {
        binding.includeDetail.apply {
            Glide.with(this@DetailActivity)
                .load(storyItem.photoUrl)
                .into(ivPhoto)

            tvLocation.isVisible = storyItem.lat != null && storyItem.lon != null

            storyItem.lat?.let { lat ->
                storyItem.lon?.let { lon ->

                    detailViewModel.getLocation(applicationContext, lat.toDouble(), lon.toDouble())
                    detailViewModel.location.observe(this@DetailActivity, { location ->

                        when (location) {
                            StaticData.LOCATION_ERROR -> tvLocation.isVisible = false
                            else -> tvLocation.text = location
                        }

                    })

                }
            }

            tvName.text = storyItem.name
            tvDesc.text = storyItem.description
            tvDate.text = storyItem.createdAt.diffFromCurrentTime(this@DetailActivity)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_location -> {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra(StaticData.FROM_ACTIVITY, StaticData.DETAIL_ACTIVITY)
                intent.putExtra(StaticData.KEY_INTENT_DETAIL, storyItem)
                startActivity(intent)
            }
        }
    }
}