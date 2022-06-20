package com.biotech.framework.extension

import android.util.Log
import java.util.concurrent.CompletableFuture


class AsyncTask(
    private val worker: (bg : AsyncTask?) -> Unit,
    private val callback : (()-> Unit)? = null,
    private var cancel : (() -> Unit)? = null
) : CompletableFuture<Void>() {

    private var su : CompletableFuture<Unit>? = null

    init {
       su =  supplyAsync {
            try {
                Log.e("TAG","執行緒")
                worker(this)
                if(isDone){
                    Log.e("TAG","已完成執行緒")
                }

            } catch (e: InterruptedException) {
                throw IllegalStateException(e)
            }
        }.exceptionally {
            Log.e("TAG",it.message!!);
        }

    }


    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        cancel?.invoke()
        return super.cancel(mayInterruptIfRunning)
    }

    override fun isDone(): Boolean {
        callback?.invoke()
        return super.isDone()
    }
}