package com.example.springbootelasticsearchexample.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.springbootelasticsearchexample.entity.Product;
import com.example.springbootelasticsearchexample.util.ElasticSearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;
@Service
public class ElasticSearchService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public SearchResponse<Map> matchAllService() throws IOException {
        Supplier<Query> supplier = ElasticSearchUtil.supplier();
        SearchResponse<Map> search = elasticsearchClient.search(s -> s.query(supplier.get()), Map.class);
        System.out.println("elasticsearch query is "+supplier.get().toString());
        return search;
    }

    public SearchResponse<Product> matchAllProductService() throws IOException {
        Supplier<Query> supplier = ElasticSearchUtil.supplier();
        SearchResponse<Product> search = elasticsearchClient.search(s -> s.index("products").query(supplier.get()), Product.class);
        System.out.println("elasticsearch query is "+supplier.get().toString());
        return search;
    }

    public SearchResponse<Product> matchAllProductWithName(String fieldValue) throws IOException {
        Supplier<Query> supplier = ElasticSearchUtil.supplierWithNameField(fieldValue);
        SearchResponse<Product> search = elasticsearchClient.search(s -> s.index("products").query(supplier.get()), Product.class);
        System.out.println("elasticsearch query is "+supplier.get().toString());
        return search;
    }

    public SearchResponse<Product> fuzzySearch(String approximateProductName) throws IOException {
        Supplier<Query> supplier = ElasticSearchUtil.createSupplierFuzzyQuery(approximateProductName);
        SearchResponse<Product> response = elasticsearchClient.search(s -> s.index("products").query(supplier.get()),Product.class);
        return response;
    }

    public SearchResponse<Product> autoSuggestProduct(String partialProductName) throws IOException {

        Supplier<Query> supplier = ElasticSearchUtil.createSupplierAutoSuggest(partialProductName);
        SearchResponse<Product> searchResponse  = elasticsearchClient
                .search(s->s.index("products").query(supplier.get()), Product.class);
        System.out.println(" elasticsearch auto suggestion query"+supplier.get().toString());
        return searchResponse;
    }
}
