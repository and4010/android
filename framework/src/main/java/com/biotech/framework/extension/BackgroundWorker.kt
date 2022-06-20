package com.biotech.framework.extension

import android.os.AsyncTask

class BackgroundWorker (
    private val worker: (bg : BackgroundWorker?) -> Unit,
    private val callback : (()-> Unit)? = null,
    private var cancel : (() -> Unit)? = null
) : AsyncTask<Void, Void, Void>() {

    init{
        execute()
    }
    override fun doInBackground(vararg params:Void?):Void? {
        worker(this)
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        callback?.invoke()
    }

    override fun onCancelled() {
        super.onCancelled()
        cancel?.invoke()
    }
}