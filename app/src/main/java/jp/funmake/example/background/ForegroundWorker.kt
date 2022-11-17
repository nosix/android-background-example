package jp.funmake.example.background

import android.content.Context
import androidx.work.*

class ForegroundWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val notificationId = inputData.getInt(PARAM_NOTIFICATION_ID, 10)
        val message = inputData.getString(PARAM_MESSAGE) ?: return Result.failure()
        setForeground(createForegroundInfo(notificationId, message))
        longAction(20) {
            log.d("$it ForegroundWorker::doWork: $message")
        }
        return Result.success(workDataOf(PARAM_MESSAGE to message))
    }

    private fun createForegroundInfo(notificationId: Int, text: String): ForegroundInfo {
        val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)
        return ForegroundInfo(notificationId, applicationContext.createNotification(text, intent))
    }

    companion object {
        const val PARAM_NOTIFICATION_ID = "notificationId"
        const val PARAM_MESSAGE = "message"
    }
}