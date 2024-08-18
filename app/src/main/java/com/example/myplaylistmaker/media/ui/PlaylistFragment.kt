package com.example.myplaylistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.FragmentPlaylistBinding
import com.example.myplaylistmaker.databinding.FragmentPlaylistsBinding
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.media.presentation.PlaylistViewModel
import com.example.myplaylistmaker.utility.StringUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


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
        val bottomNavigationView: BottomNavigationView =
            requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE
        val playlist = requireArguments().getSerializable("playlist") as Playlist
        if(playlist.pathToFile != null) binding.playlistImage.setImageBitmap(viewModel.getImageBitmap(playlist))

        binding.constraintLayout.post {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)

            val constraintLayoutBottom = binding.constraintLayout.bottom
            val coordinatorLayoutBottom = binding.coordinatorLayout.bottom
            bottomSheetBehavior.peekHeight = coordinatorLayoutBottom - constraintLayoutBottom - 24
        }
        binding.backButton.bringToFront()
        binding.backButton.setOnClickListener{
            findNavController().navigateUp()
        }
        binding.description.text = playlist.description
        viewModel.getTotalTime(playlist)
        viewModel.observeTotalTime().observe(viewLifecycleOwner){
            binding.trackCounting.text = "${SimpleDateFormat("mm", Locale.getDefault()).format(it!!)} минут • ${StringUtils.getTrackCountString(playlist.quantityTracks)} "

        }

    }
}