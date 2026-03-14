package com.flash.githubtrending.presentation.xml.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flash.githubtrending.R
import com.flash.githubtrending.databinding.RowRepoBinding
import com.flash.githubtrending.domain.model.Repo

class RepoAdapter(private val enableFavoritesIcon: Boolean = true) :
    ListAdapter<Repo, RepoAdapter.RepoViewHolder>(DiffCallback) {

    private var onItemClick: ((Repo) -> Unit)? = null
    private var onFavoriteClick: ((Repo) -> Unit)? = null

    class RepoViewHolder(
        private val binding: RowRepoBinding,
        private val onItemClick: ((Repo) -> Unit)?,
        private val onFavoriteClick: ((Repo) -> Unit)?,
        private val enableFavoritesIcon: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(repo: Repo) {
            binding.repoName.text = repo.fullName

            binding.repoOwner.text = repo.ownerName

            binding.repoLanguage.text = repo.language ?: "Unknown"

            binding.repoStars.text = buildString {
                append("★ ")
                append(repo.stars)
            }

            binding.repoForks.text = buildString {
                append("🍴 ")
                append(repo.forks)
            }

            if (enableFavoritesIcon) {
                val iconRes =
                    if (repo.isFavorite)
                        R.drawable.ic_star_filled
                    else
                        R.drawable.ic_star_outline

                val colorFilter = if (repo.isFavorite) {
                    binding.root.context.getColor(R.color.star_filled)
                } else {
                    binding.root.context.getColor(R.color.star_outline)
                }

                binding.ivFavorite.setImageResource(iconRes)
                binding.ivFavorite.setColorFilter(colorFilter)

                binding.ivFavorite.setOnClickListener {
                    onFavoriteClick?.invoke(repo)
                }
            } else {
                binding.ivFavorite.visibility = android.view.View.GONE
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(repo)
            }
        }
    }

    fun setOnItemClickListener(listener: (Repo) -> Unit) {
        onItemClick = listener
    }

    fun setOnFavoriteClickListener(listener: (Repo) -> Unit) {
        onFavoriteClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = RowRepoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RepoViewHolder(binding, onItemClick, onFavoriteClick, enableFavoritesIcon)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object DiffCallback : DiffUtil.ItemCallback<Repo>() {
        override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem == newItem
        }
    }

}