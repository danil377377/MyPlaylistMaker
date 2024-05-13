package com.example.myplaylistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.myplaylistmaker.databinding.FragmentContainerMediaBinding

import com.google.android.material.tabs.TabLayoutMediator

class MediaContainerFragment: Fragment() {

    private lateinit var binding: FragmentContainerMediaBinding
    private lateinit var tabMediator: TabLayoutMediator
    companion object {

        const val TAG = "MediaContainerFragment"
        fun newInstance() = MediaContainerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContainerMediaBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.viewPager.adapter = MediaViewPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = "Избранные треки"
                1 -> tab.text = "Плейлисты"

            }
        }
        tabMediator.attach()
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}