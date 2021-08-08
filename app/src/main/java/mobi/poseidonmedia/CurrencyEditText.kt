package mobi.poseidonmedia

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import java.text.NumberFormat

class CurrencyEditText: AppCompatEditText, TextWatcher {
    private var value = "0" // Or 0.toString() if you want to be fancy
    private var currency = NumberFormat.getCurrencyInstance() // This will format our number
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { // When the text is changed
        currency = NumberFormat.getCurrencyInstance() // For some reason, this needs to be defined each time
        if(s.toString() != value) { // Only if something has changed
            val clean = s.toString().clean() // Numbers only, plz
            value = clean.currencyParse() // Convert to currency
            setText(value) // Update the text with the formatted kind
            if(selectionStart == 0) setSelection(value.length) // Move cursor to the end
            // TODO: Don't move the cursor if it's been moved by the user
        }
    }

    constructor(ctx: Context, attr: AttributeSet?) : super(ctx, attr) {
        inputType = InputType.TYPE_CLASS_NUMBER // Bring up the numbers keyboard
    }

    // Clean a regular String into a numeric String
    private fun String.clean() = Regex("\\D").replace(this,"")

    // Parse string as currency
    private fun String.currencyParse() = if(this.toDoubleOrNull() != null)
        currency.format(this.toDouble()/100) // It's only currency if the number is valid
    else currency.format(0) // Otherwise, it's just sparkling zero

    // Get methods
    fun getDouble(): Double = value.clean().toDouble()/100
    fun getInt(): Int = value.clean().toInt()
    fun getString(): String = value.clean()
    fun getFormatted(): String = value.clean().currencyParse()

    // Set methods
    private fun setValue(newValue: String) {
        value = newValue
        setText(value.currencyParse())
    }
    fun setValue(newValue: Double) = setValue(newValue.toString().clean())
    fun setValue(newValue: Int) = setValue(newValue.toDouble())

    // Reflective constructors
    constructor(ctx: Context) : this(ctx, null)
    constructor(ctx: Context, attr: AttributeSet, style: Int) : this(ctx)

    // These methods are not used, but required by the interface
    override fun beforeTextChanged(a: CharSequence?, b: Int, c: Int, d: Int) = Unit
    override fun afterTextChanged(a: Editable?) = Unit
}