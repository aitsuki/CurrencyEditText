# CurrencyTextView

```xml
<com.aitsuki.currency.CurrencyEditText
    android:id="@+id/edit_text"
    android:layout_width="200dp"
    android:layout_height="wrap_content"
    android:hint="please enter"
    android:maxLength="14" />
```

```kotlin
val editText = findViewById<CurrencyEditText>(R.id.edit_text)
// Get or set double value
editText.setValue(1000.0)
editText.getValue()
```