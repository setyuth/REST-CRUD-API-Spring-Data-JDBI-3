package com.yuthads.springdatajdbi3crud.repository;

import java.util.List;

import javax.validation.Valid;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.springframework.stereotype.Repository;

import com.yuthads.springdatajdbi3crud.entities.Product;

@Repository
@RegisterBeanMapper(Product.class)
public interface ProductCrudRepository {

	@Transaction
	@SqlUpdate("insert into jdbi3_crud.product (name, description, price, qty) values(:name, :description, :price, :qty);")
	@GetGeneratedKeys
	int insert(@BindBean Product product);

	@SqlQuery("select * from jdbi3_crud.product where id = :id;")
	Product getById(@Bind("id") long id);

	@SqlQuery("select * from jdbi3_crud.product;")
	List<Product> getAllProducts();

	
	@Transaction
	@SqlUpdate("update jdbi3_crud.product set name = :name, description = :description, price = :price, qty = :qty where id = :id;")
	int updateProductById(@BindBean @Valid Product product);

	@Transaction
	@SqlUpdate("delete from jdbi3_crud.product where id = :id;")
	int deleteProductById(long id);

}
