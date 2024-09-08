package com.example.myplaylistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.media.presentation.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment: MakePlaylistFragment() {
    override val viewModel: EditPlaylistViewModel by viewModel()
    private lateinit var playlist: Playlist
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlist = requireArguments().getSerializable("editPlaylist") as Playlist
viewModel.initPlaylist(playlist)
        viewModel.observePlaylist().observe(viewLifecycleOwner) {
            super.binding.textInputName.setText(playlist.name)
            super.binding.textInputDescription.setText(playlist.description)
            super.binding.playlistPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
            super.binding.playlistPhoto.setImageBitmap(viewModel.getImageBitmap(playlist))

        }
    }
}