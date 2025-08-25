package com.example.ecommerceapi.service;

import com.example.ecommerceapi.dto.ProductRequest;
import com.example.ecommerceapi.model.Product;
import com.example.ecommerceapi.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;

    public List <Product> getAllProducts () {
        return this.productRepository.findAll ();
    }

    public List <Product> searchProducts (String keyword) {
        return this.productRepository.findByNameContaining (keyword);
    }

    public Product getProductById (Long id) {
        return this.productRepository.findById (id).orElseThrow (() -> new RuntimeException ("Product not found"));
    }

    public Product addProduct (ProductRequest request) {
        Product product = new Product (request.getName (), request.getDescription (), request.getPrice (), request.getQuantity ());
        return this.productRepository.save (product);
    }

    public Product updateProduct (Long id, ProductRequest request) {
        Product product = getProductById (id);
        product.setName (request.getName ());
        product.setDescription (request.getDescription ());
        product.setPrice (request.getPrice ());
        product.setQuantity (request.getQuantity ());
        return this.productRepository.save (product);
    }

    public void deleteProduct (Long id) {
        Product product = getProductById (id);
        this.productRepository.delete (product);
    }
}
