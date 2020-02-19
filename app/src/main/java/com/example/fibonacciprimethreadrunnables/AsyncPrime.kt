package com.example.fibonacciprimethreadrunnables

import android.os.AsyncTask
import android.util.Log
import org.greenrobot.eventbus.EventBus
import java.lang.StringBuilder

class AsyncPrime(intM : Int, intN : Int) : AsyncTask<Int, Int, String>() {

    var i = 0
    var rangeStart = intM
    var rangeEnd = intN

    //Runs on Main Thread
    //Setup the task to run
    override fun onPreExecute() {
        super.onPreExecute()
    }
    //Runs on WORKER thread
    //The task that needs to be run
    override fun doInBackground(vararg params: Int?): String? {
        val primeList = mutableListOf<Int>()
        while (rangeStart < rangeEnd) {
            if (checkPrimeNumber(rangeStart))
                primeList.add(rangeStart)
            ++rangeStart
        }

        return primeList.toString()
    }

    fun checkPrimeNumber(num: Int): Boolean {
        var flag = true
        for (i in 2..num / 2) {
            if (num % i == 0) {
                flag = false
                break
            }
        }
        return flag
    }

    //Runs on Main Thread
    //Used to get updates about task status
//    override fun onProgressUpdate(vararg values: String?) {
//        super.onProgressUpdate(*values)
//
//    }

    //Runs on Main Thread
    // Reports the results of the task
    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        //asyncTaskCallback.onResult(result)
        Log.d("ASYNC", result)
        EventBus.getDefault().post(AsynTaskEvent(result?: "No Result"))
    }
}

interface AsyncTaskCallback {
    fun onResult(result: String?)
}