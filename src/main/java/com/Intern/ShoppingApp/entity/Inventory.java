package com.Intern.ShoppingApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "INVENTORY_TABLE")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INVENTORY_ID")
    private Long inventoryId;

    @OneToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @Column(name = "INVENTORY_ORDERED_QUANTITY")
    private Long productOrdered;

    @Column(name = "PRODUCT_PRICE")
    private Float productPrice;

    @Column(name = "INVENTORY_AVAILABLE_QUANTITY")
    private Long productAvailable;
}
