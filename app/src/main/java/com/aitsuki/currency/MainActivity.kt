package com.aitsuki.currency

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editText = findViewById<CurrencyEditText>(R.id.edit_text)
        val tvValue = findViewById<TextView>(R.id.tv_value)
        val getBtn = findViewById<Button>(R.id.btn_get_value)
        val reverseBtn = findViewById<Button>(R.id.btn_reverse_symbol)

        getBtn.setOnClickListener {
            editText.getValue().also {
                tvValue.text = it.toPlainString()
            }
        }

        var isReverse = false
        reverseBtn.setOnClickListener {
            if (isReverse) {
                editText.setLocale(Locale.ENGLISH)
            } else {
                editText.setLocale(Locale("pt"))
            }
            isReverse = !isReverse
        }
    }
}