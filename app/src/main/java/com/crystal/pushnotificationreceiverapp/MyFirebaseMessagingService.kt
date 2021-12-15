package com.crystal.pushnotificationreceiverapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService:FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        createNotificationChannel()

        val type = remoteMessage.data["type"]
            ?.let{NotificationType.valueOf(it)}
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]

        type ?: return

        NotificationManagerCompat.from(this)
            .notify(type.id, createNotification(type, title, message))
    }

    private fun createNotification(
        type: NotificationType,
        title: String?,
        message: String?,
    ): Notification {

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, type.id, intent, FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_android_black)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) //각각 중요도를 설정해줘야한다. 채널이전버전을 위해서
            .setContentIntent(pendingIntent)

        when(type){
            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> { //큰 텍스트 블록
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                         "😾😿🙀😽😼😻😹😸😺🎃🤖👾👽☠💀👻💩🤡👺👹👿😈🤠🤑🤕🤒😷🤧🤮🤢🥴🤐😵😪🤤😴🥶🥵🤯🤬🥳😎😃🌂🥽🕶🧳🎒💼👜👛👝💍👑⛑🎓👒🧢🎩🧣🧤🧦🥾")
                )
            }
            NotificationType.CUSTOM ->{
                notificationBuilder.setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(
                            packageName,
                            R.layout.view_custom_notification
                        ).apply {
                            setTextViewText(R.id.title, title)
                            setTextViewText(R.id.message, message)
                        }
                    )
            }
        }

        return notificationBuilder.build()
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //이 때만 채널을 만든다.
            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
                .apply { description = CHANNEL_DESCRIPTION }

            ((getSystemService(Context.NOTIFICATION_SERVICE)) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_NAME = "Emoji party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party 를 위한 채널"
        private const val CHANNEL_ID = "Channel Id"

    }
}