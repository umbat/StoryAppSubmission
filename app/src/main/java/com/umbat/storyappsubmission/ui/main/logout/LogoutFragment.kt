package com.umbat.storyappsubmission.ui.main.logout

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.umbat.storyappsubmission.databinding.FragmentLogoutBinding
import com.umbat.storyappsubmission.ui.ViewModelFactory
import com.umbat.storyappsubmission.ui.registration.welcome.WelcomeActivity

class LogoutFragment : Fragment() {
    private var _binding: FragmentLogoutBinding? = null
    private val binding get() = _binding!!
    private val logoutViewModel: LogoutViewModel by viewModels { viewModelFactory }
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogoutBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModelFactory = ViewModelFactory.getInstance(requireContext())

        setupAction()

        return root
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            logoutViewModel.logout()
            intentFragment()
        }
    }

    private fun intentFragment() {
        activity?.let{
            val intent = Intent (it, WelcomeActivity::class.java)
            it.startActivity(intent)
        }
    }
}