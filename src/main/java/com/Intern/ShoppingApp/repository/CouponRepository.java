package com.Intern.ShoppingApp.repository;

import com.Intern.ShoppingApp.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Coupon findByCouponName(String coupon);
}
