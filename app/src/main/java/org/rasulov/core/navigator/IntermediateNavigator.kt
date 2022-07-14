package org.rasulov.core.navigator

import org.rasulov.core.screens.BaseScreen
import org.rasulov.core.utils.ResourceActions

class IntermediateNavigator : Navigator {

    private val target = ResourceActions<Navigator>()

    override fun launch(screen: BaseScreen) = target {
        it.launch(screen)
    }

    override fun goBack(result: Any?) = target {
        it.goBack(result)
    }

    fun setTarget(navigator: Navigator?) {
        target.resource = navigator
    }

    fun clear(){
        target.clear()
    }
}