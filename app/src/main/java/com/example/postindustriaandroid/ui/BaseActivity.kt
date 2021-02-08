package com.example.postindustriaandroid.ui

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.services.BatteryReceiver
import com.example.postindustriaandroid.utils.SharedPrefsManager
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_base.*


class BaseActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPrefsManager.init(applicationContext)
        if (SharedPrefsManager.getTheme())
            setTheme(R.style.Theme_PostindustriaAndroid_Dark)
        setContentView(R.layout.activity_base)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
            findViewById<NavigationView>(R.id.nav_view)
                .setupWithNavController(navController)
        val layout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawer_layout)
        layout.setupWithNavController(toolbar, navController, appBarConfiguration)

        this.registerReceiver(
            BatteryReceiver,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}