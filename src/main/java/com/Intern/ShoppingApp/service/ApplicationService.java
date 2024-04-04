package com.Intern.ShoppingApp.service;

import com.Intern.ShoppingApp.error.CustomException;
import com.Intern.ShoppingApp.model.*;
import com.Intern.ShoppingApp.model.request.CouponRequest;
import com.Intern.ShoppingApp.model.request.UserRequest;

import java.util.List;

public interface ApplicationService {
    ResponseModel getInventoryData();

    ResponseModel addCoupons(List<CouponRequest> coupons);

    ResponseModel fetchCoupons();

    ResponseModel createNewUser(UserRequest request);

    ResponseModel placeOrder(Long userId, Long qty, String coupon) throws CustomException;

    ResponseModel orderPayment(Long userId, Long orderId, Float amount) throws CustomException;

    ResponseModel getOrdersByUserId(Long userId) throws CustomException;

    ResponseModel getOrderByIds(Long userId, Long orderId) throws CustomException;
}
