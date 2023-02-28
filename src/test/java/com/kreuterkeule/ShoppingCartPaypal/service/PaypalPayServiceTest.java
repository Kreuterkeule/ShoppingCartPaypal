package com.kreuterkeule.ShoppingCartPaypal.service;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PaypalPayServiceTest {

    @Autowired
    private PaypalPayService paypalService;

    @Test
    public void testIfPaypalApiIsReachable() throws PayPalRESTException {
        Payment payment = paypalService.generatePayment(
                10D,
                "USD",
                "paypal",
                "sale",
                "Testing Paypal API",
                "example.com",
                "example.com"
        );

        assertThat( payment.getState().equals("created") );

    }
}
