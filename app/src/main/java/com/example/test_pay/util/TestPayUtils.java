package com.example.test_pay.util;

import android.app.Activity;
import android.widget.Toast;
import org.json.JSONException;
import java.util.Calendar;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class TestPayUtils {

    public String validateCardDetails(Card card) {
        String validationMessage = null;

        if(!card.isValid()){
            validationMessage = "Card is invalid";
        } else if (!card.validNumber()){
            validationMessage = "Card number is invalid";
        } else if (!card.validCVC()){
            validationMessage = "Card CVC is invalid";
        } else if (!card.validExpiryDate()){
            validationMessage = "Card expiry date is invalid";
        }
        return validationMessage;
    }

    public Charge generateChargeDetails(Card card, String accessCode, String email, int amount) {
        Charge charge = new Charge();
        charge.setCard(card);
        charge.setEmail(email); //email address
        charge.setAmount(amount); //test amount
        charge.setReference("ChargedFromAndroid_" + Calendar.getInstance().getTimeInMillis());
        if (accessCode != null) {
            charge.setAccessCode(accessCode); // null : this will be null except we start this transaction from our backend
        }
        try {
            charge.putCustomField("Charged From", "Android SDK");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return charge;
    }

    public void generateErrorToast (Activity activity, String message){
        Toast.makeText(activity, message, Toast.LENGTH_LONG)
                .show();
    }
}
