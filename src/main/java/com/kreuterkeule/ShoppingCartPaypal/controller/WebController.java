package com.kreuterkeule.ShoppingCartPaypal.controller;

import com.kreuterkeule.ShoppingCartPaypal.entity.Product;
import com.kreuterkeule.ShoppingCartPaypal.entity.ShoppingCart;
import com.kreuterkeule.ShoppingCartPaypal.repository.ProductRepo;
import com.kreuterkeule.ShoppingCartPaypal.service.IdService;
import com.kreuterkeule.ShoppingCartPaypal.service.PaypalPayService;
import com.kreuterkeule.ShoppingCartPaypal.service.ShoppingCartService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {

    private static final String SUCCESS_URL = "paypal/success";

    private static final String CANCEL_URL = "paypal/cancel";

    @Value("${server.port}")
    private String serverPort;

    private IdService idService;
    private ShoppingCartService cartService;
    private ProductRepo productRepo;
    private PaypalPayService paypalService;
    private ServletContext servletContext;
    private APIContext apiContext;
    @Autowired
    public WebController(IdService idService, ShoppingCartService cartService, ProductRepo productRepo, PaypalPayService paypalService, ServletContext servletContext, APIContext apiContext) {
        this.idService = idService;
        this.cartService = cartService;
        this.productRepo = productRepo;
        this.paypalService = paypalService;
        this.servletContext = servletContext;
        this.apiContext = apiContext;
    }

    @GetMapping("/")
    public String process(Model model, HttpSession session) {

        @SuppressWarnings("unchecked")
        Integer cartId = (Integer) session.getAttribute("shoppingCartId");

        ShoppingCart cart;

        if (cartId == null) {
            cartId = idService.getNewId();
            session.setAttribute("shoppingCartId", cartId);
            cart = new ShoppingCart(new ArrayList<>());
            cartService.addCart(cartId, cart);
            System.out.println("session with id '" + session.getAttribute("shoppingCartId") + "' was created");
        } else {
            System.out.println("session with id '" + session.getAttribute("shoppingCartId") + "' joined");
            cart = cartService.getCart(cartId);
        }

        Double value = cartService.getValue(cartId);
        Map<String, Integer> productsAndCounts = cart.getProductsAndTheirCount();
        List<Product> availableProducts = productRepo.findAll();

        model.addAttribute("available_products", availableProducts);
        model.addAttribute("cart", productsAndCounts);
        model.addAttribute("cart_value", value);

        return "index";

    }

    @GetMapping("addToCart")
    public String addToCart(@RequestParam("productId") Long id, HttpSession session) {

        Integer cartId = (Integer) session.getAttribute("shoppingCartId");

        if (cartId == null) {
            return "redirect:/";
        }

        ShoppingCart cart = cartService.getCart(cartId);

        Product product = productRepo.findById(id).get();

        if (product == null) {
            return "redirect:/";
        }

        cart.addProduct(product);

        return "redirect:/";


    }

    @GetMapping("removeFromCart")
    public String removeFromCart(@RequestParam("productId") Long id, HttpSession session) {
        ShoppingCart cart = cartService.getCart((Integer) session.getAttribute("shoppingCartId"));

        if (cart == null) {
            return "redirect:/";
        }

        cart.removeProduct(id);

        return "redirect:/";
    }

    @PostMapping("/destroy")
    public String destroySession(HttpServletRequest request) {
        cartService.removeCart((Integer) request.getSession().getAttribute("shoppingCartId"));
        request.getSession().invalidate();
        return "redirect:/";
    }

    @PostMapping("api/buy")
    public String checkout(HttpServletRequest request) throws PayPalRESTException, IOException {

        Integer cartId = (Integer) request.getSession().getAttribute("shoppingCartId");

        String description = "";

        for ( Map.Entry<String, Integer> productAndCount : cartService.getCart(cartId).getProductsAndTheirCount().entrySet()) {
            description += String.valueOf(productAndCount.getValue()) + "x " + productAndCount.getKey() + "\n";
        }

        Payment payment = paypalService.generatePayment(
                cartService.getValue(cartId),
                "USD",
                "paypal",
                "sale",
                description,
                "http://localhost:" + serverPort + servletContext.getContextPath() + "/" + CANCEL_URL,
                "http://localhost:" + serverPort + servletContext.getContextPath() + "/" + SUCCESS_URL
        );

        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                return "redirect:" +  link.getHref();
            }
        }

        return "redirect:/";
    }
    @GetMapping("paypal/success")
    public String getSuccessUrl(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("token") String token,
            @RequestParam("PayerID") String payerId,
            HttpServletRequest request) throws PayPalRESTException, IOException {

        Payment payment = paypalService.executePayment(paymentId, payerId);

        System.out.println("payment '" + paymentId + "' executed, State of payment: " + payment.getState() + ", Cart: " + payment.getCart());

        paypalService.capturePayment(payment.getId());

        return "success";
    }

    @GetMapping("paypal/cancel")
    public String getCancelUrl() {
        return "cancel";
    }
}
