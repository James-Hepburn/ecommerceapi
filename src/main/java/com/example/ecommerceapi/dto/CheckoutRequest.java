package com.example.ecommerceapi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {
    private Long cartId;
    private String paymentMethodId;
    private String shippingAddress;
}
