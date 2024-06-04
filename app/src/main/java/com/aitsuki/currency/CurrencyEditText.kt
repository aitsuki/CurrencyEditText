package com.aitsuki.currency

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.widget.EditText
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@SuppressLint("AppCompatCustomView")
class CurrencyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : EditText(context, attrs) {

    companion object {
        private const val D = ","
        private const val G = "."
        private val iFormat = DecimalFormat(
            "#,###",
            DecimalFormatSymbols(Locale.ROOT).apply {
                decimalSeparator = D[0]
                groupingSeparator = G[0]
            }
        )
    }

    init {
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        keyListener = DigitsKeyListener.getInstance("0123456789$D$G")

        // 不允许输入两个小数分隔符
        filters = arrayOf(*filters, object : InputFilter {
            override fun filter(
                source: CharSequence,
                start: Int,
                end: Int,
                dest: Spanned,
                dstart: Int,
                dend: Int
            ): CharSequence? {
                if (dest.contains(D) && source.subSequence(start, end) == D) {
                    return ""
                }
                return null
            }
        })

        addTextChangedListener(CurrencyTextWatcher(this))
    }

    private class CurrencyTextWatcher(private val editText: EditText) : TextWatcher {

        private var editing = false

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(text: Editable) {
            if (editing) return
            editing = true
            val source = text.toString()
            val clean = source.replace(G, "")
            if (clean.isEmpty()) {
                text.replace(0, text.length, "")
                editing = false
                return
            }

            if (clean.startsWith(D)) {
                text.replace(0, 1, "0$D")
                editing = false
                return
            }

            val ipart = iFormat.format(clean.substringBefore(D).toBigInteger())
            val builder = StringBuilder(ipart)
            if (source.contains(D)) {
                builder.append(D)
                val fpart = clean.substringAfter(D, "").take(2)
                for (c in fpart) {
                    builder.append("0")
                }
            }

            val newVal = builder.toString()
            if (source != newVal) {
                text.replace(0, text.length, newVal)
                val selection = editText.selectionStart
                if (selection < source.length) {
                    val gcount = source.substring(0, selection).count { it == G[0] }
                    val newGcount = newVal.substring(0, selection).count { it == G[0] }
                    if (newGcount > gcount) {
                        editText.setSelection((selection + newGcount - gcount).coerceAtMost(newVal.length))
                    }
                }
            }
            editing = false
        }
    }

    @SuppressLint("SetTextI18n")
    fun setValue(double: Double?) {
        if (double == null) {
            setText("")
        } else {
            setText(iFormat.format(double.toLong()) + "${D}00")
            setSelection(text.length)
        }
    }

    fun getValue(): Double? {
        return text.toString()
            .replace(G, "")
            .replace(D, ".")
            .toDoubleOrNull()
    }
}

