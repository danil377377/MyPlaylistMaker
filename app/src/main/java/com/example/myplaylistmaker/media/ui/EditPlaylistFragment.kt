package com.example.myplaylistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.media.presentation.EditPlaylistViewModel
import kotlinx.coroutines.launch
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
            if (it.pathToFile != null) {
                binding.playlistPhoto.setImageBitmap(viewModel.getImageBitmap(it))
            } else {
                binding.playlistPhoto.setImageResource(R.drawable.placeholder)
            }

    }

        super.binding.backButton.setOnClickListener{
            findNavController().popBackStack()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })

        super.binding.createButton.text = "Сохранить"
        super.binding.createButton.setOnClickListener{
            lifecycleScope.launch {
                viewModel.saveToDb()
            }
            viewModel.updatePlaylist(playlist.id)

            findNavController().navigateUp()
        }

    }

}