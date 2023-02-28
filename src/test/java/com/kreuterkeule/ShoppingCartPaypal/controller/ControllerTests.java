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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ControllerTests {

    @Value(value = "${local.server.port}")
    private int port;

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
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/paypal/cancel", String.class).contains("<!DOCTYPE html>"));
    }
}
