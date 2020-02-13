package com.example.test_pay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.paystack.android.PaystackSdk
import com.example.test_pay.repository.TestPayRepository
import android.view.View
import android.widget.Button
import android.widget.EditText
import co.paystack.android.model.Card
import com.example.test_pay.util.TestPayUtils

class MainActivity : AppCompatActivity() {

    lateinit var testPayUtils : TestPayUtils

    lateinit var testPayRepository : TestPayRepository

    lateinit var makeNewPayment : Button
    lateinit var cardNumber : EditText
    lateinit var expiryMonth : EditText
    lateinit var expiryYear : EditText
    lateinit var cvc : EditText
    lateinit var email : EditText
    lateinit var amount : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PaystackSdk.initialize(applicationContext)

        testPayRepository = TestPayRepository(this)

        testPayUtils = TestPayUtils();

        initializeViews()

        makeNewPayment.setOnClickListener(View.OnClickListener {
            makePayment()
        })
    }

    private fun initializeViews() {
        makeNewPayment = findViewById<Button>(R.id.make_new_payment)
        cardNumber = findViewById<EditText>(R.id.card_number)
        expiryMonth = findViewById<EditText>(R.id.exp_month)
        expiryYear = findViewById<EditText>(R.id.exp_year)
        cvc = findViewById<EditText>(R.id.cvc)
        email = findViewById<EditText>(R.id.email)
        amount = findViewById<EditText>(R.id.amount)
    }

    private fun makePayment() {
        var card: Card? = null
        var email : String? = null
        var amount : Int = 0

        try {
        card = generateCardFromDetails()
        email = this.email.text.trim().toString()
        amount = this.amount.text.trim().toString().toInt()

        validateInput(email, amount, card)
        } catch (e: Exception) {
            testPayUtils.generateErrorToast(this, "No field can be empty")
            return
        }

        testPayRepository.initiateCardCharge(card, email, amount)
    }

    private fun validateInput(email: String, amount: Int, card: Card) {
        if (email.length < 1 ||
                amount < 1 ||
                card.number.length < 1 ||
                card.cvc.length < 1 ||
                card.expiryMonth < 1 ||
                card.expiryYear < 1) {
            throw Exception("No field can be empty")
        }
        return
    }


    private fun generateCardFromDetails(): Card {
        val cardNumber: String = cardNumber.text.trim().toString()
        val expiryMonth: Int = expiryMonth.text.trim().toString().toInt()
        val expiryYear : Int = expiryYear.text.trim().toString().toInt()
        val cvc : String = cvc.text.trim().toString()
        return Card(cardNumber, expiryMonth, expiryYear, cvc);
    }

}
