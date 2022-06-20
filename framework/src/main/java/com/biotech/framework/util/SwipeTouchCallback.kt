package com.biotech.framework.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class SwipeTouchCallback @SuppressLint("ClickableViewAccessibility") constructor(
    _recyclerView: RecyclerView,
    _leftButton: Int,
    _rightButton: Int
) : ItemTouchHelper.Callback() {

    private var recyclerView: RecyclerView? = _recyclerView
    private var defaultLeftScrollX = _leftButton
    private var defaultRightScrollX = _rightButton
    private var mCurrentScrollX = 0
    private var mCurrentScrollXWhenInactive = 0
    private var mInitXWhenInactive = 0f
    private var mFirstInactive = false
    private var recoverQueue: Queue<Int>? = null
    private var swipedPos = -1


    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener =
        View.OnTouchListener { v, event ->
        if (swipedPos < 0) return@OnTouchListener false
        if(event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_MOVE){
            recoverQueue!!.add(swipedPos)
            swipedPos = -1
            recoverSwipedItem()
        }
        return@OnTouchListener false
    }

    init {
        this.recyclerView!!.setOnTouchListener(onTouchListener)
        recoverQueue = object : LinkedList<Int>() {
            override fun add(element: Int): Boolean {
                return if (contains(element)) false else super.add(element)
            }

        }
        attachSwipe()
    }

    private fun attachSwipe() {
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    @Synchronized
    fun recoverSwipedItem() {
        //TODO 這段有高機率抱錯 原因有可能作用在Adapter不可視範圍外 導致Null Exception
        try {
            while (!recoverQueue!!.isEmpty()) {
                val pos = recoverQueue!!.poll()
                if (pos > -1) {
                    Log.e("recoverQueue復原", "$pos++++++++++++++++++++++++")
                    Log.e("swipedPos復原", "$swipedPos++++++++++++++++++++++++")
                    Objects.requireNonNull(recyclerView!!.findViewHolderForAdapterPosition(pos))!!.itemView.scrollTo(
                        0,
                        0
                    )
                }
            }
        } catch (e: Exception) {
            Log.w(this.javaClass.toString() + "：", "發生問題，滾動無法復原")
            recoverQueue!!.clear()
            recoverQueue!!.add(0)
        }
    }





    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        // 上下拖动
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        // 向左滑动
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(dragFlags, swipeFlags)
    }



    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return Int.MAX_VALUE.toFloat()
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return Int.MAX_VALUE.toFloat()
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val pos = viewHolder.adapterPosition
        if(dX == 0f){
            mCurrentScrollX = viewHolder.itemView.scrollX
            mFirstInactive = true
        }

        if(isCurrentlyActive){
            viewHolder.itemView.scrollTo((mCurrentScrollX + -dX).toInt(),0)
        }
        else{
            if(mFirstInactive){
                mFirstInactive = false
                mCurrentScrollXWhenInactive = viewHolder.itemView.scrollX
                mInitXWhenInactive = dX


                if(swipedPos != pos){
                    recoverSwipedItem()
                    recoverQueue!!.add(pos)
                }
                swipedPos = pos

                when {
                    viewHolder.itemView.scrollX >= defaultLeftScrollX -> {
                        // 当手指松开时，ItemView的滑动距离大于给定阈值，那么最终就停留在阈值，显示删除按钮。
                        viewHolder.itemView.scrollTo(
                            Math.max(
                                mCurrentScrollX + (-dX).toInt(),
                                defaultLeftScrollX
                            ), 0
                        )
                    }
                    viewHolder.itemView.scrollX <= -defaultRightScrollX -> {
                        viewHolder.itemView.scrollTo(
                            Math.min(
                                -mCurrentScrollX - (-dX).toInt(),
                                -defaultRightScrollX
                            ), 0
                        )
                    }
                    else -> {
                        // 这里只能做距离的比例缩放，因为回到最初位置必须得从当前位置开始，dx不一定与ItemView的滑动距离相等
                        viewHolder.itemView.scrollTo(
                            (mCurrentScrollXWhenInactive * dX / mInitXWhenInactive).toInt(),
                            0
                        )
                    }
                }
            }
        }





        object {
            fun dpToPx(_context: Context, value: Float): Int {
                return TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    value,
                    _context.resources.displayMetrics
                )
                    .toInt()
            }
        }

    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (viewHolder.itemView.scrollX > defaultLeftScrollX) {
            viewHolder.itemView.scrollTo(defaultLeftScrollX, 0)
        } else if (viewHolder.itemView.scrollX < -defaultRightScrollX) {
            viewHolder.itemView.scrollTo(-defaultRightScrollX, 0)
        }
    }
}