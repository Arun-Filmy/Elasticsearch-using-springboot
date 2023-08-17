package com.example.springbootelasticsearchexample.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.springbootelasticsearchexample.entity.Product;
import com.example.springbootelasticsearchexample.entity.ProductMysql;
import com.example.springbootelasticsearchexample.service.ElasticSearchService;
import com.example.springbootelasticsearchexample.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apis")
public class ProductController {

    @Autowired
    private ProductService  productService;

    @Autowired
    private ElasticSearchService elasticSearchService;

    @GetMapping("/findAll")
    Iterable<Product> findAll(){
       return productService.getAllProducts();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductMysql> updateProduct(
            @PathVariable int id,
            @RequestBody ProductMysql updatedProduct) {
        ProductMysql updated = productService.updateProduct(updatedProduct, id);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
    @PostMapping("/insert")
    public ProductMysql insertProduct(@RequestBody ProductMysql product){
       return productService.insertProduct(product);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable int id){
        productService.deleteProduct(id);
    }

    @GetMapping("/matchAll")
    public String matchAll() throws IOException {
        SearchResponse<Map> mapSearchResponse = elasticSearchService.matchAllService();
        return mapSearchResponse.hits().hits().toString();
    }

    @GetMapping("/matchAllProducts")
    public List<Product> matchAllProducts() throws IOException {
        SearchResponse<Product> searchResponse = elasticSearchService.matchAllProductService();
        List<Hit<Product>> hits = searchResponse.hits().hits();
        List<Product> listOfProducts = new ArrayList<>();
        for(Hit<Product> hit : hits){
            listOfProducts.add(hit.source());
        }
        return listOfProducts;
    }

    @GetMapping("/matchAllProducts/{fieldValue}")
    public List<Product> matchAllProductsWithName(@PathVariable String fieldValue) throws IOException {
        SearchResponse<Product> searchResponse = elasticSearchService.matchAllProductWithName(fieldValue);
        List<Hit<Product>> hits = searchResponse.hits().hits();
        List<Product> listOfProducts = new ArrayList<>();
        for(Hit<Product> hit : hits){
            listOfProducts.add(hit.source());
        }
        return listOfProducts;
    }

    @GetMapping("/fuzzySearch/{approximateProductName}")
    public List<Product> fuzzySearch(@PathVariable String approximateProductName) throws IOException {
        SearchResponse<Product> searchResponse = elasticSearchService.fuzzySearch(approximateProductName);
        List<Hit<Product>> hits = searchResponse.hits().hits();
        List<Product> productList = new ArrayList<>();
        for(Hit<Product> hit : hits){
            productList.add((hit.source()));
        }
        return productList;
    }

    @GetMapping("/autoSuggest/{partialProductName}")
    List<String> autoSuggestProductSearch(@PathVariable String partialProductName) throws IOException {
        SearchResponse<Product> searchResponse = elasticSearchService.autoSuggestProduct(partialProductName);
        List<Hit<Product>> hitList  =  searchResponse.hits().hits();
        List<Product> productList = new ArrayList<>();
        for(Hit<Product> hit : hitList){
            productList.add(hit.source());
        }
        List<String> listOfProductNames = new ArrayList<>();
        for(Product product : productList){
            listOfProductNames.add(product.getName())  ;
        }
        return listOfProductNames;
    }
}
