package com.example.myplaylistmaker.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
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
            findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
        }



        binding.mediaButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_mediaContainerFragment)
        }


        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
        }
    }
    }

