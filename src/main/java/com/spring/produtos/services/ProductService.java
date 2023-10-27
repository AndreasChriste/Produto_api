package com.spring.produtos.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.produtos.model.ProductModel;
import com.spring.produtos.repositories.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	ProductRepository productRepository;
	
	public ProductModel save(ProductModel productModel) {
		 return productRepository.save(productModel);
	}
	
	public List<ProductModel> findAll(){
		return productRepository.findAll();
	}
	
	public Optional<ProductModel> findById(UUID id) {
		return productRepository.findById(id);
	}
	
	public ProductModel updateProduct(ProductModel productModel) {
		return productRepository.save(productModel);
	}
	
	public void deleteProduct(ProductModel productModel) {
		productRepository.delete(productModel);
	}
}
