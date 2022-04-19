package com.umbat.storyappsubmission.view.main.home

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.umbat.storyappsubmission.databinding.FragmentHomeBinding
import com.umbat.storyappsubmission.model.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: StoryAdapter
    private lateinit var token: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        adapter = StoryAdapter()

        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(activity)
            rvStory.setHasFixedSize(true)
            rvStory.adapter = adapter
        }

        showLoading(true)
        setupViewModel()
    }

    private fun setupViewModel() {
        homeViewModel = ViewModelProvider(
            this,
            ViewModelProvider(UserPreference.getInstance(dataStore))
        )[HomeViewModel::class.java]

        homeViewModel.setStoriesList(token)
        homeViewModel.getStoriesList().observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        }
    }

    private fun showLoading(state: Boolean){
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding
    }
}