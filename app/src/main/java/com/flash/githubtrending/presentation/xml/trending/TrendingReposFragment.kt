package com.flash.githubtrending.presentation.xml.trending


import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.flash.githubtrending.R
import com.flash.githubtrending.databinding.FragmentTrendingBinding
import com.flash.githubtrending.presentation.common.trending.TrendingReposViewModel
import com.flash.githubtrending.presentation.xml.shared.RepoAdapter
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrendingReposFragment :
    Fragment(R.layout.fragment_trending) {

    private val binding: FragmentTrendingBinding by viewBinding(FragmentTrendingBinding::bind)
    private val viewModel: TrendingReposViewModel by viewModels()
    private val adapter = RepoAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeUiState()
        setupSearchButton()
        observeSearchField()

        viewModel.refresh()
    }

    private fun setupSearchButton() {
        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString()
            if (query.isNotBlank()) {
                viewModel.search(query)
            } else {
                viewModel.clearSearch()
            }
        }
    }

    private fun observeSearchField() {
        binding.etSearch.doAfterTextChanged { text ->
            if (text.isNullOrBlank()) {
                viewModel.clearSearch()
            }
        }
        binding.etSearch.setOnEditorActionListener { _, actionId, event ->

            val isKeyboardEnter =
                event?.keyCode == KeyEvent.KEYCODE_ENTER &&
                        event.action == KeyEvent.ACTION_DOWN

            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED ||
                isKeyboardEnter
            ) {
                val query = binding.etSearch.text.toString()

                if (query.isNotBlank()) {
                    viewModel.search(query)
                } else {
                    viewModel.clearSearch()
                }

                true
            } else {
                false
            }
        }

    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickListener { repo ->
            val action =
                TrendingReposFragmentDirections.Companion
                    .actionTrendingToDetails(repo.fullName)

            findNavController().navigate(action)
        }

        adapter.setOnFavoriteClickListener { repo ->
            viewModel.toggleFavorite(repo)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when {
                        state.isLoading && state.repos.isEmpty() -> {
                            binding.fullScreenLoader.visibility = View.VISIBLE
                            adapter.submitList(emptyList())
                        }

                        state.error != null -> {
                            binding.fullScreenLoader.visibility = View.GONE
                            adapter.submitList(emptyList())
                            Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            binding.fullScreenLoader.visibility = View.GONE
                            adapter.submitList(state.repos) {
                                binding.recyclerView.scrollToPosition(0)
                            }
                        }
                    }
                }
            }
        }
    }
}