package com.Intern.ShoppingApp.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentSuccessResponse {
    private Long userId;
    private Long orderId;
    private String transactionId;
    private String paymentStatus;
}
