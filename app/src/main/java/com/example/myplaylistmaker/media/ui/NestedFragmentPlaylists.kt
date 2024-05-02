package com.example.myplaylistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myplaylistmaker.databinding.FragmentPlaylistsNestedBinding
import com.example.myplaylistmaker.media.ui.presentation.FavoritesViewModel
import com.example.myplaylistmaker.media.ui.presentation.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NestedFragmentPlaylists: Fragment() {
    private var _binding: FragmentPlaylistsNestedBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistsViewModel by viewModel{
        parametersOf("")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsNestedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}