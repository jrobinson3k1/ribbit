package com.rebbit.app.ui.view

import android.animation.ObjectAnimator
import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
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

    var dragDist: Float = 0F
    var prevX: Float = -1F

    private lateinit var dragView: View
    private lateinit var revealView: View

    override fun onFinishInflate() {
        super.onFinishInflate()
        dragView = getChildAt(1)
        revealView = getChildAt(0)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        horizontalRange = -revealView.measuredWidth + (dragView.layoutParams as FrameLayout.LayoutParams).marginEnd
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        dragHelper.processTouchEvent(ev)
        accumulateDragDist(ev)

        val couldBecomeClick = couldBecomeClick(ev)
        val settling = dragHelper.viewDragState == ViewDragHelper.STATE_SETTLING

        prevX = ev.x

        return !couldBecomeClick && settling
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) ViewCompat.postInvalidateOnAnimation(this)
    }

    private fun couldBecomeClick(ev: MotionEvent): Boolean {
        return isInMainView(ev) && !shouldInitiateADrag()
    }

    private fun isInMainView(ev: MotionEvent): Boolean {
        val x = ev.x
        val y = ev.y

        val withinVertical = dragView.top <= y && y <= dragView.bottom
        val withinHorizontal = dragView.left <= x && x <= dragView.right

        return withinVertical && withinHorizontal
    }

    private fun shouldInitiateADrag(): Boolean {
        val minDistToInitiateDrag = dragHelper.touchSlop.toFloat()
        return dragDist >= minDistToInitiateDrag
    }

    private fun accumulateDragDist(ev: MotionEvent) {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            dragDist = 0f
            return
        }

        val dragged = Math.abs(ev.x - prevX)

        dragDist += dragged
    }

    fun close(delay: Long = 0) {
        opened = false

        val child = getChildAt(childCount - 1)
        if (child.x != 0F) {
            val animatorProperties = fun ObjectAnimator.() {
                duration = 350
                interpolator = AccelerateDecelerateInterpolator()
                startDelay = delay
            }
            ObjectAnimator.ofInt(child, "left", 0).apply(animatorProperties).start()
            ObjectAnimator.ofInt(child, "right", child.width).apply(animatorProperties).start()
        }
    }

    private inner class DragCallback : ViewDragHelper.Callback() {
        private var draggingState: Int = ViewDragHelper.STATE_IDLE

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child == dragView
        }

        override fun onViewDragStateChanged(state: Int) {
            if (state == draggingState) return

            if ((draggingState == ViewDragHelper.STATE_DRAGGING || draggingState == ViewDragHelper.STATE_SETTLING)
                    && state == ViewDragHelper.STATE_IDLE) {
                onDragEnded()
            } else if (state == ViewDragHelper.STATE_DRAGGING) {
                onDragStarted()
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
            val finalLeft: Int = when {
                xvel < 0 -> {
                    opened = true
                    horizontalRange
                }
                xvel > 0 -> {
                    opened = false
                    0
                }
                else -> {
                    if (abs(horizontalRange - releasedChild.left) < abs(releasedChild.left)) {
                        opened = true
                        horizontalRange
                    } else {
                        opened = false
                        0
                    }
                }
            }

            if (dragHelper.settleCapturedViewAt(finalLeft, 0)) {
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