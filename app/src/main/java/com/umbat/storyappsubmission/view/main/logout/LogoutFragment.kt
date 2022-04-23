package com.umbat.storyappsubmission.view.main.logout

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.umbat.storyappsubmission.databinding.FragmentProfileBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileFragment : Fragment() {
    private lateinit var logoutViewModel: LogoutViewModel
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
            logoutViewModel.logout()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}