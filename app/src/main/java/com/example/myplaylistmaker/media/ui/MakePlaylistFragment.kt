package com.example.myplaylistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.FragmentFavoritesBinding
import com.example.myplaylistmaker.databinding.FragmentMakePlaylistBinding
import com.example.myplaylistmaker.media.presentation.FavoritesViewModel
import com.example.myplaylistmaker.search.domain.models.Track
import com.example.myplaylistmaker.search.ui.TrackAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MakePlaylistFragment: Fragment() {



    private lateinit var viewModel: FavoritesViewModel
    private lateinit var binding: FragmentMakePlaylistBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMakePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        }


    }