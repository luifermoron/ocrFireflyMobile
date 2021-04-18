package com.opensource.autofill

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var navGraph: NavGraph

    companion object {
        private const val SHOULD_LAUNCH_MAIN = "SHOULD_LAUNCH_MAIN"
        fun open(context: Context, hasTagsSetup: Boolean) {
            context.startActivity(Intent(context, MainActivity::class.java).apply {
                putExtra(SHOULD_LAUNCH_MAIN, hasTagsSetup)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val graphInflater = navHostFragment.navController.navInflater

        navGraph = graphInflater.inflate(R.navigation.mobile_navigation)
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard))
        val destination = if (intent.getBooleanExtra(
                        SHOULD_LAUNCH_MAIN,
                        false
                )
        ) R.id.navigation_home else R.id.navigation_dashboard

        navGraph.startDestination = destination
        navController.graph = navGraph
        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setupWithNavController(navController)
    }
}