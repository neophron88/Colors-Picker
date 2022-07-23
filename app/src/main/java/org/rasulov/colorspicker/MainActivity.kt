package org.rasulov.colorspicker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.rasulov.colorspicker.screens.current_color_fragment.CurrentColorFragment
import org.rasulov.core.ActivityScopeViewModel
import org.rasulov.core.FragmentsHolder
import org.rasulov.core.navigator.FragmentNavigator
import org.rasulov.core.navigator.IntermediateNavigator
import org.rasulov.core.uiactions.AndroidUiActions
import org.rasulov.core.utils.viewModelCreator

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
        Log.d("it0088", "onDestroy:")

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.navigator.setTarget(navigator)

    }

    override fun onPause() {
        super.onPause()
        activityViewModel.navigator.setTarget(null)
        Log.d("it0088", "onPause: ")
    }


    override fun notifyScreenUpdates() {
        navigator.notifyScreenUpdates()
    }

    override fun getActivityScopeViewModel(): ActivityScopeViewModel {
        return activityViewModel
    }

    override fun onBackPressed() {
        navigator.onBackPressed()
        super.onBackPressed()
    }
}