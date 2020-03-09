package com.aco.pmu.AddNewPigment

import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.aco.pmu.R

object AddNewPigmentActivityScreen : Screen<AddNewPigmentActivityScreen> () {
    val pigmentNameEditText = KEditText { withId(R.id.pigmentsNameEditText) }
    val buttonDone = KButton { withId(R.id.ic_menu_done_pigment) }

}