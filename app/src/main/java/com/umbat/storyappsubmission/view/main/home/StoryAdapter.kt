package com.umbat.storyappsubmission.view.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.umbat.storyappsubmission.databinding.ItemListStoryBinding
import com.umbat.storyappsubmission.model.StoryModel

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.HomeViewHolder>() {

    private val list = ArrayList<StoryModel>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback (onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setList(users: ArrayList<StoryModel>) {
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }

    inner class HomeViewHolder(val binding: ItemListStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        infix fun bind(user: StoryModel){
            binding.root.setOnClickListener{
                onItemClickCallback?.onItemClicked(user)
            }

            binding.apply {
                Glide.with(itemView.context)
                    .load(user. photo)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(ivStory)
                tvUsername.text = user.name
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HomeViewHolder {
        val view = ItemListStoryBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return HomeViewHolder((view))
    }

    override fun onBindViewHolder(viewHolder: HomeViewHolder, position: Int) {
        viewHolder bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickCallback {
        fun onItemClicked(data: StoryModel)
    }
}