package com.example.notifylistenerdemo

import android.service.notification.StatusBarNotification

interface NotifyListener {

    fun onReceiveMessage(sbn: StatusBarNotification?)

    fun onRemovedMessage(sbn: StatusBarNotification?)

}