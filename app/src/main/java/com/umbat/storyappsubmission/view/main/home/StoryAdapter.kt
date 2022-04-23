package com.umbat.storyappsubmission.view.main.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.umbat.storyappsubmission.R
import com.umbat.storyappsubmission.databinding.ItemListStoryBinding
import com.umbat.storyappsubmission.model.StoryModel
import com.umbat.storyappsubmission.view.main.detail.DetailActivity
import com.umbat.storyappsubmission.view.main.detail.DetailActivity.Companion.EXTRA_DATA

class StoryAdapter(private val listStory: ArrayList<StoryModel>) :
    RecyclerView.Adapter<StoryAdapter.HomeViewHolder>() {

//    private var onItemClickCallback: OnItemClickCallback? = null

    inner class HomeViewHolder(private val binding: ItemListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: StoryModel){
            binding.apply {
                tvUsername.text = user.name
                Glide.with(itemView.context)
                    .load(user. photo)
//                    .transition(DrawableTransitionOptions.withCrossFade())
                    .fitCenter()
                    .apply(
                        RequestOptions
                            .placeholderOf(R.drawable.ic_baseline_refresh_24)
                            .error(R.drawable.ic_baseline_broken_image_24))
                    .into(ivStory)
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(EXTRA_DATA, user)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemListStoryBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: HomeViewHolder, position: Int) {
        viewHolder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size

//    interface OnItemClickCallback {
//        fun onItemClicked(data: StoryModel)
//    }
}