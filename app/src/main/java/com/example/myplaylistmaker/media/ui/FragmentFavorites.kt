package com.example.myplaylistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myplaylistmaker.databinding.FragmentFavoritesNestedBinding
import com.example.myplaylistmaker.media.ui.presentation.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class FragmentFavorites: Fragment() {
    private var _binding: FragmentFavoritesNestedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModel{
    parametersOf("")}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =FragmentFavoritesNestedBinding.inflate(inflater, container, false)
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