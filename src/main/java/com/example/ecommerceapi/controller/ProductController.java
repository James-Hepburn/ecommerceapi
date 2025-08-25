package com.example.ecommerceapi.controller;

import com.example.ecommerceapi.dto.ProductRequest;
import com.example.ecommerceapi.model.Product;
import com.example.ecommerceapi.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {
    private ProductService productService;

    @GetMapping
    public List <Product> getAllProducts () {
        return this.productService.getAllProducts ();
    }

    @GetMapping("/search")
    public List <Product> searchProducts (@RequestParam String keyword) {
        return this.productService.searchProducts (keyword);
    }

    @GetMapping("/{id}")
    public Product getProductById (@PathVariable Long id) {
        return this.productService.getProductById (id);
    }

    @PostMapping
    public Product addProduct (@RequestBody ProductRequest request) {
        return this.productService.addProduct (request);
    }

    @PutMapping("/{id}")
    public Product updateProduct (@PathVariable Long id, @RequestBody ProductRequest request) {
        return this.productService.updateProduct (id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct (@PathVariable Long id) {
        this.productService.deleteProduct (id);
    }
}
