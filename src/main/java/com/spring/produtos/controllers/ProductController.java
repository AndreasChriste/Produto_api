package com.spring.produtos.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


import com.spring.produtos.dtos.ProductRecordDto;
import com.spring.produtos.model.ProductModel;
import com.spring.produtos.repositories.ProductRepository;
import com.spring.produtos.services.ProductService;

import jakarta.validation.Valid;

@RestController
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ProductRepository productRepository;
	
	
	@PostMapping("/product")
	public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto){
		var productModel = new ProductModel();
		BeanUtils.copyProperties(productRecordDto, productModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productModel));
	}
	
	
	@GetMapping("/product")
	public ResponseEntity<List<ProductModel>> getAllProduct(){
		List<ProductModel> productList = productService.findAll();
		if(!productList.isEmpty()) {
			for( ProductModel product: productList) {
				UUID id = product.getIdProduct();
				product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(productList);
	}
	
	@GetMapping("/product/{id}")
	public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id){
		Optional<ProductModel> productO = productService.findById(id);
		if(productO.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
		}
		productO.get().add(linkTo(methodOn(ProductController.class).getAllProduct()).withRel("Product List"));
		
		return ResponseEntity.status(HttpStatus.OK).body(productO.get());
	}
	
	@PutMapping("/product/{id}")
	public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id, @RequestBody @Valid ProductRecordDto productRecordDto){
		Optional<ProductModel> productO = productService.findById(id);
		if(productO.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
		}
		var productModel = productO.get();
		BeanUtils.copyProperties(productRecordDto, productModel);
		return ResponseEntity.status(HttpStatus.OK).body(productService.save(productModel));
	}
	
	@DeleteMapping("/product/{id}")
	public ResponseEntity<Object> deleteOneProduct(@PathVariable(value = "id") UUID id){
		Optional<ProductModel> productO = productRepository.findById(id);
		if(productO.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
		}
		productService.deleteProduct(productO.get());
		return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
	
	}
	
}
