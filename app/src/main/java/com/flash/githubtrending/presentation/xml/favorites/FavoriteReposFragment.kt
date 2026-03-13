package com.flash.githubtrending.presentation.xml.favorites


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.flash.githubtrending.R
import com.flash.githubtrending.databinding.FragmentFavoritesBinding
import com.flash.githubtrending.presentation.common.favorites.FavoriteReposViewModel
import com.flash.githubtrending.presentation.xml.shared.RepoAdapter
import com.flash.githubtrending.presentation.xml.trending.TrendingReposFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteReposFragment :
    Fragment(R.layout.fragment_favorites) {

    private val binding: FragmentFavoritesBinding by viewBinding(FragmentFavoritesBinding::bind)
    private val viewModel: FavoriteReposViewModel by viewModels()
    private val adapter = RepoAdapter(enableFavoritesIcon = false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeUiState()
    }


    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickListener { repo ->
            val action =
                FavoriteReposFragmentDirections
                    .actionFavoritesToDetails(repo)

            findNavController().navigate(action)
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