package com.kreuterkeule.ShoppingCartPaypal.repository;

import com.kreuterkeule.ShoppingCartPaypal.entity.PaypalOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends CrudRepository<PaypalOrder, Long> {

    List<PaypalOrder> findAll();

}
