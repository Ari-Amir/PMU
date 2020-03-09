package com.aco.pmu.AddNewClient

import android.view.View
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.aco.pmu.R
import com.aco.pmu.Helpers.getChildAtPosition
import org.hamcrest.Matcher

object ClientsFragmentScreen : Screen<ClientsFragmentScreen> () {

    val addNewClientButton = KButton { withId(R.id.floatingActionButton) }
    private val recyclerView = KRecyclerView(
        builder = { withId(R.id.recycler_view) },
        itemTypeBuilder = { itemType(ClientsFragmentScreen::RecyclerViewItem) }
    )

    class RecyclerViewItem(val parent: Matcher<View>) : KRecyclerItem<RecyclerViewItem>(parent) {
        val itemNameSurname = KTextView(parent) { withId(R.id.text_view_firstAndLastName) }
    }

    fun searchResultAtPosition(position: Int) =
        recyclerView.getChildAtPosition<RecyclerViewItem>(position)

}
