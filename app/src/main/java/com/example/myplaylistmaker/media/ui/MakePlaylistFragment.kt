package com.example.myplaylistmaker.media.ui

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.FragmentFavoritesBinding
import com.example.myplaylistmaker.databinding.FragmentMakePlaylistBinding
import com.example.myplaylistmaker.media.presentation.FavoritesViewModel
import com.example.myplaylistmaker.media.presentation.MakePlaylistViewModel
import com.example.myplaylistmaker.search.domain.models.Track
import com.example.myplaylistmaker.search.ui.TrackAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MakePlaylistFragment : Fragment() {

    private lateinit var binding: FragmentMakePlaylistBinding
    lateinit var confirmDialog: MaterialAlertDialogBuilder
    private val viewModel: MakePlaylistViewModel by viewModel()
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null)
            viewModel.onImageSelected(uri.toString())

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMakePlaylistBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.name.cursorColor= ContextCompat.getColorStateList(requireContext(), R.color.nav_bar_active)
        binding.description.cursorColor= ContextCompat.getColorStateList(requireContext(), R.color.nav_bar_active)
        val bottomNavigationView: BottomNavigationView =
            requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE
        binding.playlistPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        viewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                binding.playlistPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.playlistPhoto.setImageURI(Uri.parse(uri))
            }
        }
        viewModel.name.observe(viewLifecycleOwner) { name ->
            if (name.isNotEmpty()) binding.createButton.isEnabled = true
            else binding.createButton.isEnabled = false
        }

        binding.textInputName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onNameChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.textInputDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onDescriptionChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохранённые данные будут потеряны")
            .setNeutralButton("Отмена") { dialog, which -> }
            .setPositiveButton("Завершить") { dialog, which ->
                try {
                    findNavController().navigateUp()
                } catch (e: IllegalStateException) {
                    requireActivity().finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.shouldShowConfirmDialog()) {
                        confirmDialog.show()
                    } else {
                        try {
                            findNavController().navigateUp()
                        } catch (e: IllegalStateException) {
                            requireActivity().finish()
                        }
                    }
                }
            })

        binding.backButton.setOnClickListener {
            if (viewModel.shouldShowConfirmDialog()) {
                confirmDialog.show()
            } else {
                try {
                    findNavController().navigateUp()
                } catch (e: IllegalStateException) {
                    requireActivity().finish()
                }
            }
        }
        binding.createButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.saveToDb()
            }
            Toast.makeText(
                requireContext(),
                "Плейлист ${viewModel.name.value} создан",
                Toast.LENGTH_LONG
            )
                .show()
             try {
                findNavController().navigateUp()
            } catch (e: IllegalStateException) {
                requireActivity().finish()
            }


        }
    }
}