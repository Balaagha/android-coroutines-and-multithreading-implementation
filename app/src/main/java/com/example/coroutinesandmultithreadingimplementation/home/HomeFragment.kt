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
            ScreenReachableFromHome.PROBLEM_2 -> findNavController().navigate(R.id.problem2Fragment)
            ScreenReachableFromHome.UI_THREAD_DEMONSTRATION -> findNavController().navigate(R.id.uiThreadDemonstrationFragment)
            ScreenReachableFromHome.UI_HANDLER_DEMONSTRATION -> findNavController().navigate(R.id.uiHandlerDemonstrationFragment)
            ScreenReachableFromHome.CUSTOM_HANDLER_DEMONSTRATION -> findNavController().navigate(R.id.customHandlerDemonstrationFragment)
            ScreenReachableFromHome.PROBLEM_3 -> findNavController().navigate(R.id.problem3Fragment)
            ScreenReachableFromHome.ATOMICITY_DEMONSTRATION -> findNavController().navigate(R.id.atomicityDemonstrationFragment)
            ScreenReachableFromHome.EXERCISE_4 -> findNavController().navigate(R.id.problem4Fragment)
            ScreenReachableFromHome.EXERCISE_4_WITH_USE_CASE -> findNavController().navigate(R.id.problem4WithUseCaseSolutionFragment)
            ScreenReachableFromHome.THREAD_WAIT_DEMONSTRATION -> findNavController().navigate(R.id.threadWaitDemonstrationFragment)
            ScreenReachableFromHome.EXERCISE_5 -> findNavController().navigate(R.id.problem5Fragment)
            ScreenReachableFromHome.DESIGN_WITH_THREADS_DEMONSTRATION -> findNavController().navigate(R.id.designWithThreadDemonstrationFragment)
            ScreenReachableFromHome.DESIGN_WITH_THREAD_POOL_DEMONSTRATION -> findNavController().navigate(R.id.designWithThreadPoolDemonstrationFragment)
            ScreenReachableFromHome.DESIGN_WITH_ASYNCTASK_DEMONSTRATION -> findNavController().navigate(R.id.designWithAsyncTaskDemonstrationFragment)
            ScreenReachableFromHome.DESIGN_WITH_THREAD_POSTER_DEMONSTRATION -> findNavController().navigate(R.id.designWithThreadPosterDemonstrationFragment)
            ScreenReachableFromHome.DESIGN_WITH_COROUTINES_DEMONSTRATION -> findNavController().navigate(R.id.designWithCoroutinesDemonstrationFragment)
            ScreenReachableFromHome.EXERCISE_4_WITH_COROUTINES -> findNavController().navigate(R.id.problem4WithCoroutinesFragment)
        }
    }

}