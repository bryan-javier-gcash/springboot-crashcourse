package com.example.gcashtrainingspringboot.controller;

import com.example.gcashtrainingspringboot.config.MainConfiguration;
import com.example.gcashtrainingspringboot.model.Product;
import com.example.gcashtrainingspringboot.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({ProductController.class, MainConfiguration.class})
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void testGetAllProducts() throws Exception{
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());

        List<Product> allProducts = Arrays.asList(
                new Product(1L, "Keyboard", 150.0),
                new Product(2L, "Mouse", 50.0),
                new Product(3L, "Monitor", 200.0),
                new Product(4L, "Mouse", 220.0)
        );

        List<Product> mockProductsForPage = Arrays.asList(
               new Product(1L, "Keyboard", 150.0),
               new Product(2L, "Mouse", 50.0)
        );

       Page<Product> mockPageProducts = new PageImpl<>(mockProductsForPage, pageable, allProducts.size());

       when(productService.findAllProducts(pageable)).thenReturn(mockPageProducts);

        mockMvc.perform(get("/products").param("page", "0").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Keyboard"))
                .andExpect(jsonPath("$[0].price").value("150.0"));

    }

    @Test
    void testGetProductByID_found() throws Exception{
        Product p1 = new Product(1L, "Laptop", 1299.50);
        when(productService.findProductByID(1L)).thenReturn(Optional.of(p1));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void testGetProductByID_notFound() throws Exception{
        when(productService.findProductByID(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProduct() throws Exception{
        Product savedProduct = new Product(2L, "Mouse", 25.50);

        when(productService.saveProduct(any(Product.class))).thenReturn(savedProduct);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Mouse\",\"price\":25.50}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Mouse"));
    }

    @Test
    void testUpdateProduct_found() throws Exception{
        Product updatedProduct = new Product(1L, "Apple Macbook", 1500.00);
        when(productService.update(eq(1L), any(Product.class))).thenReturn(Optional.of(updatedProduct));

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Laptop Pro\",\"price\":1500.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple Macbook"))
                .andExpect(jsonPath("$.price").value(1500.00));
    }

    @Test
    void testPatchProduct_found() throws Exception {
        Product patchedProduct = new Product(1L, "Laptop Ultra", 1600.00);
        when(productService.patch(eq(1L), any(Product.class))).thenReturn(Optional.of(patchedProduct));

        mockMvc.perform(patch("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Laptop Ultra\",\"price\":1600.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop Ultra"))
                .andExpect(jsonPath("$.price").value(1600.00));
    }

    @Test
    void testPatchProduct_notFound() throws Exception {
        when(productService.patch(eq(2L), any(Product.class))).thenReturn(Optional.empty());

        mockMvc.perform(patch("/products/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Tablet\",\"price\":300.00}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProduct() throws Exception{
        when(productService.deleteProduct(1L)).thenReturn(true);

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProduct_notFound() throws Exception{
        when(productService.deleteProduct(2L)).thenReturn(false);
        mockMvc.perform(delete("/products/2"))
                .andExpect(status().isNotFound());
    }

}