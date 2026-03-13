package com.flash.githubtrending.presentation.xml.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.flash.githubtrending.R
import com.flash.githubtrending.databinding.FragmentDetailsBinding
import com.flash.githubtrending.presentation.xml.details.RepoDetailsFragmentArgs
import dev.androidbroadcast.vbpd.viewBinding

class RepoDetailsFragment :
    Fragment(R.layout.fragment_details) {
    private val binding: FragmentDetailsBinding by viewBinding(FragmentDetailsBinding::bind)
    private val args: RepoDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            tvRepoName.text = args.repoName
        }
    }
}