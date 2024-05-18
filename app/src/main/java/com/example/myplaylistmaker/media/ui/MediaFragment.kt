package com.example.myplaylistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myplaylistmaker.databinding.FragmentMediaBinding

class MediaFragment: Fragment() {

    companion object {
        private const val POSITION = "position"
        const val TAG = "MediaFragment"
        fun newInstance(position: Int) = MediaFragment().apply {
            arguments = Bundle().apply {
                putInt(POSITION, position)
            }

        }
    }

    private lateinit var binding: FragmentMediaBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nothingFoundTextView.text = when(requireArguments().getInt(POSITION)){
            0 -> "Ваша медиатека пуста"
            1 -> "Вы не создали ни одного плейлиста"
            else -> {""}
        }
        binding.newPlaylistButton.visibility = if (requireArguments().getInt(POSITION) == 1) {
            View.VISIBLE
        } else {
            View.GONE
        }
        }


}