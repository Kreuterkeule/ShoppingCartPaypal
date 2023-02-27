package com.kreuterkeule.ShoppingCartPaypal.controller;

import com.kreuterkeule.ShoppingCartPaypal.entity.Product;
import com.kreuterkeule.ShoppingCartPaypal.repository.ProductRepo;
import com.kreuterkeule.ShoppingCartPaypal.service.PaypalPayService;
import com.kreuterkeule.ShoppingCartPaypal.service.ShoppingCartService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private ProductRepo productRepo;


    @Autowired
    public ApiController(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping("/getProducts")
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(productRepo.findAll());
    }

    @PostMapping("/createProduct")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {

        return ResponseEntity.ok(productRepo.save(product));
    }

    @GetMapping("/deleteProduct")
    public ResponseEntity<Product> deleteProduct(@RequestParam Long id) {
        Product product = productRepo.findById(id).get();

        productRepo.deleteById(id);

        return ResponseEntity.ok(product);
    }

    @PostMapping("/updateProduct")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        Product oldProduct = productRepo.findById(product.getId()).get();

        oldProduct.setName(product.getName());
        oldProduct.setPrice(product.getPrice());

        return ResponseEntity.ok(productRepo.save(oldProduct));
    }

}
