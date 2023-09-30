package com.crix.getup.util.swipe

import android.animation.Animator
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.animation.OvershootInterpolator
import com.crix.getup.util.util.AnimationUtils
import kotlin.math.abs
import kotlin.math.max

class SwipeHelper(private val swipeStack: SwipeStack) : OnTouchListener {
    private var observedView: View? = null
    private var listenForTouchEvents = false
    private var downX = 0f
    private var downY = 0f
    private var initialX = 0f
    private var initialY = 0f
    private var pointerID = 0
    private var rotateDegree = SwipeStack.DEFAULT_SWIPE_ROTATION
    private var opacityEnd = SwipeStack.DEFAULT_SWIPE_OPACITY
    private var animationDuration = SwipeStack.DEFAULT_ANIMATION_DURATION

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!listenForTouchEvents || !swipeStack.isEnabled) return false
                v.parent.requestDisallowInterceptTouchEvent(true)
                swipeStack.onSwipeStart()
                pointerID = event.getPointerId(0)
                downX = event.getX(pointerID)
                downY = event.getY(pointerID)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val pointerIndex = event.findPointerIndex(pointerID)
                if (pointerIndex < 0) return false
                val dx = event.getX(pointerIndex) - downX
                val dy = event.getY(pointerIndex) - downY
                observedView!!.x += dx
                observedView!!.y += dy
                val dragDistanceX = observedView!!.x - initialX
                val swipeProgress = max(
                    dragDistanceX / swipeStack.width, -1f
                ).coerceAtMost(1f)
                swipeStack.onSwipeProgress(swipeProgress)
                if (rotateDegree > 0) {
                    observedView!!.rotation = rotateDegree * swipeProgress
                }
                if (opacityEnd < 1f) {
                    observedView!!.alpha = abs(swipeProgress * 2).coerceAtMost(1f)
                }
                return true
            }

            MotionEvent.ACTION_UP -> {
                v.parent.requestDisallowInterceptTouchEvent(false)
                swipeStack.onSwipeEnd()
                checkViewPosition()
                return true
            }
        }
        return false
    }

    private fun checkViewPosition() {
        if (!swipeStack.isEnabled) {
            resetViewPosition()
            return
        }
        val viewCenterHorizontal = observedView!!.x + observedView!!.width / 2
        val parentFirstThird = swipeStack.width / 3f
        val parentLastThird = parentFirstThird * 2
        if (viewCenterHorizontal < parentFirstThird) {
            swipeViewToLeft(animationDuration / 2)
        } else if (viewCenterHorizontal > parentLastThird) {
            swipeViewToRight(animationDuration / 2)
        } else {
            resetViewPosition()
        }
    }

    private fun resetViewPosition() {
        observedView!!.animate()
            .x(initialX)
            .y(initialY)
            .rotation(0f)
            .alpha(1f)
            .setDuration(animationDuration.toLong())
            .setInterpolator(OvershootInterpolator(1.4f))
            .setListener(null)
    }

    private fun swipeViewToLeft(duration: Int) {
        if (!listenForTouchEvents) return
        listenForTouchEvents = false
        observedView!!.animate().cancel()
        observedView!!.animate()
            .x(-swipeStack.width + observedView!!.x)
            .rotation(-rotateDegree)
            .alpha(0f)
            .setDuration(duration.toLong())
            .setListener(object : AnimationUtils.AnimationEndListener() {
                override fun onAnimationEnd(animation: Animator) {
                    swipeStack.onViewSwipedLeft()
                }
            })
    }

    private fun swipeViewToRight(duration: Int) {
        if (!listenForTouchEvents) return
        listenForTouchEvents = false
        observedView!!.animate().cancel()
        observedView!!.animate()
            .x(swipeStack.width + observedView!!.x)
            .rotation(rotateDegree)
            .alpha(0f)
            .setDuration(duration.toLong())
            .setListener(object : AnimationUtils.AnimationEndListener() {
                override fun onAnimationEnd(animation: Animator) {
                    swipeStack.onViewSwipedRight()
                }

            })
    }

    fun registerObservedView(view: View?, initialX: Float, initialY: Float) {
        if (view == null) return
        observedView = view
        observedView!!.setOnTouchListener(this)
        this.initialX = initialX
        this.initialY = initialY
        listenForTouchEvents = true
    }

    fun unregisterObservedView() {
        if(observedView != null) {
            observedView!!.setOnTouchListener(null)
        }
        observedView = null
        listenForTouchEvents = false
    }

    fun setAnimationDuration(duration: Int) {
        animationDuration = duration
    }

    fun setRotation(rotation: Float) {
        rotateDegree = rotation
    }

    fun setOpacity(opacity: Float) {
        opacityEnd = opacity
    }

    fun swipeViewToLeft() {
        swipeViewToLeft(animationDuration)
    }

    fun swipeViewToRight() {
        swipeViewToRight(animationDuration)
    }
}