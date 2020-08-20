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
               .trim()
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
    }
}