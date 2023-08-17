package com.example.springbootelasticsearchexample.service;

import com.example.springbootelasticsearchexample.entity.Product;
import com.example.springbootelasticsearchexample.entity.ProductMysql;
import com.example.springbootelasticsearchexample.repo.ProductMysqlRepo;
import com.example.springbootelasticsearchexample.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final ProductMysqlRepo productMysqlRepo;

    public Iterable<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public ProductMysql insertProduct(ProductMysql product) {
        ProductMysql savedJpaProduct = productMysqlRepo.save(product);
        Product elasticsearchProduct = convertToElasticsearchProduct(savedJpaProduct);
        productRepo.save(elasticsearchProduct);
        return savedJpaProduct;
    }

    public ProductMysql updateProduct(ProductMysql product, int id) {
        Optional<ProductMysql> optionalProduct = productMysqlRepo.findById(id);
        ProductMysql updatedProduct = null;

        if (optionalProduct.isPresent()) {
            ProductMysql existingProduct = optionalProduct.get();
            updatedProduct = ProductMysql.builder()
                    .id(existingProduct.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .quantity(product.getQuantity())
                    .price(product.getPrice())
                    .build();

            productMysqlRepo.save(updatedProduct);
            Product elasticsearchProduct = convertToElasticsearchProduct(updatedProduct);
            productRepo.save(elasticsearchProduct);
            return updatedProduct;
        }
        throw new RuntimeException("Product with id " + id + " not found.");
    }


    public void deleteProduct(int id) {
        Optional<ProductMysql> optionalProductMysql = productMysqlRepo.findById(id);
        if (optionalProductMysql.isPresent()) {
            productMysqlRepo.deleteById(id);
            productRepo.deleteById(id);
        } else {
            throw new RuntimeException("Product with id " + id + " not found.");
        }
    }

    private Product convertToElasticsearchProduct(ProductMysql producyMysql) {
        Product product = new Product();
        product.setId(producyMysql.getId());
        product.setName(producyMysql.getName());
        product.setDescription(producyMysql.getDescription());
        product.setQuantity(producyMysql.getQuantity());
        product.setPrice(producyMysql.getPrice());
        return product;
    }
}
