package br.com.luana.calculadoradebitcoin

import MonetaryMask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoAsyncContext
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL
import java.text.NumberFormat
import java.util.*
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.EditText
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.text.TextUtils.replace
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import org.w3c.dom.Text
import java.text.DecimalFormat
import kotlin.math.round


class MainActivity : AppCompatActivity() {


    val API_URL = "https://www.mercadobitcoin.net/api/BTC/ticker/"

    var cotacaoBitcoin:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buscarCotacao()

       txt_valor.addTextChangedListener(MonetaryMask.monetary(txt_valor))
        txt_valor.setOnEditorActionListener { v, actionId, event ->
            if ( actionId == EditorInfo.IME_ACTION_DONE ||
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER){
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

    }


    fun buscarCotacao() {

        doAsync {

            //Acessar a API e buscar seu resultado
            val resposta = URL(API_URL).readText()

            //Acessando a cotação da String em Json
            cotacaoBitcoin = JSONObject(resposta).getJSONObject("ticker").getDouble("last")

            //Formatação em moeda
            val f = NumberFormat.getCurrencyInstance(Locale("pt", "br"))

            val cotacaoFormatada = f.format(cotacaoBitcoin)

            //para o código voltar a ser executado no processo principal
            uiThread {
                txt_cotacao.text = "$cotacaoFormatada"
            }
        }
    }

    fun calcular() {


        if(txt_valor.text.isEmpty()) {
            txt_valor.error = "Preencha um valor"
            return
        }

        pb_loading.visibility = View.VISIBLE // exibindo barra de progresso
        btn_calcular.visibility = View.GONE // escondendo botão


        val valor_digitado = MonetaryMask.unMask(txt_valor).toDouble()


        //calculando o resultado
        //caso o valor da cotacão seja maior que zero, efetuamos o calculo
        //caso contrario devolvemos 0
        val resultado =   if(cotacaoBitcoin > 0) valor_digitado / (round(cotacaoBitcoin*100.0) / 100.0) else 0.0

        //atualizando a TextView com o resultado formatado com 8 casas decimais
        txt_qtd_bitcoins.text = "%.8f".format(resultado)

        pb_loading.visibility = View.GONE // esconder barra de progresso
        btn_calcular.visibility = View.VISIBLE // exibindo botão


    }

}


