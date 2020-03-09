package com.aco.pmu.Helpers

import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView

inline fun <reified T : KRecyclerItem<*>> KRecyclerView.getChildAtPosition(position: Int): T {
    var child: T? = null

    this.childAt<T>(position) { child = this }

    return child!!
}
