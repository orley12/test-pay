package com.example.test_pay

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.test_pay.util.TestPayUtils

class ConfirmationActivity : AppCompatActivity() {

    lateinit var testPayUtils: TestPayUtils

    lateinit var confirmAmount : TextView
    lateinit var confirmEmail : TextView
    lateinit var confirmCardType : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
        testPayUtils = TestPayUtils()

        initializeViews()

        val (amount, cardType, email) = getBundle()

        setTextOnViews(amount, email, cardType)

        testPayUtils.
            generateErrorToast(this, "Please check your email for confirmation")
    }

    private fun setTextOnViews(amount: String?, email: String?, cardType: String?) {
        confirmAmount.text = "Amount : $amount"
        confirmEmail.text = "Email : $email"
        confirmCardType.text = "Card : $cardType"
    }

    private fun initializeViews() {
        confirmAmount = findViewById(R.id.confirm_amount)
        confirmEmail = findViewById(R.id.confirm_email)
        confirmCardType = findViewById(R.id.card_type)
    }

    private fun getBundle(): Triple<String?, String?, String?> {
        val bundle = intent.getBundleExtra("bundle")
        val amount: String = bundle.get("amount").toString()
        val cardType: String = bundle.get("cardType").toString()
        val email: String = bundle.get("email").toString()
        return Triple(amount, cardType, email)
    }
}
