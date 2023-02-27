package com.kreuterkeule.ShoppingCartPaypal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


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
