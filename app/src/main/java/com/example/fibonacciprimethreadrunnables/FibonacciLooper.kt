package com.example.fibonacciprimethreadrunnables

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log

class FibonacciLooper : Thread(){
    lateinit var mainHandler : Handler
    lateinit var myHandler: Handler

    fun handleMessages(passedHandler: Handler){
        mainHandler = passedHandler
        myHandler = MyLooper()
    }

    inner class MyLooper : Handler(Looper.myLooper()!!)
    {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            var t1 = 0
            var t2 = 1
            val fiboList = mutableListOf<Int>()
            var strOutput: String

            for (i in 1..20) {
                fiboList.add(t1)
                val sum = t1 + t2
                t1 = t2
                t2 = sum
            }
            val message = Message()
            message.what = msg.what
            strOutput = fiboList.toString()
            Log.d("Test", strOutput)
            val bundle = Bundle()
            bundle.putString(
                "KEY",
                "Fibonacci Output : $strOutput"
            )
            message.data = bundle
            mainHandler.sendMessage(message)

        }

    }

    override fun run() {
        super.run()
        Looper.prepare()
        Looper.loop()
    }
}