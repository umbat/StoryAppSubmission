package com.umbat.storyappsubmission.ui.main.home

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.umbat.storyappsubmission.databinding.FragmentHomeBinding
import com.umbat.storyappsubmission.ui.ViewModelFactory
import com.umbat.storyappsubmission.ui.registration.welcome.WelcomeActivity

class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelFactory: ViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { viewModelFactory }
    private var token = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        setupViewModel()

        val storyAdapter = StoryAdapter()
        binding.rvStory.adapter = storyAdapter
        homeViewModel.loadState().observe(viewLifecycleOwner) { pref ->

            homeViewModel.getStoriesList(pref.token).observe(viewLifecycleOwner) { pagingData ->
                storyAdapter.submitData(lifecycle, pagingData)
            }
        }

        return root
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