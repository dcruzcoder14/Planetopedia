package com.example.planetopedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // Declare AppBarConfiguration, which is used to configure the NavigationView and ActionBar
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var planetDatabase: PlanetDatabase

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to activity_main.xml
        setContentView(R.layout.activity_main)
        // Set the toolbar as the action bar for this activity
        setSupportActionBar(findViewById(R.id.toolbar))

        // Find the DrawerLayout and NavigationView in the layout
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        // Find the NavController for the navigation host fragment
        val navController = findNavController(R.id.nav_host_fragment)

        // Set a custom navigation item selected listener for the NavigationView
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.planetsListFragment -> {
                    // Navigate to PlanetsListFragment
                    navController.navigate(R.id.planetsListFragment)

                    true
                }
                R.id.addPlanetFragment -> {
                    // Navigate to AddPlanetFragment
                    navController.navigate(R.id.addPlanetFragment)
                    true
                }
                R.id.updatePlanetFragment -> {
                    // Navigate to SelectPlanetToUpdateFragment
                    navController.navigate(R.id.selectPlanetToUpdateFragment)
                    true
                }

                R.id.deletePlanetFragment -> {
                    // Navigate to SelectPlanetToDeleteFragment
                    navController.navigate(R.id.selectPlanetToDeleteFragment)
                    true
                }
                else -> false
            }
        }

        // Initialize AppBarConfiguration with the set of top-level destinations and the DrawerLayout
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.planetsListFragment, R.id.addPlanetFragment, R.id.selectPlanetToUpdateFragment),
            drawerLayout
        )

        // Setup the action bar with the NavController and the AppBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)
        // Setup the NavigationView with the NavController
        navView.setupWithNavController(navController)

        // Set up the app database and insert sample data
        planetDatabase = PlanetDatabase.getDatabase(this)

        // Add the sample planets to the database
        GlobalScope.launch {
            BootstrapData.insertPlanets(planetDatabase)
        }


    }

    // Handle up navigation in the action bar
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        // Navigate up with the AppBarConfiguration or call the superclass implementation
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
