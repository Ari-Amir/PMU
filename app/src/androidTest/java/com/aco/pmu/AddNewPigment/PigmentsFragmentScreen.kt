package com.aco.pmu.AddNewPigment

import android.view.View
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.aco.pmu.R
import com.aco.pmu.Helpers.getChildAtPosition
import org.hamcrest.Matcher

object PigmentsFragmentScreen : Screen<PigmentsFragmentScreen> () {

    val addNewPigmentButton = KButton { withId(R.id.floatingActionButton) }
    private val recyclerView = KRecyclerView(
        builder = { withId(R.id.recycler_view) },
        itemTypeBuilder = { itemType(PigmentsFragmentScreen::RecyclerViewItem) }
    )

    class RecyclerViewItem(val parent: Matcher<View>) : KRecyclerItem<RecyclerViewItem>(parent) {
        val itemPigment = KTextView(parent) { withId(R.id.pigmentNameTextView) }
    }

    fun searchResultAtPosition(position: Int) =
        recyclerView.getChildAtPosition<RecyclerViewItem>(position)

}
