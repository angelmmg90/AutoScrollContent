package com.teseo.studios.autoscrollcontent

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
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