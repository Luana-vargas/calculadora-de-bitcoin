package br.com.luana.calculadoradebitcoin

import MonetaryMask
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
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

        txt_valor.addTextChangedListener(MonetaryMask.monetary(txt_valor))
        txt_valor.setOnEditorActionListener { v, actionId, event ->

            if ( actionId == EditorInfo.IME_ACTION_DONE ||
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER ||
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER){
                calcular()
                v.hideKeyboard()
                true
            }
            false
        }


        btn_calcular.setOnClickListener {
            it.hideKeyboard()
            calcular()
        }


        btn_reload.setOnClickListener {
            atualizarValor()
        }
    }

    fun calcular() {

        if (txt_valor.text.isEmpty()) {
            txt_valor.error = "Preencha um valor"
            return
        }

        val valor_digitado = MonetaryMask.unMask(txt_valor).toDouble()
        val resultado = if (cotacaoBitcoin > 0) valor_digitado / (round(cotacaoBitcoin * 100.0) / 100.0) else 0.0
        txt_qtd_bitcoins.text = "%.8f".format(resultado)

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









