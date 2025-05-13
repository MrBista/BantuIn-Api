package com.exercie.exercies.controller;

import com.exercie.exercies.dto.request.ProductDtoReq;
import com.exercie.exercies.dto.response.CommonResponse;
import com.exercie.exercies.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllProduct(){
        System.out.println("masuk sini");

        return CommonResponse
                .generateResponse(productService.getAllProduct(),
                        "Successfully get all data",
                        HttpStatus.OK
                );
    }
    @PostMapping()
    public ResponseEntity<?> saveProduct(@RequestBody ProductDtoReq productDtoReq){

        return CommonResponse
                .generateResponse(productService.saveProduct(productDtoReq), "Successfully save product", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long productId){
        productService.deleteProduct(productId);
        return CommonResponse
                .generateResponse(true, "Successfully delete product", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDtoReq productDtoReq, @PathVariable("id") Long id){
        return CommonResponse
                .generateResponse(productService.updateProduct(productDtoReq ,id), "successfully update product", HttpStatus.OK);
    }
}
