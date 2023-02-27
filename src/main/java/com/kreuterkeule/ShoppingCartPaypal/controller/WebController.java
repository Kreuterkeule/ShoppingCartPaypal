package com.kreuterkeule.ShoppingCartPaypal.controller;

import com.kreuterkeule.ShoppingCartPaypal.entity.Product;
import com.kreuterkeule.ShoppingCartPaypal.entity.ShoppingCart;
import com.kreuterkeule.ShoppingCartPaypal.repository.ProductRepo;
import com.kreuterkeule.ShoppingCartPaypal.service.IdService;
import com.kreuterkeule.ShoppingCartPaypal.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {

    private IdService idService;
    private ShoppingCartService cartService;
    private ProductRepo productRepo;

    @Autowired
    public WebController(IdService idService, ShoppingCartService cartService, ProductRepo productRepo) {
        this.idService = idService;
        this.cartService = cartService;
        this.productRepo = productRepo;
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

}
