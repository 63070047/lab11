package com.example.productservice2.query;

import com.example.core.event.ProductReverseEvent;
import com.example.productservice2.core.ProductEntity;
import com.example.productservice2.core.data.ProductRepository;
import com.example.productservice2.core.event.ProductCreateEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProductEventHandler {
    private final ProductRepository productRepository;
    public ProductEventHandler(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    @EventHandler
    public void on(ProductCreateEvent event){
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(event, productEntity);
        productRepository.save(productEntity);
    }
    @EventHandler
    public void on(ProductReverseEvent productReverseEvent){
        ProductEntity productEntity = productRepository.findByProductId(productReverseEvent.getProductId());
        productEntity.setQuantity(productEntity.getQuantity() - productReverseEvent.getQuantity());
        productRepository.save(productEntity);
    }
}
