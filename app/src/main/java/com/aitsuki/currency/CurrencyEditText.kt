package com.aitsuki.currency

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.widget.EditText
import java.math.BigDecimal
import java.text.DecimalFormatSymbols
import java.util.Locale

@SuppressLint("AppCompatCustomView")
class CurrencyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : EditText(context, attrs) {

    private lateinit var symbols: DecimalFormatSymbols
    private var currencyTextWatcher: CurrencyTextWatcher? = null

    init {
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        setLocale(Locale.getDefault())
    }

    fun setLocale(locale: Locale) {
        val sourceValue = if (text.isNotEmpty()) getValue() else null
        symbols = DecimalFormatSymbols(locale)
        keyListener = DigitsKeyListener.getInstance("0123456789${symbols.groupingSeparator}${symbols.decimalSeparator}")
        removeTextChangedListener(currencyTextWatcher)
        currencyTextWatcher = CurrencyTextWatcher(this, symbols)
        addTextChangedListener(currencyTextWatcher)
        sourceValue?.let { setValue(it) }
    }

    fun setValue(value: BigDecimal) {
        val text = value.toPlainString().replace(".", symbols.decimalSeparator.toString())
        setText(text)
        setSelection(getText().length)
    }

    fun getValue(): BigDecimal {
        val text = text.toString()
        if (text.isEmpty()) return BigDecimal.ZERO
        return text
            .replace(symbols.groupingSeparator.toString(), "")
            .replace(symbols.decimalSeparator.toString(), ".")
            .toBigDecimal()
    }

    fun setDoubleValue(value: Double) {
        setValue(BigDecimal.valueOf(value))
    }

    fun getDoubleValue(): Double {
        return getValue().toDouble()
    }
}