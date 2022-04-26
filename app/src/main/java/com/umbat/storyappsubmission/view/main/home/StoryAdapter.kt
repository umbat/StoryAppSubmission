package com.umbat.storyappsubmission.view.main.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.umbat.storyappsubmission.R
import com.umbat.storyappsubmission.databinding.ItemListStoryBinding
import com.umbat.storyappsubmission.model.StoryModel
import com.umbat.storyappsubmission.view.main.detail.DetailActivity
import com.umbat.storyappsubmission.view.main.detail.DetailActivity.Companion.EXTRA_DATA

class StoryAdapter(private val listStory: List<StoryModel>) :
    RecyclerView.Adapter<StoryAdapter.ListStoryViewHolder>() {

    inner class ListStoryViewHolder(private val binding: ItemListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryModel){
            binding.apply {
                tvUsername.text = story.name
                tvDesc.text = story.description
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .fitCenter()
                    .apply(
                        RequestOptions
                            .placeholderOf(R.drawable.ic_baseline_refresh_24)
                            .error(R.drawable.ic_baseline_broken_image_24)
                    )
                    .into(ivStory)
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(EXTRA_DATA, story)
                itemView.context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListStoryViewHolder {
        val binding = ItemListStoryBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ListStoryViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ListStoryViewHolder, position: Int) {
        viewHolder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size
}