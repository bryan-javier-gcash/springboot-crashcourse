package com.example.gcashtrainingspringboot.controller;

import com.example.gcashtrainingspringboot.dto.ProductRequest;
import com.example.gcashtrainingspringboot.dto.ProductResponse;
import com.example.gcashtrainingspringboot.model.Product;
import com.example.gcashtrainingspringboot.repository.ProductRepository;
import com.example.gcashtrainingspringboot.service.ProductService;
import com.example.gcashtrainingspringboot.service.ProductServiceImpl;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ModelMapper modelMapper;

    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<ProductResponse> getAll(
            @RequestParam int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "asc") String sortBy
            ){
        Pageable pageable;

        if(sortBy.equalsIgnoreCase("desc")){
            pageable = PageRequest.of(page, size, Sort.by("id").descending());
        } else {
            // default sort if null or not equal to desc
            pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        }

        return modelMapper.map(productService.findAllProducts(pageable).getContent(), List.class);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductByID(@PathVariable Long id){
        return productService.findProductByID(id)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest newProduct){
        Product product =  productService.saveProduct(modelMapper.map(newProduct, Product.class));
        return modelMapper.map(product, ProductResponse.class);
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

    @PatchMapping("/{id}")
    public ResponseEntity<Product> patchProduct(@PathVariable Long id, @Valid @RequestBody Product patchData){
        return productService.patch(id, patchData).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest updatedProductRequest){
        Product updatedProduct = modelMapper.map(updatedProductRequest, Product.class);
        return productService.update(id, updatedProduct).map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build()) ;
    }

    @GetMapping("/search")
    public Page<Product> productSearch(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "asc") String sortBy,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {
        Pageable pageable;
        if (sortBy.equalsIgnoreCase("desc")){
            pageable = PageRequest.of(page, size, Sort.by("id").descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        }

        return productService.findByNameContainingIgnoreCase(searchKeyword, pageable);
    }
}
