package com.kreuterkeule.ShoppingCartPaypal.entity;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ShoppingCart {

    private List<Product> products;

    public Double getShoppingCartValue() {
        Double value = 0D;
        for (Product product : products) {
            value += product.getPrice();
        }

        return round(value, 2);
    }

    private Double round(Double value, int places) {
        if(places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public Map<String, Integer> getProductsAndTheirCount() {
        Map<String, Integer> productsAndTheirCount = new HashMap<>();

        for (Product product : products) {
            if (productsAndTheirCount.containsKey(product.getName())) {
                productsAndTheirCount.put(product.getName(), productsAndTheirCount.get(product.getName()) + 1);
            } else {
                productsAndTheirCount.put(product.getName(), 1);
            }
        }

        return productsAndTheirCount;
    }


    public void addProduct(Product product) {

        products.add(product);

    }

    public void removeProduct(Long id) {

        for (Product product : products) {
            if (product.getId() == id) {
                products.remove(product);
                return;
            }
        }

        return;

    }
}
