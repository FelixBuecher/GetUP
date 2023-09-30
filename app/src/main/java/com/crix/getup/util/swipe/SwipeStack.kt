package com.crix.getup.util.swipe

import android.content.Context
import android.database.DataSetObserver
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.crix.getup.R
import com.crix.getup.databinding.CardItemBinding
import java.util.Random
import kotlin.math.pow

class SwipeStack @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private var _topView: View? = null
    private var _adapter: Adapter? = null
    private var _random: Random? = null
    private var _animationDuration = 0
    private var _currentViewIndex = 0
    private var _numberOfStackedViews = 0
    private var _viewSpacing = 0
    private var _viewRotation = 0
    private var _limitAmount = 0
    private var _swipeRotation = 0f
    private var _swipeOpacity = 0f
    private var _scaleFactor = 0f
    private var _disableHwAcceleration = false
    private var _isFirstLayout = true
    private var _swipeHelper: SwipeHelper? = null
    private var _dataObserver: DataSetObserver? = null
    private var _listener: SwipeStackListener? = null
    private var _progressListener: SwipeProgressListener? = null


    val currentPosition: Int
        get() = _currentViewIndex - childCount

    var adapter: Adapter?
        get() = _adapter
        set(adapter) {
            if (_adapter != null) _adapter!!.unregisterDataSetObserver(_dataObserver)
            _adapter = adapter
            _adapter!!.registerDataSetObserver(_dataObserver)
        }

    interface SwipeStackListener {
        fun onViewSwipedLeft(position: Int)

        fun onViewSwipedRight(position: Int)

        fun onStackEmpty()

        fun onStackNearEmpty()
    }

    interface SwipeProgressListener {
        fun onSwipeStart(position: Int)

        fun onSwipeProgress(position: Int, progress: Float)

        fun onSwipeEnd(position: Int)
    }

    init {
        readAttributes(attrs)
        _random = Random()
        clipToPadding = false
        clipChildren = false
        _swipeHelper = SwipeHelper(this)
        _swipeHelper!!.setAnimationDuration(_animationDuration)
        _swipeHelper!!.setRotation(_swipeRotation)
        _swipeHelper!!.setOpacity(_swipeOpacity)
        _dataObserver = object: DataSetObserver() {
            override fun onChanged() {
                super.onChanged()
                invalidate()
                requestLayout()
            }
        }
    }

    companion object {
        const val DEFAULT_ANIMATION_DURATION = 300
        const val DEFAULT_STACK_SIZE = 3
        const val DEFAULT_STACK_ROTATION = 8
        const val DEFAULT_SWIPE_ROTATION = 30f
        const val DEFAULT_SCALE_FACTOR = 1f
        const val DEFAULT_SWIPE_OPACITY = 1f
        const val DEFAULT_DISABLE_HW_ACCELERATION = true
        const val DEFAULT_LIMIT_AMOUNT = 2
        private const val KEY_SUPER_STATE = "superState"
        private const val KEY_CURRENT_INDEX = "currentIndex"
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState())
        bundle.putInt(KEY_CURRENT_INDEX, _currentViewIndex - childCount)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var state: Parcelable? = state
        if (state is Bundle) {
            _currentViewIndex = state.getInt(KEY_CURRENT_INDEX)
            state = state.getParcelable(KEY_SUPER_STATE, Parcelable::class.java)
        }
        super.onRestoreInstanceState(state)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (_adapter == null || _adapter!!.isEmpty) {
            _currentViewIndex = 0
            removeAllViewsInLayout()
            return
        }
        var x = childCount
        while (x < _numberOfStackedViews && _currentViewIndex < _adapter!!.count) {
            addNextView()
            x++
        }
        reorderItems()
        _isFirstLayout = false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }


    fun onSwipeStart() {
        if (_progressListener != null) _progressListener!!.onSwipeStart(currentPosition)
    }

    fun onSwipeProgress(progress: Float) {
        if (_progressListener != null) _progressListener!!.onSwipeProgress(
            currentPosition,
            progress
        )
    }

    fun onSwipeEnd() {
        if (_progressListener != null) _progressListener!!.onSwipeEnd(currentPosition)
    }

    fun onViewSwipedLeft() {
        if (_listener != null) _listener!!.onViewSwipedLeft(currentPosition)
        removeTopView()
    }

    fun onViewSwipedRight() {
        if (_listener != null) _listener!!.onViewSwipedRight(currentPosition)
        removeTopView()
    }

    fun setListener(listener: SwipeStackListener?) {
        _listener = listener
    }

    fun setSwipeProgressListener(listener: SwipeProgressListener?) {
        _progressListener = listener
    }

    fun swipeTopViewToRight() {
        if (childCount == 0) return
        _swipeHelper!!.swipeViewToRight()
    }

    fun swipeTopViewToLeft() {
        if (childCount == 0) return
        _swipeHelper!!.swipeViewToLeft()
    }

    fun resetStack() {
        _currentViewIndex = 0
        removeAllViewsInLayout()
        requestLayout()
    }

    private fun readAttributes(attributeSet: AttributeSet?) {
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.SwipeStack)
        try {
            _animationDuration = attrs.getInt(
                R.styleable.SwipeStack_animation_duration,
                DEFAULT_ANIMATION_DURATION
            )
            _numberOfStackedViews = attrs.getInt(
                R.styleable.SwipeStack_stack_size,
                DEFAULT_STACK_SIZE
            )
            _limitAmount = attrs.getInt(
                R.styleable.SwipeStack_limit_amount,
                DEFAULT_LIMIT_AMOUNT
            )
            _viewSpacing = attrs.getDimensionPixelSize(
                R.styleable.SwipeStack_stack_spacing,
                resources.getDimensionPixelSize(R.dimen.default_stack_spacing)
            )
            _viewRotation = attrs.getInt(
                R.styleable.SwipeStack_stack_rotation,
                DEFAULT_STACK_ROTATION
            )
            _swipeRotation = attrs.getFloat(
                R.styleable.SwipeStack_swipe_rotation,
                DEFAULT_SWIPE_ROTATION
            )
            _swipeOpacity = attrs.getFloat(
                R.styleable.SwipeStack_swipe_opacity,
                DEFAULT_SWIPE_OPACITY
            )
            _scaleFactor = attrs.getFloat(
                R.styleable.SwipeStack_scale_factor,
                DEFAULT_SCALE_FACTOR
            )
            _disableHwAcceleration = attrs.getBoolean(
                R.styleable.SwipeStack_disable_hw_acceleration,
                DEFAULT_DISABLE_HW_ACCELERATION
            )
        } finally {
            attrs.recycle()
        }
    }

    private fun addNextView() {
        if (_currentViewIndex < _adapter!!.count) {
            val bottomView = _adapter!!.getView(_currentViewIndex, null, this)
            bottomView.setTag(R.id.new_view, true)
            if (!_disableHwAcceleration) {
                bottomView.setLayerType(LAYER_TYPE_HARDWARE, null)
            }
            if (_viewRotation > 0) {
                bottomView.rotation = (_random!!.nextInt(_viewRotation) - _viewRotation / 2).toFloat()
            }
            val width = width - paddingLeft - paddingRight
            val height = height - paddingTop - paddingBottom
            var params = bottomView.layoutParams
            if (params == null) {
                params = LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
            }
            var specWidth = MeasureSpec.AT_MOST
            var specHeight = MeasureSpec.AT_MOST
            if (params.width == LayoutParams.MATCH_PARENT) {
                specWidth = MeasureSpec.EXACTLY
            }
            if (params.height == LayoutParams.MATCH_PARENT) {
                specHeight = MeasureSpec.EXACTLY
            }
            bottomView.measure(
                specWidth or width,
                specHeight or height
            )
            addViewInLayout(bottomView, 0, params, true)
            _currentViewIndex++
        }
    }

    private fun reorderItems() {
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            val topViewIndex = childCount - 1
            val distanceToViewAbove = topViewIndex * _viewSpacing - i * _viewSpacing
            val newPosX = (width - childView.measuredWidth) / 2
            val newPosY = distanceToViewAbove + paddingTop
            childView.layout(
                newPosX,
                paddingTop,
                newPosX + childView.measuredWidth,
                paddingTop + childView.measuredHeight
            )
            childView.translationZ = i.toFloat()
            val isNewView = childView.getTag(R.id.new_view) as Boolean
            val sf = _scaleFactor.toDouble().pow((childCount - i).toDouble()).toFloat()
            if (i == topViewIndex) {
                _swipeHelper!!.unregisterObservedView()
                _topView = childView
                _swipeHelper!!.registerObservedView(
                    _topView,
                    newPosX.toFloat(),
                    newPosY.toFloat()
                )
            }
            if (!_isFirstLayout) {
                if(isNewView) {
                    childView.alpha = 0f
                    childView.setTag(R.id.new_view, false)
                    childView.y = newPosY.toFloat()
                    childView.scaleX = sf
                    childView.scaleY = sf
                }
                childView.animate()
                    .y(newPosY.toFloat())
                    .scaleX(sf)
                    .scaleY(sf)
                    .alpha(1f).duration = _animationDuration.toLong()
            } else {
                childView.setTag(R.id.new_view, false)
                childView.y = newPosY.toFloat()
                childView.scaleX = sf
                childView.scaleY = sf
            }
        }
    }

    private fun removeTopView() {
        if (_topView != null) {
            removeView(_topView)
            _topView = null
        }
        if (childCount == 0) {
            if (_listener != null) _listener!!.onStackEmpty()
        }
        if (childCount <= _limitAmount) {
            if (_listener != null) _listener!!.onStackNearEmpty()
        }
    }
}