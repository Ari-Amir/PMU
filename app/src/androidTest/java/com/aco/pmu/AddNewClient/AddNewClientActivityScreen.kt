package com.aco.pmu.AddNewClient

import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.aco.pmu.R

object AddNewClientActivityScreen : Screen<AddNewClientActivityScreen> () {
    val clientsNameEditText = KEditText { withId(R.id.nameEditText) }
    val clientsSurnameNameEditText = KEditText { withId(R.id.surnameEditText) }
    val clientsPhoneNumber1EditText = KEditText { withId(R.id.phoneNumberEditText1) }
    val clientsPhoneNumber2EditText = KEditText { withId(R.id.phoneNumberEditText2) }
    val clientsCommentsEditText = KEditText { withId(R.id.clientRemarkEditText) }
    val buttonDone = KButton { withId(R.id.ic_menu_done_client) }

}