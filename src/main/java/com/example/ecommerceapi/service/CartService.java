package com.example.ecommerceapi.service;

import com.example.ecommerceapi.model.*;
import com.example.ecommerceapi.repository.CartRepository;
import com.example.ecommerceapi.repository.ProductRepository;
import com.example.ecommerceapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    public Cart getCartByUser (User user) {
        return this.cartRepository.findByUser (user).orElseGet (() -> {
            Cart cart = new Cart ();
            cart.setUser (user);
            cart.setItems (new ArrayList<>());
            cart.setTotalPrice (BigDecimal.ZERO);
            return this.cartRepository.save (cart);
        });
    }

    public Cart addItemToCart (User user, Long productId, int quantity) {
        Cart cart = getCartByUser (user);
        Product product = this.productRepository.findById (productId).orElseThrow (() -> new RuntimeException ("Product not found"));

        Optional <CartItem> existingItem = cart.getItems ().stream ()
                .filter (item -> item.getProduct ().getId ().equals (productId))
                .findFirst ();

        if (existingItem.isPresent ()) {
            CartItem item = existingItem.get ();
            item.setQuantity (item.getQuantity () + quantity);
        } else {
            CartItem newItem = new CartItem ();
            newItem.setProduct (product);
            newItem.setQuantity (quantity);
            cart.getItems ().add (newItem);
        }

        updateCartTotal (cart);
        return this.cartRepository.save (cart);
    }

    public Cart removeProductFromCart (User user, Long productId) {
        Cart cart = getCartByUser( user);
        cart.getItems ().removeIf (item -> item.getProduct ().getId ().equals (productId));
        updateCartTotal (cart);
        return this.cartRepository.save (cart);
    }

    public Cart clearCart (User user) {
        Cart cart = getCartByUser (user);
        cart.getItems ().clear ();
        cart.setTotalPrice (BigDecimal.ZERO);
        return this.cartRepository.save (cart);
    }

    private void updateCartTotal (Cart cart) {
        BigDecimal total = cart.getItems ().stream ()
                .map (item -> item.getProduct ().getPrice ().multiply (BigDecimal.valueOf (item.getQuantity ())))
                .reduce (BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice (total);
    }
}
