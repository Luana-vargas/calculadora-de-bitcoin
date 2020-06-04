import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.NumberFormat
import java.util.*


class MonetaryMask{

    companion object {

        private fun replaceChars(txt: String): String {
            return txt.replace(Regex("[\\D]"), "")
        }

        fun unMask (edit: EditText): String {
           return edit.text.toString()
               .replace(Regex("[a-zA-Z\$.]"), "")
               .replace(",", ".")
               .trim() // trocar letras por vazio
        }

        fun monetary(edit: EditText): TextWatcher{
            val textWatcher: TextWatcher = object : TextWatcher{

                private var current = ""

                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
//s= retorna o que foi digitado no edt / count === zero usuário apagando algo do edt
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!s.toString().equals(current)){
                        edit.removeTextChangedListener(this)
                        val cleanString = replaceChars(s.toString())

                        val value = cleanString.toDouble()

                        val formated = NumberFormat.getCurrencyInstance(Locale
                            ("pt", "br")).format((value/100))

                        current = formated
                        edit.setText(formated)
                        edit.setSelection(formated.length)

                        edit.addTextChangedListener(this)
                    }
                }

            }

            return textWatcher;
        }

        fun custom(mask: String, edit: EditText): TextWatcher {
            val textWatcher: TextWatcher = object: TextWatcher {

                var isUpdating: Boolean = false
                var oldString: String = ""

                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val str = replaceChars(s.toString())
                    var formated = ""

                    if (count == 0) // is deleting
                        isUpdating = true

                    if (isUpdating){
                        oldString = str
                        isUpdating = false
                        return
                    }

                    var i = 0

                    for (m: Char in mask.toCharArray()){
                        if (m != '#' && str.length > oldString.length){
                            formated += m
                            continue
                        }
                        try {
                            formated += str.get(i)
                        }catch (e: Exception){
                            break
                        }
                        i++
                    }

                    isUpdating = true
                    edit.setText(formated)
                    edit.setSelection(formated.length)
                }
            }
            return textWatcher
        }
    }

}