package com.anshul.statusgrabber.views.fragments

import android.media.audiofx.Equalizer.Settings
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anshul.statusgrabber.R
import com.anshul.statusgrabber.databinding.FragmentMediaBinding
import com.anshul.statusgrabber.databinding.FragmentSettingsBinding
import com.anshul.statusgrabber.models.SettingsModel
import com.anshul.statusgrabber.views.adapters.SettingsAdapter


class FragmentSettings : Fragment() {
    private val binding by lazy{
        FragmentSettingsBinding.inflate(layoutInflater)
    }

    private val list = ArrayList<SettingsModel>()
    private val adapter by lazy {
        SettingsAdapter(list, requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            settingsRecyclerView.adapter = adapter

            list.add(
                SettingsModel(
                    title = "How To Use",
                    desc = "Steps To Download Status"
                )
            )
            list.add(
                SettingsModel(
                    title = "Save Directory",
                    desc = "/internalstorage/Documents/${getString(R.string.app_name)}"
                )
            )
            list.add(
                SettingsModel(
                    title = "Disclaimer",
                    desc = "Read Disclaimer"
                )
            )
            list.add(
                SettingsModel(
                    title = "Privacy Policy",
                    desc = "Read Our Terms & Conditions"
                )
            )
            list.add(
                SettingsModel(
                    title = "Share",
                    desc = "Sharing is caring"
                )
            )
            list.add(
                SettingsModel(
                    title = "Rate Us",
                    desc = "Please support the app by rating on PlayStore"
                )
            )

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root
}