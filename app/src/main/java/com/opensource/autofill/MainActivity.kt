package com.opensource.autofill
/*
  ~ Copyright (c)  2021 Luis Morón
  ~
  ~ This program is free software: you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation, either version 3 of the License, or
  ~ (at your option) any later version.
  ~
  ~ This program is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~ GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.opensource.autofill.ui.AboutActivity

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var navGraph: NavGraph

    companion object {
        private const val SHOULD_LAUNCH_HOME = "SHOULD_LAUNCH_HOME"
        fun open(context: Context, hasTagsSetup: Boolean, imageUri: String?) {
            context.startActivity(Intent(context, MainActivity::class.java).apply {
                putExtra(SHOULD_LAUNCH_HOME, hasTagsSetup)
                putExtra("imageUri", imageUri)
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
                        SHOULD_LAUNCH_HOME,
                        false
                )
        ) R.id.navigation_home else R.id.navigation_dashboard

        navGraph.startDestination = destination
        navController.graph = navGraph

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setupWithNavController(navController)
    }
}