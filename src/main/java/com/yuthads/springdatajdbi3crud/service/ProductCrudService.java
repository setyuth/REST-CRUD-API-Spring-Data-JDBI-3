package com.yuthads.springdatajdbi3crud.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.yuthads.springdatajdbi3crud.entities.Product;

public interface ProductCrudService {

	Product create(Product product);

	List<Product> gdtAllProducts();

	Optional<Product> getProductById(long id);

	int updateProductById(@Valid Product product);

	int deleteProductById(long id);
	
}
