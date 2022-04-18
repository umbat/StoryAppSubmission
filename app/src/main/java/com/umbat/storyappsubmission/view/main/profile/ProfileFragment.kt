package com.umbat.storyappsubmission.view.main.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.umbat.storyappsubmission.R
import com.umbat.storyappsubmission.databinding.FragmentProfileBinding
import com.umbat.storyappsubmission.model.UserPreference
import com.umbat.storyappsubmission.view.ViewModelFactory
import com.umbat.storyappsubmission.view.registration.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileFragment : Fragment() {
    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        setupViewModel()
        setupAction()

        return root
    }

//    private fun setupViewModel() {
//        profileViewModel = ViewModelProvider(
//            requireActivity(),
//            ViewModelFactory(UserPreference.getInstance(dataStore))
//        )[ProfileViewModel::class.java]
//
//        profileViewModel.getUser().observe(requireActivity()) { user ->
//            if (user.isLogin) {
//                binding.nameTextView.text = getString(R.string.greeting, user.name)
//            } else {
//                startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
//            }
//        }
//    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            profileViewModel.logout()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}