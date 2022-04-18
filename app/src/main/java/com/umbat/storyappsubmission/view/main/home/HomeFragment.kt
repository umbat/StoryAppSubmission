package com.umbat.storyappsubmission.view.main.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.umbat.storyappsubmission.databinding.FragmentHomeBinding
import com.umbat.storyappsubmission.view.main.detail.DetailActivity

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(requireActivity())
            rvStory.setHasFixedSize(true)
            rvStory.adapter = adapter
        }

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.getStoriesList().observe(requireActivity()) {
            if (it != null) {
                adapter.setList(it)
//                showLoading(false)
            }
        }
        return root
    }

//    private fun showLoading(state: Boolean){
//        if (state) {
//            binding.progressBar.visibility = View.VISIBLE
//        } else {
//            binding.progressBar.visibility = View.GONE
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding
    }
}