package com.flash.githubtrending.presentation.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.flash.githubtrending.presentation.composables.details.RepoDetailsScreen
import com.flash.githubtrending.presentation.composables.start.StartScreen
import com.flash.githubtrending.presentation.composables.trending.TrendingScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "start"
    ) {

        composable("start") {
            StartScreen(
                onOpenClick = {
                    navController.navigate("trending")
                }
            )
        }

        composable("trending") {
            TrendingScreen(
                onRepoClick = { repoName ->
                    navController.navigate("details/$repoName")
                }
            )
        }

        composable(
            route = "details/{repoName}",
            arguments = listOf(navArgument("repoName") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val repoName =
                backStackEntry.arguments?.getString("repoName")

            RepoDetailsScreen(repoName = repoName ?: "")
        }
    }
}