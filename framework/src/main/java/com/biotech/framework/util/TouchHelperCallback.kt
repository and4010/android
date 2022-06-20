package com.biotech.framework.util

import android.graphics.Canvas
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper


class TouchHelperCallback(val ItemCallback: ItemTouchHelperCallback) : ItemTouchHelper.Callback() {

    private val TAG = "TouchHelperCallback"

    var itemTouchHelper: ItemTouchHelperCallback? = null

    fun touchHelperCallback(itemTouchHelperCallback: ItemTouchHelperCallback){
        this.itemTouchHelper = itemTouchHelperCallback
    }

    interface ItemTouchHelperCallback{
        fun onItemDelete(Position: Int)
        fun onItemMove(FromPosition:Int,ToPosition:Int)
    }


    override fun getMovementFlags(p0: RecyclerView, p1: RecyclerView.ViewHolder): Int {
        //START 右向左 END左向右 LEFT 向左 RIGHT向右 UP向上
        //如果某個值傳0，表示不觸發該操作，次數設定支援上下拖曳，支援向右滑動
        val layoutManager = p0.layoutManager
        var dragFlag = 0
        var swipeFlag = 0

        if (layoutManager is GridLayoutManager) {
            dragFlag = ItemTouchHelper.DOWN or ItemTouchHelper.UP or ItemTouchHelper.START or ItemTouchHelper.END
        }else if (layoutManager is LinearLayoutManager){
            dragFlag = ItemTouchHelper.DOWN or ItemTouchHelper.UP  //設定上下方向為拖曳
            swipeFlag = ItemTouchHelper.END //設定左右方向滑動刪除
        }
        return makeMovementFlags(dragFlag,swipeFlag)
    }

    /**
     * 拖曳切換Item的回傳
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     *   如果Item切換了位置，返回true；反之，返回false
     */

    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
        ItemCallback.onItemMove(p1.adapterPosition,p2.adapterPosition)
        return true
    }
    /**
     * 滑動Item
     *
     * @param viewHolder
     * @param direction
     *   Item滑動的方向
     */
    override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
        ItemCallback.onItemDelete(p0.adapterPosition)
    }

    override fun onChildDraw(c:Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dx:Float, dy:Float, actionState:Int, isCurrentlyActive:Boolean){
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            //滑動時更改Item的透明度
            val alpha = 1 - Math.abs(dx) / viewHolder.itemView.width.toFloat()
            viewHolder.itemView.alpha = alpha
            viewHolder.itemView.translationX = dx
        }else{
            super.onChildDraw(c,recyclerView, viewHolder,dx,dy,actionState,isCurrentlyActive)
        }

    }

}