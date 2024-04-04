package com.Intern.ShoppingApp.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "COUPON_USER_TABLE")
public class CouponUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COUPON_USER_ID")
    private Long couponUserId;

    @ManyToOne
    @JoinColumn(name = "COUPON_ID")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
