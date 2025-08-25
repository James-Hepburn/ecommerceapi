package com.example.ecommerceapi.service;

import com.example.ecommerceapi.model.Cart;
import com.example.ecommerceapi.model.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class PaymentService {
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
}
