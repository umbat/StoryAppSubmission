package com.umbat.storyappsubmission.view.main.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.umbat.storyappsubmission.databinding.FragmentHomeBinding
import com.umbat.storyappsubmission.view.ViewModelFactory
import com.umbat.storyappsubmission.view.registration.welcome.WelcomeActivity

class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels { viewModelFactory }
    private lateinit var viewModelFactory: ViewModelFactory
    private var token = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(requireContext())
            rvStory.setHasFixedSize(true)
        }

        setupViewModel()
        setupAdapter()

        return root
    }

    private fun setupAdapter() {
        homeViewModel.getAllStoriesResponse.observe(viewLifecycleOwner) { adapter ->
            if (adapter != null) {
                binding.rvStory.adapter = StoryAdapter(adapter.listStory)
            }
        }
    }

    private fun setupViewModel() {
        viewModelFactory = ViewModelFactory.getInstance(requireContext())

        showLoading()
        homeViewModel.loadState().observe(viewLifecycleOwner) {
            token = it.token
            if (!it.isLogin) {
                intentActivity()
            } else {
                getStoriesList(token)
            }
        }
        showToast()
    }

    private fun showLoading() {
        homeViewModel.showLoading.observe(viewLifecycleOwner) {
            binding.progressBarHome.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun showToast() {
        homeViewModel.toastText.observe(viewLifecycleOwner) { toastText ->
            Toast.makeText(
                requireContext(), toastText, Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun intentActivity() {
        startActivity(Intent(requireContext(), WelcomeActivity::class.java))
    }

    private fun getStoriesList(token: String) {
        homeViewModel.getStoriesList(token)
    }
}