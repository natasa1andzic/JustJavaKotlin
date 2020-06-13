package com.example.natasaandzic.justjava

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var number = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        orderBtn.setOnClickListener {
            val (value, fullMessage) = prepareOrder()
            sendOrderEmail(value, fullMessage)
        }

        incrementBtn.setOnClickListener {
            number++
            if (number > 30) {
                Toast.makeText(this@MainActivity, "You cannot have more than 30 cups of coffee", Toast.LENGTH_SHORT).show()
                number = 30
            }
            quantityNumber.text = number.toString()
        }
        decrementBtn.setOnClickListener {
            if (number > 0) {
                Toast.makeText(this@MainActivity, "You cannot have less than 0 cups of coffee", Toast.LENGTH_SHORT).show()
                number--
                quantityNumber.text = number.toString()
            } else {
                number = 0
                quantityNumber.text = number.toString()
            }
        }
    }

    private fun prepareOrder(): Pair<String, String> {
        val value = nameEt.text.toString()
        val hasSoyMilk = soyMilkCb.isChecked
        val hasChocolate = chocolateCb.isChecked
        val price = calculatePrice(hasSoyMilk, hasChocolate)
        val fullMessage = createOrderSummary(price, hasSoyMilk, hasChocolate, value)

        quantityNumber.text = fullMessage
        priceTv.text = calculatePrice(hasSoyMilk, hasChocolate).toString()
        return Pair(value, fullMessage)
    }

    private fun sendOrderEmail(value: String, fullMessage: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for $value")
        intent.putExtra(Intent.EXTRA_TEXT, "Just Java order for $fullMessage")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun calculatePrice(soy: Boolean, choco: Boolean): Int {
        var basePrice = 190
        if (soy) basePrice += 30
        if (choco) basePrice += 50
        return number * basePrice
    }

    private fun createOrderSummary(price: Int, soy: Boolean, choco: Boolean, name: String): String {
        val quantity = quantityNumber.text.toString()
        var priceMessage = "Name $name"
        priceMessage += "\nQuantity: $quantity"
        priceMessage += "\nSoy milk? $soy"
        priceMessage += "\nChocolate? $choco"
        priceMessage += """
Total: ${number * price} RSD"""
        priceMessage += "\nThank you! :)"
        return priceMessage
    }
}