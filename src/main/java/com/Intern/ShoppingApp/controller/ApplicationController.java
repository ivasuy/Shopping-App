package com.Intern.ShoppingApp.controller;

import com.Intern.ShoppingApp.error.CustomException;
import com.Intern.ShoppingApp.model.*;
import com.Intern.ShoppingApp.model.request.CouponRequest;
import com.Intern.ShoppingApp.model.request.UserRequest;
import com.Intern.ShoppingApp.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopping-app")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // GET /inventory - Retrieves inventory data
    @GetMapping("/inventory")
    public ResponseModel getInventoryData(){
        return applicationService.getInventoryData();
    }

    // POST /createCoupons - Adds coupons to the system
    @PostMapping("/createCoupons")
    public ResponseModel addCoupons(@RequestBody List<CouponRequest> coupons){
        return applicationService.addCoupons(coupons);
    }

    // GET /fetchCoupons - Retrieves available coupons
    @GetMapping("/fetchCoupons")
    public ResponseModel fetchCoupons(){
        return applicationService.fetchCoupons();
    }

    // POST /createNewUser - Creates a new user
    @PostMapping("/createNewUser")
    public ResponseModel createNewUser(@RequestBody UserRequest request){
        return applicationService.createNewUser(request);
    }

    // POST /{userId}/order - Places an order for a user
    @PostMapping("/{userId}/order")
    public ResponseModel placeOrder(
            @PathVariable Long userId,
            @RequestParam Long qty,
            @RequestParam String coupon) throws CustomException{
        return applicationService.placeOrder(userId, qty, coupon);
    }

    // POST /{userId}/{orderId}/pay - Processes payment for an order
    @PostMapping("/{userId}/{orderId}/pay")
    public ResponseModel orderPayment(
            @PathVariable Long userId,
            @PathVariable Long orderId,
            @RequestParam Float amount) throws CustomException{
        return applicationService.orderPayment(userId, orderId, amount);
    }

    // GET /{userId}/order - Retrieves orders for a specific user
    @GetMapping("/{userId}/order")
    public ResponseModel getOrdersByUserId(@PathVariable Long userId) throws CustomException {
        return applicationService.getOrdersByUserId(userId);
    }

    // GET /{userId}/orders/{orderId} - Retrieves a specific order by user and order ID
    @GetMapping("/{userId}/orders/{orderId}")
    public ResponseModel getOrderByIds(
            @PathVariable Long userId,
            @PathVariable Long orderId) throws CustomException{
        return applicationService.getOrderByIds(userId, orderId);
    }

}
