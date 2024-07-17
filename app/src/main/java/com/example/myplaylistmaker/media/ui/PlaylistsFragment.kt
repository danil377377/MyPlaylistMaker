package com.example.myplaylistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myplaylistmaker.R

import com.example.myplaylistmaker.databinding.FragmentPlaylistsBinding
import com.example.myplaylistmaker.media.domain.ImageDecoder
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.media.presentation.MakePlaylistViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    private lateinit var binding: FragmentPlaylistsBinding
    private val viewModel: MakePlaylistViewModel by viewModel()
    private val imageDecoder: ImageDecoder by inject()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
binding.newPlaylistButton.setOnClickListener{
    findNavController().navigate(R.id.action_mediaContainerFragment_to_makePlaylistFragment)
}
        val errorIcon = binding.errorImage
        val errorText = binding.nothingFoundTextView
        val recyclerView = binding.recyclerView
viewModel.getListOfPlaylists()
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = PlaylistsAdapter(viewModel.lastPlaylists,{

        }, imageDecoder)
        recyclerView.adapter = adapter
        lifecycleScope.launch {
            viewModel.getListOfPlaylists()}
        viewModel.observePlaylists().observe(viewLifecycleOwner){
            if(it != emptyList<Playlist>()) {
                recyclerView.isVisible = true
                errorIcon.isVisible = false
                errorText.isVisible = false
                adapter.clear()
                adapter.addAll(it)
                adapter.notifyDataSetChanged()
            }
            else{
                recyclerView.isVisible = false
                errorIcon.isVisible = true
                errorText.isVisible = true
            }
        }
    }


}