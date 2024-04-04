package com.Intern.ShoppingApp.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InventoryResponse {
    private Long productOrdered;
    private Float productPrice;
    private Long productAvailable;
}
