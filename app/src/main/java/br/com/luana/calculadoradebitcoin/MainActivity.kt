package br.com.luana.calculadoradebitcoin

import MonetaryMask
import android.os.Bundle

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL
import java.text.NumberFormat
import java.util.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.math.round


class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    val API_URL = "https://www.mercadobitcoin.net/api/BTC/ticker/"
    var cotacaoBitcoin: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()
        atualizarValor()


        btn_reload.setOnClickListener {
            atualizarValor()
        }

        btn_calcular.setOnClickListener {
            it.hideKeyboard()
            calcular()
            notificacaoSimples("Teste", "Teste")
        }
    }

    fun calcular() {

        if (txt_valor.text.isEmpty()) {
            txt_valor.error = "Preencha um valor"
            return
        }

        pb_loading.visibility = View.VISIBLE // exibindo barra de progresso
        btn_calcular.visibility = View.GONE // escondendo botão

        val valor_digitado = MonetaryMask.unMask(txt_valor).toDouble()
        if (cotacaoBitcoin > 0) valor_digitado / (round(cotacaoBitcoin * 100.0) / 100.0) else 0.0

        pb_loading.visibility = View.GONE // esconder barra de progresso
        btn_calcular.visibility = View.VISIBLE // exibindo botão

        val f = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
        val cotacaoFormatada = f.format(cotacaoBitcoin)

        txt_qtd_bitcoins.text = cotacaoFormatada

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


    private suspend fun buscarCotacao(): String {
        val resposta = URL(API_URL).readText()
        val cotacaoBitcoin = JSONObject(resposta).getJSONObject("ticker").getDouble("last")
        return cotacaoBitcoin.toString()
    }

    private fun atualizarValor () {
        launch {
            coroutineScope {
                val cotacao = async(Dispatchers.IO) { buscarCotacao() }
                val f = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
                val cotacaoValue = cotacao.await()
                cotacaoBitcoin = cotacaoValue.toDouble()
                val cotacaoFormatada = f.format(cotacaoBitcoin)
                txt_cotacao.text = cotacaoFormatada
            }
        }
    }


}









