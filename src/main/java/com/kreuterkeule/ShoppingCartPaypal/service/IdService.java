package com.kreuterkeule.ShoppingCartPaypal.service;

import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class IdService {

    List<Integer> ids = new ArrayList<>();

    public Integer getNewId() {
        Integer upperBound = 9000000; //max sessions

        Integer id = 0;

        Boolean uniqueIdFound = false;

        while (!uniqueIdFound) {
            Integer candidate = new Random().nextInt(upperBound);
            if (!ids.contains(candidate)) {
                id = candidate;
                uniqueIdFound = true;
            }
        }



        return id;
    }

    public void sessionKilled(Integer id) {
        ids.remove(id);
    }

}
