package com.example.storey.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storey.R
import com.example.storey.data.remote.response.ListStoryItem
import com.example.storey.databinding.ItemRowMainBinding
import com.example.storey.helper.AllStoriesDiffCallback

class AllStoriesAdapter(private val onItemClickCallback: OnItemClickCallback) :
    PagingDataAdapter<ListStoryItem, AllStoriesAdapter.AllStoriesViewHolder>(AllStoriesDiffCallback) {
    inner class AllStoriesViewHolder(var binding: ItemRowMainBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllStoriesViewHolder {
        val binding = ItemRowMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllStoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllStoriesViewHolder, position: Int) {
        val context = holder.binding.root.context

        getItem(position)?.let { story ->
            holder.binding.apply {
                tvName.text = story.name
                Glide.with(context)
                    .load(story.photoUrl)
                    .placeholder(R.drawable.ic_baseline_person_pin_24)
                    .into(ivPhoto)

                root.setOnClickListener {
                    val optionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            Pair(ivPhoto, context.resources.getString(R.string.photo)),
                            Pair(tvName, context.resources.getString(R.string.name)),
                        )
                    onItemClickCallback.onItemClicked(story, optionsCompat)
                }
            }
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(story: ListStoryItem, optionsCompat: ActivityOptionsCompat)
    }
}