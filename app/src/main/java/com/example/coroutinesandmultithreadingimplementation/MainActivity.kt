package com.example.coroutinesandmultithreadingimplementation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.coroutinesandmultithreadingimplementation.common.BaseActivity
import com.example.coroutinesandmultithreadingimplementation.common.ToolbarManipulator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), ToolbarManipulator {

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.homeFragment,
            )
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()
        setupNavigation()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun setupNavigation() {
        /**
         * Connect [toolbar], [navController] and [appBarConfiguration] together.
         * This makes your [navController] aware of top-level fragments defined in appBarConfiguration.
         */
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun setScreenTitle(screenTitle: String?) {
        txt_screen_title.text = screenTitle
    }

    override fun showUpButton() {
        btn_back.visibility = View.VISIBLE
    }

    override fun hideUpButton() {
        btn_back.visibility = View.INVISIBLE
    }


}