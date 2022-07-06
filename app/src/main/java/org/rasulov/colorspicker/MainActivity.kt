package org.rasulov.colorspicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import org.rasulov.colorspicker.screens.current_color_fragment.CurrentColorFragment
import org.rasulov.core.navigator.FragmentNavigator
import org.rasulov.core.navigator.IntermediateNavigator
import org.rasulov.core.screens.FragmentsHolder
import org.rasulov.core.uiactions.AndroidUiActions
import org.rasulov.core.utils.viewModelCreator

/**
 * This application is a single-activity app. MainActivity is a container
 * for all screens.
 */
class MainActivity : AppCompatActivity(), FragmentsHolder {


    private lateinit var navigator: FragmentNavigator
    private val activityViewModel by viewModelCreator<ActivityScopeViewModel> {
        ActivityScopeViewModel(
            AndroidUiActions(applicationContext),
            IntermediateNavigator()
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigator = FragmentNavigator(
            this,
            R.id.fragments_container
        ) { CurrentColorFragment.Screen() }
        navigator.onCreate(savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        navigator.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        // execute navigation actions only when activity is active
        activityViewModel.navigator.setTarget(navigator)

    }

    override fun onPause() {
        super.onPause()
        // postpone navigation actions if activity is not active
        activityViewModel.navigator.setTarget(null)
    }

    override fun notifyScreenUpdates() {
        navigator.notifyScreenUpdates()
    }

    override fun getActivityScopeViewModel(): ActivityScopeViewModel {
        return activityViewModel
    }

}