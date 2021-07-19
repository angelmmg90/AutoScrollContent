package com.teseo.studios.autoscrollcontent

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.Interpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar
import kotlin.math.abs

private const val SPEED = 40
private var xPushDown = 0f
private var yPushDown = 0f
private var startClickTime: Long = 0

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
     * Sliding speed, default 40
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
    private var canTouch = false

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
     * Start sliding
     */
    fun startAutoScroll() {
        isStopAutoScroll = false
        openAutoScroll(currentSpeed, false)
    }

    /**
     * Start sliding
     *
     * @param speed   Sliding distance (determining the sliding speed)
     * @param reverse Whether to slide backwards
     */
    fun openAutoScroll(speed: Int = SPEED, reverse: Boolean = false) {
        isReverse = reverse
        currentSpeed = speed
        isOpenAuto = true
        notifyLayoutManager()
        startScroll()
    }

    /**
     * Is it possible to manually slide when swiping automatically
     */
    fun setCanTouch(b: Boolean) {
        canTouch = b
    }

    fun setItemClickListener(onItemClicked: (ViewHolder?, Int) -> Unit) {
        itemClickListener = onItemClicked
    }

    fun canTouch(): Boolean {
        return canTouch
    }

    /**
     * Set whether to display the list infinitely
     */
    fun setLoopEnabled(loopEnabled: Boolean) {
        isLoopEnabled = loopEnabled
        if (adapter != null) {
            adapter!!.notifyDataSetChanged()
            startScroll()
        }
    }

    /**
     * Whether to slide infinitely
     */
    fun isLoopEnabled(): Boolean {
        return isLoopEnabled
    }

    /**
     * Set whether to reverse
     */
    fun setReverse(reverse: Boolean) {
        isReverse = reverse
        notifyLayoutManager()
        startScroll()
    }

    /**
     * @param isStopAutoScroll
     */
    fun pauseAutoScroll(isStopAutoScroll: Boolean) {
        this.isStopAutoScroll = isStopAutoScroll
    }

    fun getReverse(): Boolean {
        return isReverse
    }

    /**
     * Start scrolling
     */
    private fun startScroll() {
        if (!isOpenAuto) return
        if (scrollState == SCROLL_STATE_SETTLING) return
        if (inflate && isReady) {
            speedDy = 0
            speedDx = currentSpeed
            smoothScroll()
        }
    }

    private fun smoothScroll() {
        if (!isStopAutoScroll) {
            val absSpeed = abs(currentSpeed)
            val d = if (isReverse) -absSpeed else absSpeed
            smoothScrollBy(d, d, interpolator)
        }
    }

    private fun notifyLayoutManager() {
        val layoutManager = layoutManager
        if (layoutManager is LinearLayoutManager) {
            val linearLayoutManager = layoutManager as LinearLayoutManager?
            linearLayoutManager?.let { lm ->
                lm.reverseLayout = isReverse
            }
        }
    }

    override fun swapAdapter(adapter: Adapter<*>?, removeAndRecycleExistingViews: Boolean) {
        super.swapAdapter(generateAdapter(adapter!!), removeAndRecycleExistingViews)
        isReady = true
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(generateAdapter(adapter!!))
        isReady = true
    }

    /**
     * This method JUST determines whether we want to intercept the motion.
     * If we return true, onTouchEvent will be called and we do the actual
     * scrolling there.
     */
    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        return if (canTouch) {
            when (e.action) {
                MotionEvent.ACTION_DOWN -> {
                    pointTouch = true

                    xPushDown = e.x
                    yPushDown = e.y
                    startClickTime = Calendar.getInstance().timeInMillis;

                    itemRvClicked()
                    continueScroll()
                }
                MotionEvent.ACTION_UP -> {
                    continueScroll()
                    return false
                }
                MotionEvent.ACTION_MOVE -> {
                    return true
                }
            }
            super.onInterceptTouchEvent(e)
            return false
        } else false
    }

    private fun continueScroll() {
        if (isOpenAuto) {
            pointTouch = false
            currentSpeed += 1
            startScroll()
            currentSpeed -= 1
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        return if (canTouch) {
            when (e.action) {
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    continueScroll()
                    return false
                }
            }
            super.onTouchEvent(e)
        } else false
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    private fun itemRvClicked() {
        val clickDuration: Long = Calendar.getInstance().timeInMillis - startClickTime

        if (clickDuration < 200) {
            val viewHolder: ViewHolder?
            val actualPositionItem: Int
            val child = this.findChildViewUnder(xPushDown, yPushDown)
            if (child != null && itemClickListener != null) {
                viewHolder = this.findContainingViewHolder(child)

                viewHolder?.let {
                    actualPositionItem =
                        (this.adapter as NestingRecyclerViewAdapter).getActualPosition(viewHolder.adapterPosition)
                    itemClickListener?.invoke(viewHolder, actualPositionItem)
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        startScroll()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        inflate = true
    }

    override fun onScrolled(dx: Int, dy: Int) {
        if (pointTouch) {
            speedDx = 0
            speedDy = 0
            return
        }
        val vertical: Boolean
        if (dx == 0) { //Vertical scrolling
            speedDy += dy
            vertical = true
        } else { //Horizontal scrolling
            speedDx += dx
            vertical = false
        }
        if (vertical) {
            if (abs(speedDy) >= abs(currentSpeed)) {
                speedDy = 0
                smoothScroll()
            }
        } else {
            if (abs(speedDx) >= abs(currentSpeed)) {
                speedDx = 0
                smoothScroll()
            }
        }
    }

    private fun generateAdapter(adapter: Adapter<*>): NestingRecyclerViewAdapter<out ViewHolder> {
        return NestingRecyclerViewAdapter(this, adapter)
    }

    class NestingRecyclerViewAdapter<VH : ViewHolder>(
        private val autoScrollRecyclerView: AutoScrollContent,
        var adapter: Adapter<VH>
    ) : Adapter<VH>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            return adapter.onCreateViewHolder(parent, viewType)
        }

        override fun registerAdapterDataObserver(observer: AdapterDataObserver) {
            super.registerAdapterDataObserver(observer)
            adapter.registerAdapterDataObserver(observer)
        }

        override fun unregisterAdapterDataObserver(observer: AdapterDataObserver) {
            super.unregisterAdapterDataObserver(observer)
            adapter.unregisterAdapterDataObserver(observer)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            adapter.onBindViewHolder(holder, generatePosition(position))
        }

        override fun setHasStableIds(hasStableIds: Boolean) {
            super.setHasStableIds(hasStableIds)
            adapter.setHasStableIds(hasStableIds)
        }

        override fun getItemCount(): Int {
            //If it is an infinite scroll mode, set an unlimited number of items
            return if (getLoopEnable()) Int.MAX_VALUE else adapter.itemCount
        }

        override fun getItemViewType(position: Int): Int {
            return adapter.getItemViewType(generatePosition(position))
        }

        override fun getItemId(position: Int): Long {
            return adapter.getItemId(generatePosition(position))
        }

        /**
         * Returns the corresponding position according to the current scroll mode
         */
        fun generatePosition(position: Int): Int {
            return if (getLoopEnable()) {
                getActualPosition(position)
            } else {
                position
            }
        }

        /**
         * Returns the actual position of the item
         *
         * @param position The position after starting to scroll will grow indefinitely
         * @return Item actual location
         */
        fun getActualPosition(position: Int): Int {
            val itemCount = adapter.itemCount
            return if (position >= itemCount) position % itemCount else position
        }

        private fun getLoopEnable(): Boolean {
            return autoScrollRecyclerView.isLoopEnabled
        }

        fun getReverse(): Boolean {
            return autoScrollRecyclerView.isReverse
        }
    }

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