package com.example.myplaylistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myplaylistmaker.databinding.FragmentMediaBinding

class FragmentMedia: Fragment() {

    companion object {
        private const val POSITION = "position"

        fun newInstance(position: Int) = FragmentMedia().apply {
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

        }


}