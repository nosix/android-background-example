package jp.funmake.example.background

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class BoundService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        log.d("BoundService::onCreate")
    }

    override fun onDestroy() {
        log.d("BoundService::onDestroy")
        coroutineScope.cancel()
    }

    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }

    inner class Binder : android.os.Binder() {

        fun execute() {
            coroutineScope.launch {
                longAction(20) {
                    log.d("$it BoundService::execute")
                }
            }
        }
    }
}