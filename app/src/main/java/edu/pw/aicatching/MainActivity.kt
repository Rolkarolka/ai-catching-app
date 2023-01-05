package edu.pw.aicatching

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import edu.pw.aicatching.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AICatching)
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in listOf(R.id.mainFragment, R.id.authorizationFragment)) {
                binding.mainToolbar.visibility = View.GONE
            } else {
                binding.mainToolbar.visibility = View.VISIBLE
            }
        }
        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.mainFragment, R.id.authorizationFragment)
        )
        setSupportActionBar(binding.mainToolbar)
        binding.mainToolbar.setupWithNavController(navController, appBarConfiguration)
        binding.mainToolbar.setNavigationOnClickListener {
            val currentNavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = currentNavHostFragment.navController
            if (navController.currentDestination?.id == R.id.clothDescriptionFragment &&
                navController.popBackStack(R.id.wardrobeFragment, false)
            ) {
            } else if (navController.currentDestination?.id == R.id.clothDescriptionFragment &&
                navController.popBackStack(R.id.cameraFragment, false)
            ) {
            } else {
                navController.popBackStack()
            }
        }
    }
}
