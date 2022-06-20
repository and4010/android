package com.biotech.framework.util

import android.content.Context
import android.graphics.*
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.HashMap

abstract class SwipeHelper(dragDirs: Int, swipeDirs: Int) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    companion object {
        const val BUTTON_WIDTH = 200
    }

    private var recyclerView: RecyclerView? = null
    private var buttons : MutableList<UnderlayButton> = arrayListOf()
    private var gestureDetector : GestureDetector? = null
    private var swipedPos = -1
    private var swipeThreshold = 0.5f
    private var buttonsBuffer : MutableMap<Int, MutableList<UnderlayButton>>? = null
    private var recoverQueue : Queue<Int>? = null

    private val gestureListener : SimpleOnGestureListener = object :SimpleOnGestureListener(){
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            for (i in buttons){
                if(i.onClick(e!!.x, e.y)) break
            }
            return true
        }

    }

    private val onTouchListener : View.OnTouchListener = object :View.OnTouchListener{
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if(swipeDirs < 0) return false
            val point = event?.let { Point(it.rawX.toInt(), event.rawY.toInt()) }
            val swipedViewHolder = recyclerView?.findViewHolderForAdapterPosition(swipedPos)
            val swipedItem = swipedViewHolder?.itemView
            val rect = Rect()
            swipedItem?.getGlobalVisibleRect(rect)
            if(event!!.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_MOVE){
                if(rect.top < point!!.y && rect.bottom > point.y)
                    gestureDetector!!.onTouchEvent(event)
                else
                    recoverQueue!!.add(swipedPos)
                swipedPos = -1
                recoverSwipedItem()
            }
            return false
        }

    }

    constructor(context: Context, recyclerView: RecyclerView) : this(0, ItemTouchHelper.LEFT){
        this.recyclerView = recyclerView
        this.gestureDetector = GestureDetector(context, gestureListener)
        this.recyclerView!!.setOnTouchListener(onTouchListener)
        buttonsBuffer = HashMap()
        recoverQueue = object :LinkedList<Int>(){
            override fun add(element: Int): Boolean {
                return if(contains(element)) false else super.add(element)
            }
        }
        attachSwipe()
    }

   private fun drawButtons(c: Canvas, itemView : View ,  buffer : MutableList<UnderlayButton>, pos :Int ,  dx :Float){
       var right = itemView.right.toFloat()
       val dButtonWidth = (-1) * dx / buffer.size

       for(i in buffer){
           val left = right - dButtonWidth
           i.onDraw(
               c, RectF(left, itemView.top.toFloat(), right.toFloat(), itemView.bottom.toFloat()),pos
           )
           right = left -right
       }
   }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
      return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        if(swipedPos != pos)
            recoverQueue!!.add(swipedPos)
        swipedPos = pos

        if(buttonsBuffer!!.containsKey(swipedPos))
            buttons = buttonsBuffer!![swipedPos]!!
        else
            buttons.clear()

        buttonsBuffer!!.clear()
        swipeThreshold = 0.5f * buttons.size * SwipeHelper.BUTTON_WIDTH
        recoverSwipedItem()

    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return  0.1f * defaultValue;
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 5.0f * defaultValue;
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
        var translationX = dX
        val itemView = viewHolder.itemView
        if(pos < 0){
            swipedPos = pos
            return
        }

        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            if(dX < 0) {
                var buffer : MutableList<UnderlayButton> = arrayListOf()
                if(!buttonsBuffer!!.containsKey(pos)){
                    instantiateUnderlayButton(viewHolder, buffer)
                    buttonsBuffer!![pos] = buffer
                }
                else
                {
                    buffer = buttonsBuffer!![pos]!!
                }
                translationX = dX * buffer.size * BUTTON_WIDTH / itemView.width
                drawButtons(c, itemView, buffer, pos, translationX)
            }
        }



        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    class UnderlayButton(
        val text: String?,
        val imageResId: Int?,
        val color: Int?,
        private val clickListener: UnderlayButtonClickListener
    ){
      private var pos :Int = 0
      private var clickRegion : RectF? = null


        fun onClick(x: Float, y: Float):Boolean{
            if(clickRegion != null && clickRegion!!.contains(x, y)){
                clickListener.onClick(pos)
                return true
            }
            return false
        }

        fun onDraw(c: Canvas, rect: RectF, pos: Int){
            val p = Paint()

            //Draw background
            p.color = color!!
            c.drawRect(rect, p)

            //draw Text
            p.color = Color.WHITE
//            p.textSize = 12.0f

            val r = Rect()
            val cHeight = rect.height()
            val cWidth = rect.width()

            p.textAlign = Paint.Align.LEFT
            p.textSize = 30.0f
            p.getTextBounds(text, 0, text!!.length, r)

            val x = cWidth / 2f - r.width() / 2f -r.left
            val y = cHeight / 2f + r.height() / 2f - r.bottom;
            c.drawText(text, rect.left + x, rect.top + y, p)

            clickRegion = rect
            this.pos = pos

        }

    }


    @Synchronized
    fun recoverSwipedItem(){
        while (!recoverQueue.isNullOrEmpty()){
            val pos = recoverQueue!!.poll()
            if(pos > -1)
                recyclerView!!.adapter!!.notifyItemChanged(pos);
        }
    }

    fun attachSwipe(){
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    abstract fun instantiateUnderlayButton(
        viewHolder: RecyclerView.ViewHolder,
        underlayButtons: MutableList<UnderlayButton>
    )



    interface UnderlayButtonClickListener {
        fun onClick(pos: Int)
    }





}