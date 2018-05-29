package com.rebbit.app.ui.view

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class DragLayout : FrameLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val dragHelper: ViewDragHelper = ViewDragHelper.create(this, 1F, DragCallback()).apply { minVelocity = 300F }
    private var horizontalRange = 0

    var dragging = false
        private set
    var opened = false
        private set

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        horizontalRange = -(w * 0.2).toInt()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (dragHelper.shouldInterceptTouchEvent(ev)) true
        else super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) ViewCompat.postInvalidateOnAnimation(this)
    }

    private inner class DragCallback : ViewDragHelper.Callback() {
        private var draggingState: Int = ViewDragHelper.STATE_IDLE

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child.parent == this@DragLayout
        }

        override fun onViewDragStateChanged(state: Int) {
            if (state == draggingState) return

            if ((draggingState == ViewDragHelper.STATE_DRAGGING || draggingState == ViewDragHelper.STATE_SETTLING)
                    && state == ViewDragHelper.STATE_IDLE) {
                onDragStarted()
            } else if (state == ViewDragHelper.STATE_DRAGGING) {
                onDragEnded()
            }

            draggingState = state
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return horizontalRange
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return max(min(0, left), horizontalRange)
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val invalidate = when {
                xvel < 0 -> {
                    opened = true
                    dragHelper.settleCapturedViewAt(horizontalRange, 0)
                }
                xvel > 0 -> {
                    opened = false
                    dragHelper.settleCapturedViewAt(0, 0)
                }
                else -> {
                    val left =
                            if (abs(horizontalRange - releasedChild.left) < abs(releasedChild.left)) {
                                opened = true
                                horizontalRange
                            } else {
                                opened = false
                                0
                            }
                    dragHelper.settleCapturedViewAt(left, 0)
                }
            }

            if (invalidate) {
                ViewCompat.postInvalidateOnAnimation(this@DragLayout)
            }
        }

        private fun onDragStarted() {
            dragging = true
        }

        private fun onDragEnded() {
            dragging = false
        }
    }
}