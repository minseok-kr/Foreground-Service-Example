package com.minseok.foregroundservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setNotificationChannel()
    }

    private fun setNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "일반", NotificationManager.IMPORTANCE_LOW)

            getSystemService(NotificationManager::class.java)
                    ?.createNotificationChannel(serviceChannel)
        }
    }

    fun startService(v: View) {
        Intent(this, TargetService::class.java).also {
            ContextCompat.startForegroundService(this, it)
        }
    }

    fun stopService(v: View) {
        Intent(this, TargetService::class.java).also {
            stopService(it)
        }
    }

    companion object {
        const val CHANNEL_ID = "_notification_channel_id"
    }
}

class TargetService : Service() {

    // 서비스 시작 시 호출 됨. 한 번이 아닌 여러번 발생할 수 있음.
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .build()

        startForeground(FOREGROUND_NOTIFICATION_ID, notification)

        return Service.START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent): IBinder? = null

    companion object {
        internal var FOREGROUND_NOTIFICATION_ID = 1
    }
}
