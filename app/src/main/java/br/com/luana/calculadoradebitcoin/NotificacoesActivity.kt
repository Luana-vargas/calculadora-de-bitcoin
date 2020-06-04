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

class NotificacoesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificacoes)
    }
}

fun Activity.notificacaoSimples(title:String, message:String) {

    val nBuilder = NotificationCompat.Builder(this, "default") // id do canal de notificação

    //Definindo um icone pequeno, está sendo acessado por 'R.mipmap.ic_launcher'
    nBuilder.setSmallIcon(R.drawable.ic_bitcoin)

    //Definindo o titulo da notificação
    nBuilder.setContentTitle(title)

    //Definindo o conteudo da notificação
    nBuilder.setContentText(message)

    //construindo o objeto Notification
    val notificacao = nBuilder.build()

    //Acessando o serviço de notificação do sistema
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        //criação do objeto
        val channel = NotificationChannel("default",
            "Canal de notificação teste",
            NotificationManager.IMPORTANCE_DEFAULT)

        //criar o canal de notificação
        notificationManager.createNotificationChannel(channel)
    }


    // enviando a notificação em si
    notificationManager.notify(1, notificacao)

}