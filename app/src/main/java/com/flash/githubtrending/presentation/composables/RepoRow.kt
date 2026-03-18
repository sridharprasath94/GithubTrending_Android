package com.flash.githubtrending.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.flash.githubtrending.domain.model.Repo

@Composable
fun RepoRow(
    repo: Repo,
    onRepoClick: (Repo) -> Unit,
    onToggleFavorite: (Repo) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFavorite by remember(repo.fullName, repo.isFavorite) {
        mutableStateOf(repo.isFavorite)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onRepoClick(repo) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = repo.fullName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = {
            isFavorite = !isFavorite   // optimistic toggle
            onToggleFavorite(repo)
        }) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}