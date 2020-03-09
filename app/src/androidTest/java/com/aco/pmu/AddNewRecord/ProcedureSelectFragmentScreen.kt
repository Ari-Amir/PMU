package com.aco.pmu.AddNewRecord

import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.aco.pmu.Helpers.getChildAtPosition
import com.aco.pmu.R


object ProcedureSelectFragmentScreen : Screen<ProcedureSelectFragmentScreen> () {

    private val recyclerView = KRecyclerView(
        builder = { withId(R.id.recycler_view) },
        itemTypeBuilder = {}
    )

    fun searchResultAtPosition(position: Int) =
        recyclerView.getChildAtPosition<KRecyclerItem<Int>>(position)
}