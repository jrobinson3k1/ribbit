package com.rebbit.app.ui.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup

class DragRecyclerView : RecyclerView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        if( getDragViewChildren().firstOrNull { it.dragging } != null)
            return false
        return super.onInterceptTouchEvent(e)
    }

    private fun ViewGroup.getDragViewChildren(): List<DragLayout> {
        val dragLayouts = mutableListOf<DragLayout>()
        for (i in 0..childCount) {
            val child = getChildAt(i)
            if (child is DragLayout) dragLayouts.add(child)
            else if (child is ViewGroup) dragLayouts.addAll(child.getDragViewChildren())
        }

        return dragLayouts
    }
}