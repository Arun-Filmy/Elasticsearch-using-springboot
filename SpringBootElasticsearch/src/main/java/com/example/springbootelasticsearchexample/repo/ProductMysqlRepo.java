package com.example.springbootelasticsearchexample.repo;

import com.example.springbootelasticsearchexample.entity.Product;
import com.example.springbootelasticsearchexample.entity.ProductMysql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMysqlRepo extends JpaRepository<ProductMysql, Integer> {

}
