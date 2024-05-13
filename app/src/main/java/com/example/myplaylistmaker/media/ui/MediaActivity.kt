package com.example.myplaylistmaker.media.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.ActivityMediaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewPager.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorites_tracks)
                1 -> tab.text = getString(R.string.playlists)

            }
        }
        tabMediator.attach()
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }


}