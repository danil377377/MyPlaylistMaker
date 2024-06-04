package com.example.myplaylistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment

import com.example.myplaylistmaker.databinding.FragmentSettingsBinding
import com.example.myplaylistmaker.settings.presentation.SettingsViewModel

import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment: Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    companion object {

        const val TAG = "SettingsFragment"
        fun newInstance(): Fragment {
            return SettingsFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: SettingsViewModel by viewModel()

        binding.themeSwitcher.isChecked = viewModel.getThemeSettings()
        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.updateThemeSettings(checked)

        }


        binding.contract.setOnClickListener {
            viewModel.openTherms()
        }

        binding.shareButton.setOnClickListener {

            viewModel.shareApp()
        }




        binding.writeToSupport.setOnClickListener {
            viewModel.openSupport()
        }


    }
}