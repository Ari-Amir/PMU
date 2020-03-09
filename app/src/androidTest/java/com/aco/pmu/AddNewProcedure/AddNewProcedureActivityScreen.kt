package com.aco.pmu.AddNewProcedure

import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.aco.pmu.R

object AddNewProcedureActivityScreen : Screen<AddNewProcedureActivityScreen> () {
    val procedureNameEditText = KEditText { withId(R.id.procedureNameEditText) }
    val buttonDone = KButton { withId(R.id.ic_menu_done_procedure) }

}