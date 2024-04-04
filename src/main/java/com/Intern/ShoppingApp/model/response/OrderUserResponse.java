package com.Intern.ShoppingApp.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderUserResponse {
    private Long orderId;
    private Float orderAmount;
    private Date orderDate;
    private String coupon;
}
