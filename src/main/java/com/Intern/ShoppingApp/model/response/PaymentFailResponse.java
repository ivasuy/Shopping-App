package com.Intern.ShoppingApp.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentFailResponse {
    private Long userId;
    private Long orderId;
    private String transactionId;
    private String paymentStatus;
    private String paymentDescription;
}
