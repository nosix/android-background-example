package jp.funmake.example.background

import android.content.Context
import androidx.work.*

class ForegroundWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val notificationId = inputData.getInt(PARAM_NOTIFICATION_ID, 10)
        setForeground(createForegroundInfo(notificationId))
        val message = inputData.getString(PARAM_MESSAGE) ?: Result.failure()
        log.d("ForegroundWorker::doWork: $message")
        return Result.success(workDataOf(PARAM_MESSAGE to message))
    }

    private fun createForegroundInfo(notificationId: Int): ForegroundInfo {
        val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)
        return ForegroundInfo(notificationId, applicationContext.createNotification(intent))
    }

    companion object {
        const val PARAM_NOTIFICATION_ID = "notificationId"
        const val PARAM_MESSAGE = "message"
    }
}