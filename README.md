# CurrencyTextView

```xml
<com.aitsuki.currency.CurrencyEditText
    android:id="@+id/edit_text"
    android:layout_width="200dp"
    android:layout_height="wrap_content"
    android:hint="please enter"
    android:maxLength="20" />
```

```kotlin
val editText = findViewById<CurrencyEditText>(R.id.edit_text)

// Get or set value
editText.setValue(BigDecimal("1234567890.12"))
val value: BigDecimal = editText.getValue()

// Set Locale, default is Locale.getDefault()
editText.setLocale(Locale.US) // 1,234,567,890.12
editText.setLocale(Locale("pt")) // 1.234.567.890,12
```