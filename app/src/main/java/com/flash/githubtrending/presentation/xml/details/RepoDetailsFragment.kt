package com.flash.githubtrending.presentation.xml.details

import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import com.flash.githubtrending.R
import com.flash.githubtrending.databinding.FragmentDetailsBinding
import com.flash.githubtrending.domain.model.Repo
import com.flash.githubtrending.presentation.common.details.RepoDetailsUiState
import com.flash.githubtrending.presentation.common.details.RepoDetailsViewModel
import dev.androidbroadcast.vbpd.viewBinding
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepoDetailsFragment :
    Fragment(R.layout.fragment_details) {
    private val binding: FragmentDetailsBinding by viewBinding(FragmentDetailsBinding::bind)
    private val args: RepoDetailsFragmentArgs by navArgs()
    private val viewModel: RepoDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val repoId = args.repoId

        viewModel.loadRepo(repoId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        RepoDetailsUiState.Initial -> {
                        }

                        is RepoDetailsUiState.Success -> {
                            bindRepo(state.repo)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    binding.root.showCenteredSnackBar(event.message)
                }
            }
        }
    }

    private fun openUrl(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()

        customTabsIntent.launchUrl(requireContext(), url.toUri())
    }


    private fun bindRepo(repo: Repo) {
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
}

fun View.showCenteredSnackBar(message: String) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)

    val textView = snackBar.view.findViewById<MaterialTextView>(
        com.google.android.material.R.id.snackbar_text
    )
    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    textView.gravity = android.view.Gravity.CENTER

    snackBar.show()
}