package com.example.notifylistenerdemo

import android.content.ComponentName
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotifyService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        NotifyHelper.onReceive(sbn)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        NotifyHelper.onRemoved(sbn)
    }

    override fun onListenerDisconnected() {
        requestRebind(ComponentName(this, NotificationListenerService::class.java))
    }

}