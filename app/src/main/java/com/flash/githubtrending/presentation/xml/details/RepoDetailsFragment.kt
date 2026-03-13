package com.flash.githubtrending.presentation.xml.details

import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.flash.githubtrending.R
import com.flash.githubtrending.databinding.FragmentDetailsBinding
import dev.androidbroadcast.vbpd.viewBinding

class RepoDetailsFragment :
    Fragment(R.layout.fragment_details) {
    private val binding: FragmentDetailsBinding by viewBinding(FragmentDetailsBinding::bind)
    private val args: RepoDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val repo = args.repo

        with(binding) {

            tvRepoName.text = repo.fullName
            tvOwner.text = buildString {
                append("Owner: ")
                append(repo.ownerName)
            }
            tvDescription.text = repo.description ?: "No description available"

            tvStats.text = buildString {
                append("⭐ ")
                append(repo.stars)
                append("   |   🍴 ")
                append(repo.forks)
            }
            tvLanguage.text = repo.language ?: "Unknown language"

            repo.ownerAvatarUrl?.let {
                ivAvatar.load(it)
            }

            btnRepoLink.setOnClickListener {
                openUrl(repo.repoHtmlUrl)
            }

            btnOwnerLink.setOnClickListener {
                openUrl(repo.ownerHtmlUrl)
            }
        }
    }

    private fun openUrl(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()

        customTabsIntent.launchUrl(requireContext(), url.toUri())
    }
}