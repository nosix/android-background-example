package jp.funmake.example.background

import kotlinx.coroutines.delay

suspend fun longAction(seconds: Int, log: (Int) -> Unit) {
    for (i in 0 until seconds) {
        log(i)
        delay(1000 * 60)
    }
}