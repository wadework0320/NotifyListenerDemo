package com.example.notifylistenerdemo

import android.service.notification.StatusBarNotification

object NotifyHelper {

    private var notifyListener: NotifyListener? = null

    fun setNotifyListener(notifyListener: NotifyListener?) {
        this.notifyListener = notifyListener
    }

    fun onReceive(sbn: StatusBarNotification?) {
        notifyListener?.onReceiveMessage(sbn)
    }

    fun onRemoved(sbn: StatusBarNotification?) {
        notifyListener?.onRemovedMessage(sbn)
    }

}