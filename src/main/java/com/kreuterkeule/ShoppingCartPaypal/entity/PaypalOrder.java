package com.kreuterkeule.ShoppingCartPaypal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

// not jet needed, but will be needed, when orders will be captured and admins can see which orders are to be delivered.
@Table(name = "paypalorder")
@Data
@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class PaypalOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String paypalorder;
    @CreationTimestamp
    LocalDateTime creationTime;
}
