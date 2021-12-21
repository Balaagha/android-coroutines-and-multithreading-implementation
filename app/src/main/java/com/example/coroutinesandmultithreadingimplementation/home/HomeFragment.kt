package com.example.coroutinesandmultithreadingimplementation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.example.coroutinesandmultithreadingimplementation.R
import com.example.coroutinesandmultithreadingimplementation.common.BaseFragment
import com.example.coroutinesandmultithreadingimplementation.common.ToolbarManipulator
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment(), HomeArrayAdapter.Listener {

    private lateinit var toolbarManipulator: ToolbarManipulator
    private lateinit var mListScreensReachableFromHomeView: ListView
    private lateinit var mAdapterScreensReachableFromHome: HomeArrayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbarManipulator = compositionRoot.getToolbarManipulator()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        mAdapterScreensReachableFromHome = HomeArrayAdapter(requireContext(), ScreenReachableFromHome.values(),this)
        mListScreensReachableFromHomeView = view.findViewById(R.id.list_screens)
        mListScreensReachableFromHomeView.adapter = mAdapterScreensReachableFromHome
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositionRoot.getToolbarManipulator()?.setScreenTitle("Home Fragment")
    }

    override fun onScreenClicked(screenReachableFromHome: ScreenReachableFromHome?) {
        when (screenReachableFromHome) {
            ScreenReachableFromHome.PROBLEM_1 -> findNavController().navigate(R.id.problem1Fragment)
        }
    }

}