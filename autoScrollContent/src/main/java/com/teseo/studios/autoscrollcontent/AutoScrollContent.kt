package com.teseo.studios.autoscrollcontent

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Interpolator
import androidx.recyclerview.widget.RecyclerView

private const val SPEED = 40

class AutoScrollContent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    /**
     * Sliding estimator
     */
    private var interpolator: UniformSpeedInterpolator = UniformSpeedInterpolator()

    /**
     * Dx and dy between units
     */
    private var speedDx = 0

    /**
     * Dx and dy between units
     */
    private var speedDy: Int = 0

    /**
     * Sliding speed, default 100
     */
    private var currentSpeed = SPEED

    /**
     * Whether to display the list infinitely
     */
    private var isLoopEnabled = false

    /**
     * Whether to slide backwards
     */
    private var isReverse = false

    /**
     * Whether to turn on automatic sliding
     */
    private var isOpenAuto = false

    /**
     * Whether the user can manually slide the screen
     */
    private var canTouch = true

    /**
     * Whether the user clicks on the screen
     */
    private var pointTouch = false

    /**
     * Are you ready for data?
     */
    private var isReady = false

    /**
     * Whether initialization is complete
     */
    private var inflate = false

    /**
     * Whether to stop scroll
     */
    private var isStopAutoScroll = false

    private var itemClickListener: ((ViewHolder?, Int) -> Unit)? = null

    /**
     * Custom estimator
     * Swipe the list at a constant speed
     */
    private class UniformSpeedInterpolator : Interpolator {

        override fun getInterpolation(input: Float): Float {
            return input
        }
    }
}