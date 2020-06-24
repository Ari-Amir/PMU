package com.aco.pmu.AddNewRecord

import android.view.View
import com.aco.pmu.Helpers.getChildAtPosition
import com.agoda.kakao.common.views.KView
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.switch.KSwitch
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.aco.pmu.R
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import org.hamcrest.Matcher

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

    private val recyclerView = KRecyclerView(
        builder = { withId(R.id.recycler_view) },
        itemTypeBuilder = { itemType(AddNewRecordActivityScreen::RecyclerViewItem) }
    )

    class RecyclerViewItem(val parent: Matcher<View>) : KRecyclerItem<RecyclerViewItem>(parent) {
        val itemDate = KTextView(parent) { withId(R.id.dateTextView) }
        val itemTime = KTextView(parent) { withId(R.id.timeTextView) }
        val itemNameSurname = KTextView(parent) { withId(R.id.firstAndLastNamesTextView) }
        val itemPhoneNumber = KTextView(parent) { withId(R.id.phoneNumberTextView) }
        val itemProcedure = KTextView(parent) { withId(R.id.procedureTextView) }
    }

    fun searchResultAtPosition(position: Int) =
        recyclerView.getChildAtPosition<RecyclerViewItem>(position)
}






//object AddNewRecordActivityScreen : Screen<AddNewRecordActivityScreen>() {
//    val dateSelectTextView = KTextView { withId(R.id.dateSelectTextView) }
//    val timeSelectTextView = KTextView { withId(R.id.timeSelectTextView) }
//    val nameSurnameSelectTextView = KTextView { withId(R.id.nameSurnameSelectTextView) }
//    val procedureSelectTextView = KTextView { withId(R.id.procedureSelectTextView) }
//    val pigmentsSelectTextView1 = KTextView { withId(R.id.pigmentsSelectTextView1) }
//    val spinner1 = KView { withId(R.id.spinner1) }
//    val pigmentsSelectTextView2 = KTextView { withId(R.id.pigmentsSelectTextView2) }
//    val spinner2 = KView { withId(R.id.spinner2) }
//    val pigmentsSelectTextView3 = KTextView { withId(R.id.pigmentsSelectTextView3) }
//    val spinner3 = KView { withId(R.id.spinner3) }
//    val priceEditText = KEditText { withId(R.id.priceEditText) }
//    val commentsEditText = KEditText { withId(R.id.commentsEditText) }
//    val doneSwitch = KSwitch { withId(R.id.doneSwitch) }
//    val buttonDone = KButton { withId(R.id.ic_menu_done_record) }
//    }