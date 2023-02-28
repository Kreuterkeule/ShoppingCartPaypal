package com.kreuterkeule.ShoppingCartPaypal.controller;

import com.kreuterkeule.ShoppingCartPaypal.entity.Product;
import com.kreuterkeule.ShoppingCartPaypal.repository.ProductRepo;
import com.kreuterkeule.ShoppingCartPaypal.service.PaypalPayService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTests {

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    PaypalPayService paypalService;

    @Autowired
    private APIContext apiContext;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepo productRepo;

    @Test
    public void testIfROOTLoadsAndProductsArePresent() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/", String.class)).contains("<!DOCTYPE html>");
        System.out.println(this.restTemplate.getForObject("http://localhost:" + port + "/", String.class));
        for (Product product : productRepo.findAll()) {
            assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/", String.class)).contains(product.getName());
        }
    }

    @Test
    public void testIfSuccessAndCancelUrlsAreReachable() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/paypal/success", String.class).contains("<!DOCTYPE html>"));
    }

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

        Boolean approvalUrlFound = false;

        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                approvalUrlFound = true;
            }
        }

        assertTrue(approvalUrlFound);

    }
}
