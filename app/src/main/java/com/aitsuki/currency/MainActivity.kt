package com.aitsuki.currency

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editText = findViewById<CurrencyEditText>(R.id.edit_text)
        val tvValue = findViewById<TextView>(R.id.tv_value)
        val showBtn = findViewById<Button>(R.id.btn_show)
        val randomBtn = findViewById<Button>(R.id.btn_random)

        showBtn.setOnClickListener {
            editText.getValue().also {
                tvValue.text = it?.let { BigDecimal.valueOf(it).toPlainString() }
            }
        }

        randomBtn.setOnClickListener {
            editText.setValue(Random.nextDouble(0.0, 100000000.0))
        }
    }
}