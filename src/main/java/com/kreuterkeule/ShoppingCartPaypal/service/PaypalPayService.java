package com.kreuterkeule.ShoppingCartPaypal.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class PaypalPayService {

    @Autowired
    APIContext apiContext;

    public Payment generatePayment(
            Double total,
            String currency,
            String payMethod,
            String intent,
            String description,
            String cancelUrl,
            String successUrl
    ) throws PayPalRESTException {

        Amount amount = new Amount();
        amount.setCurrency(currency);

        // make every number into a number with two floating point numbers
        // example: 2.2 => 2.20; 30.537 => 30.54
        // why? this is required from PayPal REST api, otherwise you would get a VALIDATION_ERROR
        total = new BigDecimal(total).setScale(2, RoundingMode.CEILING).doubleValue();

        amount.setTotal(total.toString());

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(payMethod);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(successUrl);
        redirectUrls.setCancelUrl(cancelUrl);

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactionList);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {

        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        System.out.println("executing payment '" + paymentId + "'");

        return payment.execute(apiContext, paymentExecution);

    }


    //TODO: not quite working jet - needs a fix - probably it's cause I'm just generating a payment, not an order.
    //TODO: Getting error RESOURCE_NOT_FOUND from paypal api - needs further research.
    public void capturePayment(String paymentId) throws IOException {
        URL url = new URL("https://api-m.sandbox.paypal.com/v2/payments/authorizations/" + paymentId + "/capture");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Authorization", apiContext.getAccessToken());

        // not needed
        //httpConn.setRequestProperty("PayPal-Request-Id", "123e4567-e89b-12d3-a456-426655440010");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        //not needed and producing errors
        //writer.write("");
        //writer.flush();
        //writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        System.out.println(response);
    }
}
