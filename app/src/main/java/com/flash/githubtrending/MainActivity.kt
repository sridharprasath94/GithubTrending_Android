package com.flash.githubtrending

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.flash.githubtrending.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.startFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }

                R.id.trendingFragment,
                R.id.favoritesFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }

                else -> {
                    binding.bottomNav.visibility = View.GONE
                }
            }
        }
    }
}

//
//@AndroidEntryPoint
//class MainActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            GithubTrendingTheme {
//                AppNavHost()
//            }
//        }
//    }
//}
