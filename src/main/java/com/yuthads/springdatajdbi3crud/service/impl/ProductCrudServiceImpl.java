package com.yuthads.springdatajdbi3crud.service.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.yuthads.springdatajdbi3crud.entities.Product;
import com.yuthads.springdatajdbi3crud.repository.ProductCrudRepository;
import com.yuthads.springdatajdbi3crud.service.ProductCrudService;
import com.yuthads.springdatajdbi3crud.service.exception.DataNotFoundException;

@Service
public class ProductCrudServiceImpl implements ProductCrudService {

	private final ProductCrudRepository productRepository;
	
	public ProductCrudServiceImpl(Jdbi jdbi) {
		this.productRepository = jdbi.onDemand(ProductCrudRepository.class);
	}
	
	@Override
	public Product create(Product product) {
		return getById(productRepository.insert(product));
	}
	
	private Product getById(long id) {
		
		Product product = productRepository.getById(id);
		if(ObjectUtils.isEmpty(product)) {
			throw new DataNotFoundException(MessageFormat.format("Product id {0} not found.", String.valueOf(id)));
		}
		return product;
	}

	@Override
	public List<Product> gdtAllProducts() {
		return productRepository.getAllProducts();
	}

	@Override
	public Optional<Product> getProductById(long id) {
		return Optional.ofNullable(productRepository.getById(id));
	}

	@Override
	public int updateProductById(@Valid Product product) {
		return productRepository.updateProductById(product);
	}

	@Override
	public int deleteProductById(long id) {
		return productRepository.deleteProductById(id);
	}


}
