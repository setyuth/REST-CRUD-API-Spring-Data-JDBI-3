package com.yuthads.springdatajdbi3crud.entities;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	private Long id;
	
	@NotBlank
	@Size(max = 100)
	private String name;
	
	@Size(max = 255)
	private String description;
	
	private double price;
	
	private int qty;
	
}
