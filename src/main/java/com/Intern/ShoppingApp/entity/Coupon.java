package com.Intern.ShoppingApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "COUPON_TABLE")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COUPON_ID")
    private Long couponId;

    @Column(name = "COUPON_NAME", length = 15, nullable = false)
    private String couponName;

    @Column(name = "COUPON_DISCOUNT", length = 15, nullable = false)
    private Float discount;
}
