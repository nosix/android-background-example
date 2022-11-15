package jp.funmake.example.background

import android.app.Service
import android.content.Intent
import android.os.IBinder

class LogService : Service() {
    override fun onCreate() {
        log.d("LogService::onCreate")
    }

    override fun onDestroy() {
        log.d("LogService::onDestroy")
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("not supported")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return if (intent?.getBooleanExtra(PARAM_CANCEL, false) == true) {
            log.d("LogServer::cancel")
            stopSelf()
            START_NOT_STICKY
        } else {
            val returnValue = intent?.getIntExtra(PARAM_RETURN_VALUE, START_STICKY_COMPATIBILITY)
                ?: START_STICKY_COMPATIBILITY
            log.d("LogService::onStartCommand($flags, $startId) -> $returnValue")
            returnValue
        }
    }

    companion object {
        const val PARAM_CANCEL = "cancel"
        const val PARAM_RETURN_VALUE = "return"
    }
}