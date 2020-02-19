package com.example.fibonacciprimethreadrunnables

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity(), AsyncTaskCallback {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAsyncTaskEvent(event : AsynTaskEvent) {
        tvPrimeResult.text = event.resultMessage
    }

    fun runThreadRunnable(inputString : String) {
        var reversableString = inputString
        val thread = Thread(Runnable {
            runOnUiThread{ tvReverseString.text = "Thread Started with " + inputString}
            for(i in 0..10) {
                reversableString = inputString.reversed()
                runOnUiThread{ tvReverseString.text = "Thread has now reversed the string!"}
                Thread.sleep(100)
            }
            runOnUiThread{ tvReverseString.text ="Thread Stopped with " + reversableString}

        })
        thread.start()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnStartThread -> {
                runThreadRunnable(etString.text.toString())
            }
            R.id.btnFindPrimes -> {
                val asyncPrime = AsyncPrime(
                    etRangeM.text.toString().toInt(),etRangeN.text.toString().toInt())
                asyncPrime.execute()
            }
            R.id.btnFibonacci -> {
                onLooperExecuted("test")
            }
        }
    }

    override fun onResult(result: String?) {
        tvPrimeResult.text = result?: "No Result"
    }

    fun onLooperExecuted(valuePassed: String){
        val calcFibo = FibonacciLooper()
        calcFibo.handleMessages(Handler(Looper.getMainLooper()){
                message ->
            tvFiboResult.text = message.data.getString("KEY")
            true
        })
        calcFibo.start()
        val message = Message()
        message.data.putString("KEY_MAIN", valuePassed)
        calcFibo.myHandler.sendMessage(message)
    }

}
