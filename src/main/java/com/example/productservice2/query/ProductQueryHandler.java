package com.example.productservice2.query;

import com.example.productservice2.core.ProductEntity;
import com.example.productservice2.core.data.ProductRepository;
import com.example.productservice2.query.rest.ProductRestModel;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductQueryHandler {

    private final ProductRepository productRepository;
    public ProductQueryHandler(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    @QueryHandler
    List<ProductRestModel> findProduct(FindProductsQuery query){
        List<ProductRestModel> models = new ArrayList<>();
        List<ProductEntity> entities = productRepository.findAll();
        for(ProductEntity entity : entities){
            ProductRestModel model = new ProductRestModel();
            BeanUtils.copyProperties(entity, model);
            models.add(model);
        }
        return models;
    }
}
