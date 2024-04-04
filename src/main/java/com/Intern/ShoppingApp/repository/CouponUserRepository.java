package com.Intern.ShoppingApp.repository;

import com.Intern.ShoppingApp.entity.CouponUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponUserRepository extends JpaRepository<CouponUser, Long> {

    @Query("SELECT cu FROM CouponUser cu WHERE cu.user.userId = :userId AND cu.coupon.couponId = :couponId")
    CouponUser findByUserIdAndCouponId(Long userId, Long couponId);
}
