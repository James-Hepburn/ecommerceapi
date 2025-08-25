package com.example.ecommerceapi.controller;

import com.example.ecommerceapi.dto.CheckoutRequest;
import com.example.ecommerceapi.model.Cart;
import com.example.ecommerceapi.model.Order;
import com.example.ecommerceapi.model.User;
import com.example.ecommerceapi.repository.UserRepository;
import com.example.ecommerceapi.service.PaymentService;
import com.example.ecommerceapi.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@AllArgsConstructor
public class CheckoutController {
    private PaymentService paymentService;
    private CartService cartService;
    private UserRepository userRepository;

    public User getUser () {
        Authentication authentication = SecurityContextHolder.getContext ().getAuthentication ();
        String username = authentication.getName ();
        User user = this.userRepository.findByUsername (username).orElseThrow (() -> new RuntimeException ("User not found"));
        return user;
    }

    @PostMapping("/payment")
    public String createPayment () {
        User user = getUser ();
        Cart cart = this.cartService.getCartByUser (user);
        return this.paymentService.createStripeCheckoutSession (cart);
    }

    @PostMapping("/complete")
    public Order completeCheckout () {
        User user = getUser ();
        Cart cart = this.cartService.getCartByUser (user);
        Order order = this.paymentService.createOrderFromCart (cart, user);
        this.cartService.clearCart (user);
        return order;
    }
}
