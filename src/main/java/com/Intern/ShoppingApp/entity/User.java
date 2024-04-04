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
@Table(name = "USER_TABLE")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "USER_NAME", length = 75, nullable = false)
    private String userName;

    @Column(name = "USER_PHONE_NUMBER", length = 10,nullable = false, unique = true)
    private String userPhoneNumber;

    @Column(name = "USER_PASSWORD", nullable = false)
    private String userPassword;
}
