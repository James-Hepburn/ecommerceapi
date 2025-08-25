package com.example.ecommerceapi.controller;

import com.example.ecommerceapi.model.Cart;
import com.example.ecommerceapi.model.User;
import com.example.ecommerceapi.repository.UserRepository;
import com.example.ecommerceapi.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {
    private CartService cartService;
    private UserRepository userRepository;

    public User getUser () {
        Authentication authentication = SecurityContextHolder.getContext ().getAuthentication ();
        String username = authentication.getName ();
        User user = this.userRepository.findByUsername (username).orElseThrow (() -> new RuntimeException ("User not found"));
        return user;
    }

    @GetMapping
    public Cart getCart () {
        User user = getUser ();
        return this.cartService.getCartByUser (user);
    }

    @PostMapping("/add/{productId}")
    public Cart addToCart (@PathVariable Long productId, @RequestParam int quantity) {
        User user = getUser ();
        return this.cartService.addItemToCart (user, productId, quantity);
    }

    @PostMapping("/remove/{productId}")
    public Cart removeFromCart (@PathVariable Long productId) {
        User user = getUser ();
        return this.cartService.removeProductFromCart (user, productId);
    }

    @DeleteMapping("/clear")
    public void clearCart () {
        User user = getUser ();
        this.cartService.clearCart (user);
    }
}
