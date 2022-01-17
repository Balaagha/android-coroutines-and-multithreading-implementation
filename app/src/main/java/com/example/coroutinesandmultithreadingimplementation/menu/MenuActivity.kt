package com.example.coroutinesandmultithreadingimplementation.menu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.coroutinesandmultithreadingimplementation.R
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.CoroutineOneActivity
import com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.MainActivity
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity(),MenuArrayAdapter.Listener {

    private lateinit var mAdapterScreensReachableFromMenu: MenuArrayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        mAdapterScreensReachableFromMenu = MenuArrayAdapter(this, ScreenReachableFromMainMenu.values(),this)
        listScreens.adapter = mAdapterScreensReachableFromMenu
    }

    override fun onScreenClicked(screenReachableFromHome: ScreenReachableFromMainMenu?) {
        when (screenReachableFromHome) {
            ScreenReachableFromMainMenu.MULTITHREADING_VASILITY -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            ScreenReachableFromMainMenu.COROUTINES_LUKAS -> {
                startActivity(Intent(this, CoroutineOneActivity::class.java))
            }
        }
    }
}