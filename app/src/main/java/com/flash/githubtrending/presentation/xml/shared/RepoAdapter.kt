package com.flash.githubtrending.presentation.xml.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flash.githubtrending.R
import com.flash.githubtrending.databinding.RowRepoBinding
import com.flash.githubtrending.domain.model.Repo

class RepoAdapter :
    ListAdapter<Repo, RepoAdapter.RepoViewHolder>(DiffCallback) {

    private var onItemClick: ((Repo) -> Unit)? = null
    private var onFavoriteClick: ((Repo) -> Unit)? = null

    class RepoViewHolder(
        private val binding: RowRepoBinding,
        private val onItemClick: ((Repo) -> Unit)?,
        private val onFavoriteClick: ((Repo) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(repo: Repo) {
            binding.repoName.text = repo.fullName

            val iconRes =
                if (repo.isFavorite)
                    R.drawable.ic_star_filled
                else
                    R.drawable.ic_star_outline

            binding.ivFavorite.setImageResource(iconRes)

            binding.ivFavorite.setOnClickListener {
                onFavoriteClick?.invoke(repo)
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
        return RepoViewHolder(binding, onItemClick, onFavoriteClick)
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