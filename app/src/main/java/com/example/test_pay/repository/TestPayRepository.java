package com.example.test_pay.repository;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.test_pay.ConfirmationActivity;
import com.example.test_pay.service.ServiceBuilder;
import com.example.test_pay.service.TestPayService;
import com.example.test_pay.util.TestPayUtils;
import java.io.IOException;
import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestPayRepository {

    private static String TAG = TestPayRepository.class.getSimpleName();

    private Activity activity;

    private TestPayService testPayService;

    private TestPayUtils testPayUtils;

    public TestPayRepository(Activity activity) {
        this.activity = activity;
        testPayService = ServiceBuilder.buildService(TestPayService.class);
        testPayUtils = new TestPayUtils();
    }

    public void verifyTransaction(String reference, Charge chargeDetails, String cardType) {

        Call<ResponseBody> call = testPayService.verifyTransaction(reference);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                assert response.body() != null;
                try {
                    String ref = (response.body().string());
                    startConfirmationActivity(cardType, chargeDetails);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void initiateCardCharge(Card card, String email, int amount) {

        String validationResult = testPayUtils.validateCardDetails(card);

        if (validationResult != null) {
            testPayUtils.generateErrorToast(activity, validationResult);
            return;
        }

        String cardType = card.getType();

        Charge chargeDetails = testPayUtils.generateChargeDetails(card, null, email, amount);

        performCharge(chargeDetails, cardType);
    }

    private void performCharge(Charge chargeDetails, String cardType) {
        PaystackSdk.chargeCard(activity, chargeDetails, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                // This is called only after transaction is deemed successful.
                // Retrieve the transaction, and send its reference to your server
                // for verification.
                String paymentReference = transaction.getReference();

                verifyTransaction(paymentReference, chargeDetails, cardType);
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                // This is called only before requesting OTP.
                // Save reference so you may send to server. If
                // error occurs with OTP, you should still verify on server.
                Toast.makeText(activity, transaction.getReference(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                //handle error here
                if (transaction.getReference() != null) {
                    testPayUtils.generateErrorToast(activity, transaction.getReference() +
                            " concluded with error: " +
                            error.getMessage());
                } else {
                    testPayUtils.generateErrorToast(activity, error.getMessage());
                }
            }
        });
    }

    private void startConfirmationActivity(String cardType, Charge chargeDetails) {
        Bundle bundle = new Bundle();
        bundle.putString("cardType", cardType);
        bundle.putString("email", chargeDetails.getEmail());
        bundle.putString("amount", String.valueOf(chargeDetails.getAmount()));

        Intent intentConfirmationActivity = new Intent(activity, ConfirmationActivity.class);
        intentConfirmationActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentConfirmationActivity.putExtra("bundle", bundle);

        activity.getApplicationContext()
                .startActivity(intentConfirmationActivity);
    }
}
