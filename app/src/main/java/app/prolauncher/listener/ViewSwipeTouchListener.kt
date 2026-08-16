package app.prolauncher.listener

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewConfiguration
import kotlin.math.abs
import kotlin.math.hypot

internal open class ViewSwipeTouchListener(c: Context?, v: View) : OnTouchListener {
    private var longPressOn = false
    private var dragStarted = false
    private var longPressX = 0F
    private var longPressY = 0F
    private val touchSlop: Int = (c?.let { ViewConfiguration.get(it).scaledTouchSlop } ?: 0)
    private val gestureDetector: GestureDetector

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                view.isPressed = true
                longPressOn = false
                dragStarted = false
            }

            MotionEvent.ACTION_UP -> {
                view.isPressed = false
                if (longPressOn && !dragStarted) {
                    longPressOn = false
                    onLongClick(view)
                }
                dragStarted = false
            }

            MotionEvent.ACTION_CANCEL -> {
                view.isPressed = false
                longPressOn = false
                dragStarted = false
            }

            MotionEvent.ACTION_MOVE -> {
                if (longPressOn && !dragStarted) {
                    val distance = hypot(motionEvent.rawX - longPressX, motionEvent.rawY - longPressY)
                    if (distance > touchSlop) {
                        dragStarted = true
                        onLongPressMove(view)
                    }
                }
            }
        }
        return gestureDetector.onTouchEvent(motionEvent)
    }

    private inner class GestureListener(private val view: View) : SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD: Int = 100
        private val SWIPE_VELOCITY_THRESHOLD: Int = 100

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            onClick(view)
            return super.onSingleTapUp(e)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            onDoubleClick()
            return super.onDoubleTap(e)
        }

        override fun onLongPress(e: MotionEvent) {
            longPressOn = true
            dragStarted = false
            longPressX = e.rawX
            longPressY = e.rawY
            super.onLongPress(e)
        }

        override fun onFling(
            event1: MotionEvent?,
            event2: MotionEvent,
            velocityX: Float,
            velocityY: Float,
        ): Boolean {
            if (dragStarted) return false
            try {
                val diffY = event2.y - (event1?.y ?: 0F)
                val diffX = event2.x - (event1?.x ?: 0F)
                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) onSwipeRight() else onSwipeLeft()
                    }
                } else {
                    if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY < 0) onSwipeUp() else onSwipeDown()
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return false
        }
    }

    open fun onSwipeRight() {}
    open fun onSwipeLeft() {}
    open fun onSwipeUp() {}
    open fun onSwipeDown() {}
    open fun onLongClick(view: View) {}
    open fun onLongPressMove(view: View) {}
    private fun onDoubleClick() {}
    open fun onClick(view: View) {}

    init {
        gestureDetector = GestureDetector(c, GestureListener(v))
    }
}