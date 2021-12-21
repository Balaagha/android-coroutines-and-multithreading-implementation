package com.example.coroutinesandmultithreadingimplementation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Choreographer
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.coroutinesandmultithreadingimplementation.common.BaseActivity
import com.example.coroutinesandmultithreadingimplementation.common.ToolbarManipulator
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Field
import java.lang.reflect.Modifier

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
        reduceChoreographerSkippedFramesWarningThreshold()
    }

    @SuppressLint("SoonBlockedPrivateApi")
    private fun reduceChoreographerSkippedFramesWarningThreshold() {
        var field: Field? = null
        try {
            field = Choreographer::class.java.getDeclaredField("SKIPPED_FRAME_WARNING_LIMIT")
            field.isAccessible = true
            field.setInt(field, field.modifiers and Modifier.FINAL.inv())
            field[null] = 1
        } catch (e: NoSuchFieldException) {
            // probably failed to change Choreographer's field, but it's not critical
        } catch (e: IllegalAccessException) {
        }
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