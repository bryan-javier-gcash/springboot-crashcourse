package com.example.gcashtrainingspringboot.controller;

import com.example.gcashtrainingspringboot.model.Product;
import com.example.gcashtrainingspringboot.repository.ProductRepository;
import com.example.gcashtrainingspringboot.service.ProductService;
import com.example.gcashtrainingspringboot.service.ProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAll(){
        return productService.findAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductByID(@PathVariable Long id){
        return productService.findProductByID(id)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@Valid @RequestBody Product newProduct){
        return productService.saveProduct(newProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        boolean deleted = productService.deleteProduct(id);

        if(deleted){
            return ResponseEntity.noContent().build(); // 204 No Content
        }else{
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // @PatchMapping
    @PatchMapping("/{id}")
    public ResponseEntity<Product> patchProduct(@PathVariable Long id, @Valid @RequestBody Product patchData){
        return productService.patch(id, patchData).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    // @PutMapping
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product updatedProduct){
        return productService.update(id, updatedProduct).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
}
