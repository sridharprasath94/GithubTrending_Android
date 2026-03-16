package com.flash.githubtrending.presentation.xml.trending


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
import com.flash.githubtrending.presentation.utils.fastSmoothScrollToTop
import com.flash.githubtrending.presentation.xml.shared.RepoAdapter
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
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
        observeEvents()
        observeSearchField()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
                    val firstVisible = layoutManager.findFirstVisibleItemPosition()

                    if (firstVisible > 0) {
                        binding.recyclerView.fastSmoothScrollToTop()
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        )
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchField() {
        binding.etSearch.doAfterTextChanged { text ->
            viewModel.onSearchQueryChanged(text?.toString().orEmpty())
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerView.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }

        adapter.setOnItemClickListener { repo ->
            val action =
                TrendingReposFragmentDirections
                    .actionTrendingToDetails(repo)

            findNavController().navigate(action)
        }

        adapter.setOnFavoriteClickListener { repo ->
            viewModel.toggleFavorite(repo)
            binding.recyclerView.scrollToPosition(0)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pagedRepos.collectLatest { pagingData ->
                    binding.fullScreenLoader.visibility = View.GONE
                    adapter.submitData(pagingData)
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.fullScreenLoader.visibility =
                        if (state.isLoading) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { error ->
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }
}