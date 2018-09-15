package com.example.test_pay.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.test_pay.R
import com.example.test_pay.TestPayApplication
import com.example.test_pay.util.TestPayUtils
import javax.inject.Inject

class ConfirmationActivity : AppCompatActivity() {

    @Inject
    lateinit var testPayUtils: TestPayUtils

    lateinit var confirmAmount : TextView
    lateinit var confirmEmail : TextView
    lateinit var confirmCardType : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as TestPayApplication)
            .appComponent
            .inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        initializeViews()

        val (amount, cardType, email) = getBundle()

        setTextOnViews(amount, email, cardType)

        testPayUtils.
            generateErrorToast(this, "Please check your email for confirmation")
    }

    private fun setTextOnViews(amount: Int, email: String?, cardType: String?) {
        confirmAmount.text = "Amount : ${amount / 100}"
        confirmEmail.text = "Email : $email"
        confirmCardType.text = "Card : $cardType"
    }

    private fun initializeViews() {
        confirmAmount = findViewById(R.id.confirm_amount)
        confirmEmail = findViewById(R.id.confirm_email)
        confirmCardType = findViewById(R.id.card_type)
    }

    private fun getBundle(): Triple<Int, String, String> {
        val bundle = intent.getBundleExtra("bundle")
        val amount: Int = bundle.get("amount").toString().toInt()
        val cardType: String = bundle.get("cardType").toString()
        val email: String = bundle.get("email").toString()
        return Triple(amount, cardType, email)
    }
}
