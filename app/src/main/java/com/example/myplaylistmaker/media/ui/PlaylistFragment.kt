package com.example.myplaylistmaker.media.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.FragmentPlaylistBinding
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.media.presentation.PlaylistViewModel
import com.example.myplaylistmaker.search.ui.TrackAdapter
import com.example.myplaylistmaker.utility.StringUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class PlaylistFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel by viewModel<PlaylistViewModel>()
    private lateinit var tracksAdapter: TrackAdapter
    private var isClickAllowed = true
    private lateinit var playlist: Playlist
    lateinit var confirmDialog: MaterialAlertDialogBuilder

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavigationView: BottomNavigationView =
            requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE
        playlist = requireArguments().getSerializable("playlist") as Playlist

        if (playlist.pathToFile != null) {
            binding.playlistImage.setImageBitmap(
                viewModel.getImageBitmap(
                    playlist
                )
            )
            binding.playlistImageBottomView.setImageBitmap(
                viewModel.getImageBitmap(
                    playlist
                )
            )
        }
        val moreBottomSheetBehavior = BottomSheetBehavior.from(binding.moreBottomSheet)
        val overlay = binding.overlay
        moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.moreButton.setOnClickListener {
            moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            overlay.visibility = View.VISIBLE
        }
        moreBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        overlay.visibility = View.VISIBLE
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        overlay.visibility = View.VISIBLE
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.playlistName.text = playlist.name
        binding.playlistQuantity.text = StringUtils.getTrackCountString(playlist.quantityTracks)

        binding.constraintLayout.post {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
            val constraintLayoutBottom = binding.constraintLayout.bottom
            val coordinatorLayoutBottom = binding.coordinatorLayout.bottom
            bottomSheetBehavior.peekHeight = coordinatorLayoutBottom - constraintLayoutBottom - 24
        }
        binding.editInfoButtonBottomSheet.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("editPlaylist", playlist)
            }
            findNavController().navigate(
                R.id.action_playlistFragment_to_editPlaylistFragment,
                bundle
            )
        }

        binding.backButton.bringToFront()
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.header.text = playlist.name
        binding.description.text = playlist.description

        viewModel.getTotalTime(playlist)
        var countingTracks = playlist.quantityTracks
        viewModel.observeTotalTime().observe(viewLifecycleOwner) {
            binding.trackCounting.text = "${
                StringUtils.getMinutesCountString(
                    SimpleDateFormat(
                        "mm",
                        Locale.getDefault()
                    ).format(it!!).toInt()
                )
            } • ${StringUtils.getTrackCountString(countingTracks)} "
            binding.playlistQuantity.text = StringUtils.getTrackCountString(countingTracks)
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
        tracksAdapter.setOnItemLongClickListener {
            confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle("Хотите удалить трек?")
                .setNeutralButton("Отмена") { dialog, which -> }
                .setPositiveButton("Удалить") { dialog, which ->
                    viewModel.deleteTrackFromPlaylist(it, playlist)
                    viewModel.getTracks(playlist)
                    viewModel.getTotalTime(playlist)
                }
            confirmDialog.show()

            true
        }

        val recyclerView = binding.recyclerView
        recyclerView.adapter = tracksAdapter

        viewModel.getTracks(playlist)

        viewModel.observeTracks().observe(viewLifecycleOwner) {
            Log.d("треки", it.toString())
            tracksAdapter.trackList.clear()
            tracksAdapter.trackList.addAll(it)
            tracksAdapter.notifyDataSetChanged()
            countingTracks = it.size

        }
        binding.deletePlaylistButtonBottomSheet.setOnClickListener {
            moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle("Хотите удалить плэйлист «${playlist.name}»?")
                .setNeutralButton("Отмена") { dialog, which -> }
                .setPositiveButton("Удалить") { dialog, which ->
                    viewModel.deletePlaylistById(playlist.id)
                    findNavController().popBackStack()
                }
            confirmDialog.show()

            true
        }


        binding.shareButton.setOnClickListener {
            if (viewModel.checkTracList()) {
                Toast.makeText(
                    requireContext(),
                    "В этом плейлисте нет списка треков, которым можно поделиться",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                viewModel.sharePlaylist(viewModel.getPlaylistInfo(playlist), requireContext())

            }
        }
        binding.shareButtonBottomSheet.setOnClickListener {
            if (viewModel.checkTracList()) {
                Toast.makeText(
                    requireContext(),
                    "В этом плейлисте нет списка треков, которым можно поделиться",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                lifecycleScope.launch {
                    viewModel.sharePlaylist(viewModel.getPlaylistInfo(playlist), requireContext())
                }
            }
        }
        viewModel.observeName().observe(viewLifecycleOwner) {
            binding.header.text = it
            binding.playlistName.text = it
        }
        viewModel.observeDescription().observe(viewLifecycleOwner) {
            binding.description.text = it
        }
        viewModel.observeImage().observe(viewLifecycleOwner) {
            if (it != null) {
                binding.playlistImage.setImageBitmap(it)
                binding.playlistImageBottomView.setImageBitmap(it)
            } else {
                binding.playlistImage.setImageResource(R.drawable.placeholder)
                binding.playlistImageBottomView.setImageBitmap(it)
            }
        }
        viewModel.observePlaylist().observe(viewLifecycleOwner){
            playlist=it
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
        viewModel.getPlaylistName(playlist.id)
        viewModel.getPlaylistDescription(playlist.id)
        viewModel.getPlaylistImage(playlist.id)
        viewModel.getPlaylist(playlist.id)
    }
}