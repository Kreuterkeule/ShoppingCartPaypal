package com.kreuterkeule.ShoppingCartPaypal.controller;

import com.kreuterkeule.ShoppingCartPaypal.entity.Product;
import com.kreuterkeule.ShoppingCartPaypal.repository.ProductRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * This test also tests the productRepo and its service, so I did not make special tests for them!
 *
 */


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductDatabaseApiControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepo productRepo;

    @LocalServerPort
    private Integer serverPort;

    private Long productId;
    private String productName;

    @BeforeEach
    // this is also testing the function to create
    public void createProductTest() {


        record RequestDTO(String name, Double price) {}

        ResponseEntity<Product> response = restTemplate.postForEntity(
                "http://localhost:" + serverPort + "/api/createProduct",
                new RequestDTO("test", 12.2D),
                Product.class);

        Product product = response.getBody();

        productId = product.getId();
        productName = product.getName();

        assertThat(response.getStatusCode().equals(200));
        assertThat(product.getName().equals("test"));
        assertThat(product.getPrice().equals(12.2D));
    }

    @Test
    public void updateProductTest() {

        System.out.println(productId);

        record RequestDTO(Long id, String name, Double price) {}

        ResponseEntity<Product> response = restTemplate.postForEntity(
                "http://localhost:" + serverPort + "/api/updateProduct",
                new RequestDTO(productId, "test_updated", 12.3D),
                Product.class);

        Product product = response.getBody();

        productId = product.getId();
        productName = product.getName();

        assertThat(response.getStatusCode().equals(200));
        assertThat(product.getName().equals("test_updated"));
        assertThat(product.getPrice().equals(12.3D));
    }

    @AfterEach
    // this is also testing the function to delete. and it's there, to not damage the database.
    public void deleteProductTest() {

        ResponseEntity<Product> response = restTemplate.getForEntity(
                "http://localhost:" + serverPort + "/api/deleteProduct?id=" + productId,
                Product.class);

        Product product = response.getBody();

        assertThat(product.getName().equals(productName));
        assertThat(product.getId().equals(productId));

        Optional<Product> deletedProduct = productRepo.findById(productId);

        assertThat(deletedProduct.equals(null));

    }

    @Test
    public void getAllProductsTest() {
        List<Product> productListFromRepo = productRepo.findAll();

        List<Product> productListFromRequest = restTemplate.getForObject("http://localhost:" + serverPort + "/api/getProducts", ArrayList.class);

        assertThat(productListFromRepo.equals(productListFromRequest));

    }

}
