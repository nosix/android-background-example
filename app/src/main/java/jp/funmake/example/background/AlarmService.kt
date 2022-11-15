package jp.funmake.example.background

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AlarmService : Service() {
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
        log.d("AlarmService::onStartCommand($flags, $startId)")
        stopSelf()
        return START_NOT_STICKY
    }
}