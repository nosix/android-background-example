package jp.funmake.example.background

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.ServiceCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ForegroundService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        log.d("ForegroundService::onCreate")
    }

    override fun onDestroy() {
        log.d("ForegroundService::onDestroy")
        coroutineScope.cancel()
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("not supported")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channelId = 20
        val cancelIntent = Intent(this, LogService::class.java).apply {
            putExtra(PARAM_CANCEL, true)
        }
        val pendingIntent = PendingIntent.getService(
            this,
            0,
            cancelIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        startForeground(channelId, createNotification(pendingIntent))

        return if (intent?.getBooleanExtra(PARAM_CANCEL, false) == true) {
            log.d("ForegroundServer::cancel")
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
            stopSelf()
            START_NOT_STICKY
        } else {
            val returnValue = intent?.getIntExtra(PARAM_RETURN_VALUE, START_STICKY_COMPATIBILITY)
                ?: START_STICKY_COMPATIBILITY
            coroutineScope.launch {
                longAction(20) {
                    log.d("$it ForegroundService::onStartCommand($flags, $startId) -> $returnValue")
                }
                stopSelf()
            }
            returnValue
        }
    }

    companion object {
        const val PARAM_CANCEL = "cancel"
        const val PARAM_RETURN_VALUE = "return"
    }
}