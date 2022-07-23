package org.rasulov.core.navigator

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.rasulov.colorspicker.R
import org.rasulov.core.screens.BaseFragment
import org.rasulov.core.screens.BaseScreen
import org.rasulov.core.screens.HasScreenTitle
import org.rasulov.core.utils.ARG_SCREEN
import org.rasulov.core.utils.Event

class FragmentNavigator(
    private val activity: AppCompatActivity,
    @IdRes private val containerId: Int,
    private val initialScreen: () -> BaseScreen
) : Navigator {

    private var result: Event<Any>? = null


    fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            // define the initial screen that should be launched when app starts.
            launchFragment(
                screen = initialScreen(),
                addToBackStack = false
            )
        }
        activity
            .supportFragmentManager
            .registerFragmentLifecycleCallbacks(fragmentCallbacks, false)

    }

    fun onDestroy() {
        activity
            .supportFragmentManager
            .unregisterFragmentLifecycleCallbacks(fragmentCallbacks)

    }


    override fun launch(screen: BaseScreen) {
        launchFragment(screen)
    }

    override fun goBack(result: Any?) {
        if (result != null) {
            this.result = Event(result)
        }
        activity.onBackPressed()
    }


    private fun launchFragment(screen: BaseScreen, addToBackStack: Boolean = true) {

        val fragment = screen.javaClass.enclosingClass.newInstance() as Fragment
        fragment.arguments = bundleOf(ARG_SCREEN to screen)

        val transaction = activity.supportFragmentManager.beginTransaction()
        if (addToBackStack) transaction.addToBackStack(null)
        transaction
            .setCustomAnimations(
                R.anim.enter,
                R.anim.exit,
                R.anim.pop_enter,
                R.anim.pop_exit
            )
            .replace(containerId, fragment)
            .commit()
    }

    private val fragmentCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {

            notifyScreenUpdates()

            val result = result?.getValue() ?: return
            if (f is BaseFragment) {
                // has result that can be delivered to the screen's view-model
                f.viewModel.onResult(result)
            }
        }
    }

    fun notifyScreenUpdates() {
        val f = activity.supportFragmentManager.findFragmentById(R.id.fragments_container)

        if (activity.supportFragmentManager.backStackEntryCount > 0) {
            // more than 1 screen -> show back button in the toolbar
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        if (f is HasScreenTitle && f.getScreenTitle() != null) {
            // fragment has custom screen title -> display it
            activity.supportActionBar?.title = f.getScreenTitle()
        } else {
            activity.supportActionBar?.title = activity.getString(R.string.app_name)
        }

    }

    fun onBackPressed() {
        val f = activity.supportFragmentManager.findFragmentById(R.id.fragments_container)
        if (f is BaseFragment) {
            f.viewModel.onBackPressed()
        }
    }
}