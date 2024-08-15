package com.example.myplaylistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myplaylistmaker.databinding.FragmentPlaylistBinding
import com.example.myplaylistmaker.databinding.FragmentPlaylistsBinding
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.media.presentation.PlaylistViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel by viewModel<PlaylistViewModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlist = requireArguments().getSerializable("playlist") as Playlist
        if(playlist.pathToFile != null) binding.playlistImage.setImageBitmap(viewModel.getImageBitmap(playlist))

        binding.constraintLayout.post {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)

            val constraintLayoutBottom = binding.constraintLayout.bottom
            val coordinatorLayoutBottom = binding.coordinatorLayout.bottom
            bottomSheetBehavior.peekHeight = coordinatorLayoutBottom - constraintLayoutBottom - 24
        }
    }
}