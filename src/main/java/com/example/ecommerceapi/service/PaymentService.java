package com.example.ecommerceapi.service;

import com.example.ecommerceapi.model.*;
import com.example.ecommerceapi.repository.CartRepository;
import com.example.ecommerceapi.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentService {
    private CartService cartService;
    private OrderRepository orderRepository;
    private CartRepository cartRepository;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init () {
        Stripe.apiKey = this.stripeApiKey;
    }

    public PaymentIntent createPaymentIntent (User user, Cart cart) throws StripeException {
        BigDecimal amount = cart.getTotalPrice ();
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder ()
                .setAmount (amount.multiply (BigDecimal.valueOf (100)).longValue ())
                .setCurrency ("usd")
                .addPaymentMethodType ("card")
                .build ();
        return PaymentIntent.create (params);
    }

    public Order createOrderFromCart (Cart cart, User user) {
        Order order = new Order ();
        order.setUser (user);

        List <OrderItem> items = new ArrayList<>();

        for (CartItem cartItem : cart.getItems ()) {
            OrderItem orderItem = new OrderItem ();
            orderItem.setProduct (cartItem.getProduct ());
            orderItem.setQuantity (cartItem.getQuantity ());
            orderItem.setPrice (cartItem.getProduct ().getPrice ());
            orderItem.setOrder (order);
            items.add (orderItem);
        }

        order.setItems (items);
        order.setTotalPrice (cart.getTotalPrice ());

        cart.getItems ().clear ();
        cart.setTotalPrice (BigDecimal.ZERO);

        this.cartRepository.save (cart);
        return this.orderRepository.save (order);
    }

    public String createStripeCheckoutSession (Cart cart) {
        return "stripe-session-id-placeholder";
    }
}
