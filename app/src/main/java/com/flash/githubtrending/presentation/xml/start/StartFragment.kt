package com.flash.githubtrending.presentation.xml.start

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.flash.githubtrending.R
import com.flash.githubtrending.databinding.FragmentStartBinding
import dev.androidbroadcast.vbpd.viewBinding

class StartFragment : Fragment(R.layout.fragment_start) {
    private val binding: FragmentStartBinding by viewBinding(FragmentStartBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      with(binding){
            btnOpen.setOnClickListener {
                findNavController().navigate(R.id.action_start_to_trending)
            }
      }
    }
}