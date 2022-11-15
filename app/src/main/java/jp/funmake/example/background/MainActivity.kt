package jp.funmake.example.background

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        log.d("MainActivity::onCreate")
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
}