package com.example.myplaylistmaker.media.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.FragmentPlaylistBinding
import com.example.myplaylistmaker.databinding.FragmentPlaylistsBinding
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.media.presentation.PlaylistViewModel
import com.example.myplaylistmaker.search.domain.models.Track
import com.example.myplaylistmaker.search.ui.SearchFragment
import com.example.myplaylistmaker.search.ui.TrackAdapter
import com.example.myplaylistmaker.utility.StringUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class PlaylistFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel by viewModel<PlaylistViewModel>()
    private lateinit var tracksAdapter: TrackAdapter
    private var isClickAllowed = true
    private lateinit var playlist: Playlist

    companion object{
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
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
        playlist = requireArguments().getSerializable("playlist") as Playlist

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
            binding.trackCounting.text = "${StringUtils.getMinutesCountString(SimpleDateFormat("mm", Locale.getDefault()).format(it!!).toInt())} • ${StringUtils.getTrackCountString(playlist.quantityTracks)} "

        }

        tracksAdapter = TrackAdapter(
        ) {
            if (clickDebounce()) {
                val bundle = Bundle().apply {
                    putSerializable("track", it)
                }
                findNavController().navigate(R.id.action_playlistFragment_to_playerActivity, bundle)
            }
        }

        val recyclerView = binding.recyclerView
        recyclerView.adapter = tracksAdapter

            viewModel.getTracks(playlist)

        viewModel.observeTracks().observe(viewLifecycleOwner){
Log.d("треки", it.toString())
            tracksAdapter.trackList.clear()
            tracksAdapter.trackList.addAll(it)
            tracksAdapter.notifyDataSetChanged()


        }
    }


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTracks(playlist)
    }
}