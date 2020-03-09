package com.aco.pmu.AddNewRecord

import com.agoda.kakao.common.views.KView
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.switch.KSwitch
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.aco.pmu.R

object AddNewRecordActivityScreen : Screen<AddNewRecordActivityScreen>() {
    val dateSelectTextView = KTextView { withId(R.id.dateSelectTextView) }
    val timeSelectTextView = KTextView { withId(R.id.timeSelectTextView) }
    val nameSurnameSelectTextView = KTextView { withId(R.id.nameSurnameSelectTextView) }
    val procedureSelectTextView = KTextView { withId(R.id.procedureSelectTextView) }
    val pigmentsSelectTextView1 = KTextView { withId(R.id.pigmentsSelectTextView1) }
    val spinner1 = KView { withId(R.id.spinner1) }
    val pigmentsSelectTextView2 = KTextView { withId(R.id.pigmentsSelectTextView2) }
    val spinner2 = KView { withId(R.id.spinner2) }
    val pigmentsSelectTextView3 = KTextView { withId(R.id.pigmentsSelectTextView3) }
    val spinner3 = KView { withId(R.id.spinner3) }
    val priceEditText = KEditText { withId(R.id.priceEditText) }
    val commentsEditText = KEditText { withId(R.id.commentsEditText) }
    val doneSwitch = KSwitch { withId(R.id.doneSwitch) }
    val buttonDone = KButton { withId(R.id.ic_menu_done_record) }
    }