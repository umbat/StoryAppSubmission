package com.umbat.storyappsubmission.view.main.logout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umbat.storyappsubmission.databinding.FragmentLogoutBinding

class LogoutFragment : Fragment() {
    private lateinit var logoutViewModel: LogoutViewModel
    private var _binding: FragmentLogoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupAction()

        return root
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            logoutViewModel.logout()
        }
    }

    private fun intentFragment() {

    }
}