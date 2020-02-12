package com.example.test_pay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.paystack.android.PaystackSdk
import com.example.test_pay.repository.TestPayRepository
import android.view.View
import android.widget.Button
import android.widget.EditText
import co.paystack.android.model.Card

class MainActivity : AppCompatActivity() {

    lateinit var makeNewPayment : Button
    lateinit var cardNumber : EditText
    lateinit var expiryMonth : EditText
    lateinit var expiryYear : EditText
    lateinit var cvc : EditText
    lateinit var email : EditText
    lateinit var amount : EditText
    lateinit var makePaymentAgain : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PaystackSdk.initialize(applicationContext)

        val testPayRepository = TestPayRepository(this)

//        testPayRepository.initializeTransaction()

        initializeViews()

        makeNewPayment.setOnClickListener(View.OnClickListener {
            val card : Card = generateCardFromDetails()
            val email : String = email.text.trim().toString()
            val amount = amount.text.trim().toString().toInt()
            testPayRepository.initiateCardCharge(card, email, amount)
        })
    }

    private fun generateCardFromDetails(): Card {
        val cardNumber: String = cardNumber.text.trim().toString()
        val expiryMonth: Int = expiryMonth.text.trim().toString().toInt()
        val expiryYear : Int = expiryYear.text.trim().toString().toInt()
        val cvc : String = cvc.text.trim().toString()
        return Card(cardNumber, expiryMonth, expiryYear, cvc);
    }

    private fun initializeViews() {
        makeNewPayment = findViewById<Button>(R.id.make_new_payment)
        cardNumber = findViewById<EditText>(R.id.card_number)
        expiryMonth = findViewById<EditText>(R.id.exp_month)
        expiryYear = findViewById<EditText>(R.id.exp_year)
        cvc = findViewById<EditText>(R.id.cvc)
        email = findViewById<EditText>(R.id.email)
        amount = findViewById<EditText>(R.id.amount)
        makePaymentAgain = findViewById<Button>(R.id.make_payment_again)
    }

}
