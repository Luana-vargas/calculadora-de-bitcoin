package br.com.luana.calculadoradebitcoin

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService

// TODO Gerar notificação com app em background

class NotificacoesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificacoes)
    }
}

fun Activity.notificacaoSimples(title:String, message:String) {

    val nBuilder = NotificationCompat.Builder(this, "default")

    nBuilder.setSmallIcon(R.drawable.ic_bitcoin)

    nBuilder.setContentTitle(title)

    nBuilder.setContentText(message)

    val notificacao = nBuilder.build()

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val channel = NotificationChannel("default",
            "Canal de notificação teste",
            NotificationManager.IMPORTANCE_DEFAULT)

        notificationManager.createNotificationChannel(channel)
    }

    notificationManager.notify(1, notificacao)

}