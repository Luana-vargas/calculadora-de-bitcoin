package br.com.luana.calculadoradebitcoin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.doAsync
import java.net.URL

class MainActivity : AppCompatActivity() {

    val API_URL = "https://www.mercadobitcoin.net/api/BTC/ticker/"

    var cotacaoBitcoin:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buscarCotacao()

    }

    fun buscarCotacao() {

        //doAsync, função da lib Anko
        doAsync {

            //Acessar a API e buscar seu resultado
            val resposta = URL(API_URL).readText()

        }

    }
}
