package com.example.notifylistenerdemo

import android.annotation.SuppressLint
import android.app.Notification
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.service.notification.StatusBarNotification
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.example.notifylistenerdemo.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), NotifyListener {

    private lateinit var binding: ActivityMainBinding
    private val notificationListener =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (isNLServiceEnabled()) {
                binding.textView.text = "已開啟"
                toggleNotificationListenerService()
            } else {
                binding.textView.text = "未開啟"
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NotifyHelper.setNotifyListener(this)
    }

    fun requestPermission(view: View) {
        if (!isNLServiceEnabled()) {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            notificationListener.launch(intent)
        } else {
            binding.textView.text = "已開啟"
            toggleNotificationListenerService()
        }
    }

    private fun isNLServiceEnabled(): Boolean {
        val packageNames = NotificationManagerCompat.getEnabledListenerPackages(this)
        return packageNames.contains(packageName)
    }

    private fun toggleNotificationListenerService() {
        packageManager.setComponentEnabledSetting(
            ComponentName(applicationContext, NotifyService::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
        )

        packageManager.setComponentEnabledSetting(
            ComponentName(applicationContext, NotifyService::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onReceiveMessage(sbn: StatusBarNotification?) {
        sbn?.apply {
            val msgContent = if (sbn.notification.tickerText != null) {
                sbn.notification.tickerText.toString()
            } else {
                val extras = sbn.notification.extras
                val title = extras.getString(Notification.EXTRA_TITLE)
                val text = extras.getString(Notification.EXTRA_TEXT)
                "$title:$text"
            }

            val time =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE).format(Date(sbn.postTime))
            binding.textView.text = "應用包名：${sbn.packageName}\n消息内容：${msgContent}\n消息時間：${time}"
        }
    }

    override fun onRemovedMessage(sbn: StatusBarNotification?) {
        binding.textView.text = "通知移除"
    }

}