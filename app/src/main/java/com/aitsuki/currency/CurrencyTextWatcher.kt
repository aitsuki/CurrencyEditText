package com.aitsuki.currency

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import java.text.DecimalFormatSymbols

class CurrencyTextWatcher(
    private val editText: EditText,
    symbols: DecimalFormatSymbols,
) : TextWatcher {

    private var groupingSeparator = symbols.groupingSeparator
    private var decimalSeparator = symbols.decimalSeparator

    private val notCurrencyRegex = "[^0-9$groupingSeparator$decimalSeparator]".toRegex()
    private var ignoreNextChanged = false

    override fun afterTextChanged(s: Editable?) {
        if (ignoreNextChanged) {
            ignoreNextChanged = false
            return
        }
        val initial = s?.toString() ?: return
        if (initial.isEmpty()) return
        if (initial == decimalSeparator.toString()) {
            ignoreNextChanged = true
            s.replace(0, initial.length, "0$decimalSeparator")
            return
        }

        var processed = initial
            .replace(notCurrencyRegex, "")
            .replace(groupingSeparator.toString(), "")
        if (processed.count { it == decimalSeparator } > 1) {
            processed = processed.substringBeforeLast(decimalSeparator)
        }

        val hasDecimal = processed.contains(decimalSeparator)
        if (hasDecimal) {
            var integerPart = processed.substringBefore(decimalSeparator)
            if (integerPart.isNotEmpty()) {
                integerPart = formatIntegerPart(integerPart)
            }
            var decimalPart = processed.substringAfter(decimalSeparator)
            if (decimalPart.length > 2) {
                decimalPart = decimalPart.substring(0, 2)
            }
            processed = "$integerPart$decimalSeparator$decimalPart"
        } else {
            processed = formatIntegerPart(processed)
        }

        if (initial != processed) {
            ignoreNextChanged = true
            s.replace(0, initial.length, processed)
            if (processed.length > initial.length && editText.selectionEnd < initial.length) {
                editText.setSelection(editText.selectionEnd + processed.length - initial.length)
            }
        }
    }

    private fun formatIntegerPart(s: String): String {
        return buildString {
            append(s)
            for (i in s.length - 3 downTo 1 step 3) {
                insert(i, groupingSeparator)
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}