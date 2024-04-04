package com.Intern.ShoppingApp.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderAmountResponse {
    private Long orderId;
    private Long userId;
    private Long quantity;
    private Float orderAmount;
    private String coupon;
}
