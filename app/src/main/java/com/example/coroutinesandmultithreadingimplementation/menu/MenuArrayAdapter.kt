package com.example.coroutinesandmultithreadingimplementation.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.coroutinesandmultithreadingimplementation.R

class MenuArrayAdapter(context: Context, values: Array<ScreenReachableFromMainMenu>, private val mListener: Listener) :
    ArrayAdapter<ScreenReachableFromMainMenu>(context, 0, values) {

    interface Listener {
        fun onScreenClicked(screenReachableFromHome: ScreenReachableFromMainMenu?)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_screen_reachable_from_home, parent, false)

        val screenReachableFromHome = getItem(position)

        // display screen name
        val txtName = view.findViewById<TextView>(R.id.txt_screen_name)
        txtName.text = screenReachableFromHome!!.getName()

        // set click listener on individual item view
        view.setOnClickListener { mListener.onScreenClicked(screenReachableFromHome) }
        return view
    }
}
