package com.Intern.ShoppingApp.config;

import com.Intern.ShoppingApp.entity.Coupon;
import com.Intern.ShoppingApp.entity.Inventory;
import com.Intern.ShoppingApp.entity.Product;
import com.Intern.ShoppingApp.entity.User;
import com.Intern.ShoppingApp.repository.CouponRepository;
import com.Intern.ShoppingApp.repository.InventoryRepository;
import com.Intern.ShoppingApp.repository.ProductRepository;
import com.Intern.ShoppingApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final CouponRepository couponRepository;
    private boolean alreadyInitialized = false;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!alreadyInitialized) {
            initializeData();
            alreadyInitialized = true;
        }
    }

    private void initializeData() {
        if (userRepository.count() == 0 && productRepository.count() == 0 && inventoryRepository.count() == 0
                && couponRepository.count() == 0) {

            User user = new User();
            user.setUserName("VASU");
            user.setUserPassword("VASU123@");
            user.setUserPhoneNumber("7300801153");
            userRepository.save(user);

            Product product = new Product();
            product.setProductName("BOURBON");
            product = productRepository.save(product);

            Inventory inventory = new Inventory();
            inventory.setProduct(product);
            inventory.setProductOrdered(0L);
            inventory.setProductPrice(100f);
            inventory.setProductAvailable(100L);
            inventoryRepository.save(inventory);

            saveCoupon("OFF5", 5F);
            saveCoupon("OFF10", 10F);
        }
    }

    private void saveCoupon(String couponName, Float discount) {
        Coupon coupon = new Coupon();
        coupon.setCouponName(couponName);
        coupon.setDiscount(discount);
        couponRepository.save(coupon);
    }
}
