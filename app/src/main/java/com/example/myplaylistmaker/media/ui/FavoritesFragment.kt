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
import com.example.myplaylistmaker.databinding.FragmentFavoritesBinding
import com.example.myplaylistmaker.media.presentation.FavoritesViewModel
import com.example.myplaylistmaker.search.domain.models.Track
import com.example.myplaylistmaker.search.ui.SearchFragment
import com.example.myplaylistmaker.search.ui.TrackAdapter
import com.example.myplaylistmaker.search.ui.presentation.TracksSearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstance() = FavoritesFragment()
    }

    private lateinit var tracksAdapter: TrackAdapter
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var binding: FragmentFavoritesBinding
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tracksAdapter = TrackAdapter(
        ) {
            if (clickDebounce()) {
                val bundle = Bundle().apply {
                    putSerializable("track", it)
                }
                findNavController().navigate(R.id.action_mediaContainerFragment_to_playerActivity, bundle)
            }
        }
        val recyclerView = binding.favoritesRecyclerView
        recyclerView.adapter = tracksAdapter

        val view: FavoritesViewModel by viewModel()
        viewModel = view
        lifecycleScope.launch {
        viewModel.getFavoritesTrackList()}

        viewModel.observeFavorites().observe(viewLifecycleOwner){
            if(it == ArrayList<Track>()) {
                binding.nothingFoundTextView.visibility = View.VISIBLE
                binding.errorImage.visibility = View.VISIBLE
                binding.favoritesRecyclerView.visibility = View.GONE
            }else{
                tracksAdapter.trackList.clear()
                tracksAdapter.trackList.addAll(it)
                tracksAdapter.notifyDataSetChanged()
                binding.nothingFoundTextView.visibility = View.GONE
                binding.errorImage.visibility = View.GONE
                binding.favoritesRecyclerView.visibility = View.VISIBLE

            }
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
        lifecycleScope.launch {
            viewModel.getFavoritesTrackList()
        }
    }

}