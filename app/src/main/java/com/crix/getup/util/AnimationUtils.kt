package com.crix.getup.util

import android.animation.Animator

class AnimationUtils {
    abstract class AnimationEndListener: Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {

        }

        override fun onAnimationCancel(animation: Animator) {

        }

        override fun onAnimationRepeat(animation: Animator) {

        }
    }
}