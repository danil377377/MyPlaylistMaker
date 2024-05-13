package com.example.myplaylistmaker.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.FragmentMainBinding
import com.example.myplaylistmaker.media.ui.MediaContainerFragment
import com.example.myplaylistmaker.search.ui.SearchFragment
import com.example.myplaylistmaker.settings.ui.SettingsFragment

class MainFragment: Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchButton.setOnClickListener{
            // Навигируемся на следующий экран
            parentFragmentManager.commit {
                replace(
                    R.id.rootFragmentContainerView,
                    SearchFragment.newInstance(),
                    SearchFragment.TAG
                )


                addToBackStack(SearchFragment.TAG)
            }
        }



        binding.mediaButton.setOnClickListener {
            parentFragmentManager.commit {
                replace(
                    R.id.rootFragmentContainerView,
                    MediaContainerFragment.newInstance(),
                    MediaContainerFragment.TAG
                )


                addToBackStack(MediaContainerFragment.TAG)
            }
        }


        binding.settingsButton.setOnClickListener {
            parentFragmentManager.commit {
                replace(
                    R.id.rootFragmentContainerView,
                    SettingsFragment.newInstance(),
                    SettingsFragment.TAG
                )


                addToBackStack(SettingsFragment.TAG)
            }
        }
    }
    }

