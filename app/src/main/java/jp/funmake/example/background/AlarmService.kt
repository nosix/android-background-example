package jp.funmake.example.background

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        log.d("AlarmService::onCreate")
    }

    override fun onDestroy() {
        log.d("AlarmService::onDestroy")
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("not supported")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        coroutineScope.launch {
            longAction(20) {
                log.d("$it AlarmService::onStartCommand($flags, $startId)")
            }
            stopSelf()
        }
        return START_NOT_STICKY
    }
}