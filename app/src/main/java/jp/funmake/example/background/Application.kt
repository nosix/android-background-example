package jp.funmake.example.background

import timber.log.Timber

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Tree())
        }
    }

    class Tree : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String {
            return "background-example"
        }
    }
}