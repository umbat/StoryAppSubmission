package com.umbat.storyappsubmission.ui.main.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.umbat.storyappsubmission.R
import com.umbat.storyappsubmission.databinding.ActivityDetailBinding
import com.umbat.storyappsubmission.model.StoryModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupData()
    }

    private fun setupData() {
        val detail = intent.getParcelableExtra<StoryModel>(EXTRA_DATA) as StoryModel
        binding.apply {
            tvUsername.text = detail.name
            tvDetailStory.text = detail.description
            Glide.with(this@DetailActivity)
                .load(detail.photoUrl)
                .fitCenter()
                .apply(
                    RequestOptions
                        .placeholderOf(R.drawable.ic_baseline_refresh_24)
                        .error(R.drawable.ic_baseline_broken_image_24)
                ).into(ivDetailStory)
        }
    }

    private fun setupView() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.detail_story)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}