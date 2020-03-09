package com.aco.pmu.AddNewRecord

import androidx.test.espresso.Espresso.onData
import com.agoda.kakao.screen.Screen
import org.hamcrest.core.IsAnything.anything


object SpinnerItemSelectFragmentScreen : Screen<SpinnerItemSelectFragmentScreen> () {

    fun searchItemAtPosition(position: Int) =
        onData(anything()).atPosition(position)
    }


