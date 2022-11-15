package jp.funmake.example.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class LogWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val message = inputData.getString(PARAM_MESSAGE) ?: Result.failure()
        log.d("LogWorker::doWork: $message")
        return Result.success(workDataOf(PARAM_MESSAGE to message))
    }

    companion object {
        const val PARAM_MESSAGE = "message"
    }
}