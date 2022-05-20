package ru.geekbrains.kotlin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

//Server Key
//AAAA2DaU8YA:APA91bHVV7DMaGrMng8FVSfpAirkURiGRIHFQ7jW4Ieh8vVu70i61KcBJB-FpcIj4rAr7-PdY_ulujxTwIUVHdWzYkrmW_uZ6t8UYCnSlzud48VQ49iWQ8ZQQl8qAVfOsqckXoAd-AMx

class MyService : FirebaseMessagingService() {

    companion object{
        private const val NOTIFICATION_ID_LOW = 1
        private const val CHANNEL_ID_LOW = "channel_id_1"

        private const val KEY_MESSAGE = "myMessage"
        private const val KEY_TITLE = "myTitle"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if(!message.data.isNullOrEmpty()){
            val title = message.data[KEY_TITLE]
            val message = message.data[KEY_MESSAGE]
            if(!title.isNullOrEmpty()&&!message.isNullOrEmpty()){
                push(title,message)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun push(title: String, message: String){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilderHigh = NotificationCompat.Builder(this, CHANNEL_ID_LOW).apply {
            setSmallIcon(R.drawable.ic_map_marker)
            setContentTitle(title)
            setContentText(message)
            priority = NotificationManager.IMPORTANCE_HIGH
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelNameLow = "Name $CHANNEL_ID_LOW"
            val channelDescriptionLow = "Description $CHANNEL_ID_LOW"
            val channelPriorityLow = NotificationManager.IMPORTANCE_LOW
            val channelLow = NotificationChannel(CHANNEL_ID_LOW, channelNameLow, channelPriorityLow).apply {
                description = channelDescriptionLow
            }
            notificationManager.createNotificationChannel(channelLow)
        }
        notificationManager.notify(NOTIFICATION_ID_LOW, notificationBuilderHigh.build())
    }

}