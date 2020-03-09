package com.aco.pmu.screens

import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.aco.pmu.R


object MainActivityScreen : Screen<MainActivityScreen> () {

    val clientsButton = KButton { withId(R.id.navigation_clients) }
    val moreButton = KButton { withId(R.id.navigation_more) }
    val recordsButton = KButton { withId(R.id.navigation_records) }
}