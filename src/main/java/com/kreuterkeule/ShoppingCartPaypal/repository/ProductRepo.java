package com.kreuterkeule.ShoppingCartPaypal.repository;

import com.kreuterkeule.ShoppingCartPaypal.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends CrudRepository<Product, Long> {

    List<Product> findAll();

}
