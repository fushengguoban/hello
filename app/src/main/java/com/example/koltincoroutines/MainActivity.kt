package com.example.koltincoroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initCoroutines()
    }

    private fun initCoroutines() {

    }

    /**
     * createCoroutine 函数 我们可以使用这个函数构建协程，但是这个协程并不会立即执行
     * fun <T> (suspend () -> T).createCoroutine(
    completion: Continuation<T>
    ): Continuation<Unit>
     * 其中 suspend() -> T 是 createCoroutine函数的Receiver ，Receiver 是一个被suspend修饰的挂起函数，这也是
     * 协程的执行体，简称协程体
     *
     * completion 会在协程执行完成后调用，实际上就是协程的完成回调
     *
     * 返回值是一个Continuation对象，通过这个值来触发启动协程
     */
    val continuation = suspend {
        println("In coroutine.")
        5
    }.createCoroutine(object : Continuation<Int> {
        override val context = EmptyCoroutineContext

        override fun resumeWith(result: Result<Int>) {
            println("Coroutine End:$result")
        }

    })


    val continuation2 = suspend {
        println("In coroutine2.")
        5
    }.startCoroutine(object : Continuation<Int> {
        override val context = EmptyCoroutineContext
        override fun resumeWith(result: Result<Int>) {
        }

    })


    suspend fun delay(time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS) {
        if (time <= 0) {
            return
        }

        suspendCoroutine<Unit> { continuation ->
            executor.schedule({ continuation.resume(Unit) }, time, unit)
        }
    }

    private val executor = Executors.newScheduledThreadPool(1) { runnable ->
        Thread(runnable, "Scheduler").apply { isDaemon = true }
    }

    val ints = sequence {
        (1..3).forEach {
            yield(it)
        }
    }


}