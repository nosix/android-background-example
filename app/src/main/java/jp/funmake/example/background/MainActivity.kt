package jp.funmake.example.background

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        log.d("MainActivity::onCreate")
        findViewById<Button>(R.id.cancel).setOnClickListener {
            cancel()
        }
        startBackgroundTasks()
    }

    override fun onStart() {
        super.onStart()
        log.d("MainActivity::onStart")
    }

    override fun onResume() {
        super.onResume()
        log.d("MainActivity::onResume")
    }

    override fun onPause() {
        super.onPause()
        log.d("MainActivity::onPause")
    }

    override fun onStop() {
        super.onStop()
        log.d("MainActivity::onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        log.d("MainActivity::onDestroy")
    }

    private fun startBackgroundTasks() {
        startWorkers()
        startServices()
        startAlarms()
    }

    private fun startWorkers() {
        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<LogWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .setInputData(workDataOf(LogWorker.PARAM_MESSAGE to "oneTime"))
            .build()
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<LogWorker>(15, TimeUnit.MINUTES, 15, TimeUnit.MINUTES)
                .setInitialDelay(1, TimeUnit.MINUTES)
                .setInputData(workDataOf(LogWorker.PARAM_MESSAGE to "periodic"))
                .build()
        val foregroundOneTimeWorkRequest = OneTimeWorkRequestBuilder<ForegroundWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .setInputData(workDataOf(ForegroundWorker.PARAM_MESSAGE to "foregroundOneTime"))
            .build()
        val foregroundPeriodicWorkRequest =
            PeriodicWorkRequestBuilder<ForegroundWorker>(15, TimeUnit.MINUTES, 15, TimeUnit.MINUTES)
                .setInitialDelay(1, TimeUnit.MINUTES)
                .setInputData(workDataOf(ForegroundWorker.PARAM_MESSAGE to "foregroundPeriodic"))
                .build()

        WorkManager.getInstance(this).run {
            enqueueUniqueWork(
                WORK_NAME_ONE_TIME_BACKGROUND,
                ExistingWorkPolicy.REPLACE,
                oneTimeWorkRequest
            )
            enqueueUniquePeriodicWork(
                WORK_NAME_PERIODIC_BACKGROUND,
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWorkRequest
            )
            enqueueUniqueWork(
                WORK_NAME_ONE_TIME_FOREGROUND,
                ExistingWorkPolicy.REPLACE,
                foregroundOneTimeWorkRequest
            )
            enqueueUniquePeriodicWork(
                WORK_NAME_PERIODIC_FOREGROUND,
                ExistingPeriodicWorkPolicy.REPLACE,
                foregroundPeriodicWorkRequest
            )
        }
    }

    private fun startServices() {
        val backgroundService = Intent(this, LogService::class.java).apply {
            putExtra(LogService.PARAM_RETURN_VALUE, Service.START_STICKY)
        }
        val foregroundService = Intent(this, ForegroundService::class.java).apply {
            putExtra(ForegroundService.PARAM_RETURN_VALUE, Service.START_STICKY)
        }
        val boundService = Intent(this, BoundService::class.java)

        startService(backgroundService)
        ActivityCompat.startForegroundService(this, foregroundService)
        bindService(boundService, serviceConnection, Service.BIND_AUTO_CREATE)
    }

    private fun startAlarms() {
        val alarmAction = Intent(this, AlarmService::class.java).let {
            PendingIntent.getService(
                this,
                0,
                it,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        val alarmManager = checkNotNull(getSystemService<AlarmManager>())
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 1000 * 60,
            alarmAction
        )
        this.alarmAction = alarmAction
    }

    private fun cancel() {
        log.d("MainActivity::cancel")
        cancelWorkers()
        cancelServices()
        cancelAlarms()
    }

    private fun cancelWorkers() {
        WorkManager.getInstance(this).run {
            cancelUniqueWork(WORK_NAME_ONE_TIME_BACKGROUND)
            cancelUniqueWork(WORK_NAME_PERIODIC_BACKGROUND)
            cancelUniqueWork(WORK_NAME_ONE_TIME_FOREGROUND)
            cancelUniqueWork(WORK_NAME_PERIODIC_FOREGROUND)
        }
    }

    private fun cancelServices() {
        stopService(Intent(this, LogService::class.java))
        stopService(Intent(this, ForegroundService::class.java))
        unbindService(serviceConnection)
    }

    private fun cancelAlarms() {
        val alarmManager = checkNotNull(getSystemService<AlarmManager>())
        alarmAction?.let { action ->
            alarmManager.cancel(action)
            alarmAction = null
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            log.d("onServiceConnected: $name")
            (binder as? BoundService.Binder)?.execute()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            log.d("onServiceDisconnected: $name")
        }
    }

    private var alarmAction: PendingIntent? = null

    companion object {
        private const val WORK_NAME_ONE_TIME_BACKGROUND = "oneTimeBackground"
        private const val WORK_NAME_PERIODIC_BACKGROUND = "periodicBackground"
        private const val WORK_NAME_ONE_TIME_FOREGROUND = "oneTimeForeground"
        private const val WORK_NAME_PERIODIC_FOREGROUND = "periodicForeground"
    }
}