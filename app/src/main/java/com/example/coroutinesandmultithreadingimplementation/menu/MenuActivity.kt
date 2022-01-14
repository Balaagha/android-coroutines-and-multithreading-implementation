package com.example.coroutinesandmultithreadingimplementation.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.coroutinesandmultithreadingimplementation.R
import com.example.coroutinesandmultithreadingimplementation.androidcoroutinesmasterclass.CoroutineMainActivity
import com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.MainActivity
import com.example.coroutinesandmultithreadingimplementation.androidmultithreadingmasterclass.home.HomeArrayAdapter
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
                startActivity(Intent(this, CoroutineMainActivity::class.java))
            }
        }
    }
}