package com.example.test_pay.repository;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import com.example.test_pay.service.ServiceBuilder;
import com.example.test_pay.service.TestPayService;
import com.example.test_pay.util.TestPayUtils;
import java.io.IOException;
import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
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

    public void initializeTransaction() {

        Call<String> call = testPayService.initializeTransaction();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String accessCode = response.body();
                Toast.makeText(activity, accessCode, Toast.LENGTH_LONG)
                        .show();

                Card card = new Card("5060666666666666666", 11, 21, "123");
                String cardDetails = testPayUtils.validateCardDetails(card);
                Log.d(TAG, "onResponse: cardDetails " + cardDetails);

                String cardType = card.getType();
                Log.d(TAG, "onResponse: cardType " + cardType);

                Charge chargeDetails = testPayUtils.generateChargeDetails(card, accessCode, null, 0);
                performCharge(chargeDetails);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                generateErrorToast(t.getMessage());
            }
        });

    }

    private void performCharge(Charge chargeDetails) {
        PaystackSdk.chargeCard(activity, chargeDetails, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                // This is called only after transaction is deemed successful.
                // Retrieve the transaction, and send its reference to your server
                // for verification.
                String paymentReference = transaction.getReference();
                Log.d(TAG, "onResponse: called: ");
                Log.d(TAG, "onResponse: paymentReference: " + paymentReference);
                verifyTransaction(paymentReference);
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
                if (error instanceof ExpiredAccessCodeException) {
                    Toast.makeText(activity, error.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                if (transaction.getReference() != null) {
                    Toast.makeText(activity, transaction.getReference() +
                            " concluded with error: " +
                            error.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void verifyTransaction(String reference) {

        Call<ResponseBody> call = testPayService.verifyTransaction(reference);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                assert response.body() != null;
                try {
                    String ref = (response.body().string());
                    Log.d(TAG, "onResponse: Transaction Successful! payment reference: " + ref);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onResponse: Transaction Successful! payment reference: " + t.getMessage());
            }
        });
    }

    public void initiateCardCharge(Card card, String email, int amount) {
//        Card card = new Card("5060666666666666666", 11, 21, "123");
        String validationResult = testPayUtils.validateCardDetails(card);
        if (validationResult != null) {
            generateErrorToast(validationResult);
            Log.d(TAG, "onResponse: validationResult " + validationResult);
            return;
        }

        String cardType = card.getType();
        Log.d(TAG, "onResponse: cardType " + cardType);

        Charge chargeDetails = testPayUtils.generateChargeDetails(card, null, email, amount);
        performCharge(chargeDetails);
    }

    private void generateErrorToast (String message){
        Toast.makeText(activity, message, Toast.LENGTH_LONG)
                .show();
    }
}
