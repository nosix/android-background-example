package jp.funmake.example.background

import android.content.Context
import androidx.work.*

class ForegroundWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        setForeground(createForegroundInfo())
        val message = inputData.getString(PARAM_MESSAGE) ?: Result.failure()
        log.d("ForegroundWorker::doWork: $message")
        return Result.success(workDataOf(PARAM_MESSAGE to message))
    }

    private fun createForegroundInfo(): ForegroundInfo {
        val notificationId = 10
        val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)
        return ForegroundInfo(notificationId, applicationContext.createNotification(intent))
    }

    companion object {
        const val PARAM_MESSAGE = "message"
    }
}