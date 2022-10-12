package com.yuthads.springdatajdbi3crud.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yuthads.springdatajdbi3crud.entities.Product;
import com.yuthads.springdatajdbi3crud.service.ProductCrudService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.concurrent.TimeUnit.SECONDS;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Slf4j
public class ProductCrudController {

	
	private final ProductCrudService productService;
	
	@PostMapping()
	@CacheEvict(value="product", allEntries = true)
	public ResponseEntity<Product> createProduct (@RequestBody @Valid Product product) {
		
		log.info("Rest request to save product: {}", product);
		
		return new ResponseEntity<>(productService.create(product), HttpStatus.CREATED);
		
	}
	
	@GetMapping()
	public ResponseEntity<List<Product>> getAllProducts() {
		
		sleep(2);
		
		log.info("Rest request to get all products");
		
		List<Product> list = productService.gdtAllProducts();
		
		return new ResponseEntity<>(list, HttpStatus.OK);
		
	}
	
	private void sleep(int second) {

		try {
			SECONDS.sleep(second);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@GetMapping("/{id}")
	//@Cacheable(value = "product", key = "#id")
	public ResponseEntity<Product> getProductById(@PathVariable("id") long id) {
		
		log.info("Rest request to get product by id : {}", id);
		
		Optional<Product> optProduct = this.productService.getProductById(id);
		
		return optProduct.map(T -> 
					new ResponseEntity<>(T, HttpStatus.OK))
					.orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
		
	}
	
	@PutMapping()
	public ResponseEntity<String> updateProductById(@RequestBody @Valid Product product) {
		
		log.info("Rest request to update product by id : {}", product.getId());
		
		Optional<Product> opt = this.productService.getProductById(product.getId());
		
		opt.ifPresent(T -> this.productService.updateProductById(product));
		
		return opt.map(T -> new ResponseEntity<>("Product with id "+T.getId()+" was updated.", HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND));
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProductById(@PathVariable("id") long id) {
		
		log.info("Rest request to delete product from id : {}", id);
		
		Optional<Product> opt = this.productService.getProductById(id);
		
		opt.ifPresent(T -> this.productService.deleteProductById(id));
		
		return opt.map(T -> new ResponseEntity<>("Product with id "+T.getId()+" was deleted.", HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND));
	}
	
}
