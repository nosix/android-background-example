package jp.funmake.example.background

import android.app.Service
import android.content.Intent
import android.os.IBinder

class BoundService : Service() {
    override fun onCreate() {
        log.d("BoundService::onCreate")
    }

    override fun onDestroy() {
        log.d("BoundService::onDestroy")
    }

    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }

    inner class Binder : android.os.Binder() {
        fun execute() {
            log.d("BoundService::execute")
        }
    }
}