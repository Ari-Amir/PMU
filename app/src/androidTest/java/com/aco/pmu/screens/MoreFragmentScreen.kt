package com.aco.pmu.screens


import com.aco.pmu.R
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton



object MoreFragmentScreen : Screen<MoreFragmentScreen> () {

    val procedureButton = KButton { withId(R.id.proceduresButton) }
    val pigmentsButton = KButton { withId(R.id.pigmentsButton) }

}
