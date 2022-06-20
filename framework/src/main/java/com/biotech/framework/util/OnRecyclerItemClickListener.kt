package com.biotech.framework.util

import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent

abstract class OnRecyclerItemClickListener(private val recyclerView: RecyclerView) :
    RecyclerView.OnItemTouchListener {
    private val SingleTapUp: GestureDetectorCompat//點擊探測
    private val OnLongClick: GestureDetectorCompat//長壓探測

    init {
        SingleTapUp = GestureDetectorCompat(
            recyclerView.context,
            ItemClickTouchHelperGestureListener()
        )
        OnLongClick = GestureDetectorCompat(
            recyclerView.context,ItemTouchHelperGestureListener()
        )
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        SingleTapUp.onTouchEvent(e)
        OnLongClick.onTouchEvent(e)
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        SingleTapUp.onTouchEvent(e)
        OnLongClick.onTouchEvent(e)
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    abstract fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int)

    abstract fun onLongClick(viewHolder: RecyclerView.ViewHolder, position: Int)

    //TODO 長壓螢幕超過一定時間觸發
    private inner class ItemTouchHelperGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onLongPress(e: MotionEvent) {
            val childViewUnder = recyclerView.findChildViewUnder(e.x, e.y)
            if (childViewUnder != null) {
                val childViewHolder = recyclerView.getChildViewHolder(childViewUnder)
                val position = recyclerView.getChildAdapterPosition(childViewUnder)
                onLongClick(childViewHolder,position)
            }
            return super.onLongPress(e)
        }
    }
    //TODO 點擊觸發
    private inner class ItemClickTouchHelperGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val childViewunder = recyclerView.findChildViewUnder(e.x,e.y)
            if (childViewunder != null){
                val vh = recyclerView.getChildViewHolder(childViewunder)
                val position = recyclerView.getChildAdapterPosition(childViewunder)
                onItemClick(vh,position)

            }

            return super.onSingleTapUp(e)
        }
    }
}
