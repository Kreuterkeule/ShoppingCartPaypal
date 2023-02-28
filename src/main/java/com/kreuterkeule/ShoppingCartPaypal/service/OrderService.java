package com.kreuterkeule.ShoppingCartPaypal.service;

import com.kreuterkeule.ShoppingCartPaypal.entity.PaypalOrder;
import com.kreuterkeule.ShoppingCartPaypal.repository.OrderRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

// not yet important, cause orders are not yet captured and saved.
@Service
public class OrderService {

    private OrderRepo orderRepo;

    public OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Scheduled(cron = "@Hourly")
    public void deleteOld() {
        List<PaypalOrder> orders = orderRepo.findAll();

        for (PaypalOrder order : orders) {
            if (ChronoUnit.WEEKS.between(order.getCreationTime(), LocalDateTime.now()) > 2) {
                orderRepo.deleteById(order.getId());
            }
        }


    }

}
