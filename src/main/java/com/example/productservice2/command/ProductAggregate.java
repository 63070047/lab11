package com.example.productservice2.command;

import com.example.core.command.ReverseProductCommand;
import com.example.core.event.ProductReverseEvent;
import com.example.productservice2.command.rest.CreateProductCommand;
import com.example.productservice2.core.event.ProductCreateEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregate {

    //state
    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;
    public ProductAggregate(){
    }
    //commandHandler
    @CommandHandler
    public ProductAggregate(CreateProductCommand command){
        //BusinessLogic
        if(command.getPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Price cannot be less than or equal to zero");
        }
        if(command.getTitle() == null || command.getTitle().isBlank()){
            throw new IllegalArgumentException("Title cannot be empty");
        }
        ProductCreateEvent productCreatedEvent = new ProductCreateEvent();
        BeanUtils.copyProperties(command, productCreatedEvent);
        AggregateLifecycle.apply(productCreatedEvent);
    }
    @CommandHandler
    public void handler(ReverseProductCommand reverseProductCommand){
        if (quantity < reverseProductCommand.getQuantity()){
            throw new IllegalArgumentException("Insufficient unber of items in stock");
        }
        ProductReverseEvent productReverseEvent = ProductReverseEvent.builder()
                .orderId(reverseProductCommand.getOrderId())
                .productId(reverseProductCommand.getProductId())
                .quantity(reverseProductCommand.getQuantity())
                .userId(reverseProductCommand.getUserId())
                .build();
        AggregateLifecycle.apply(productReverseEvent);
    }
    @EventSourcingHandler
    public void on(ProductCreateEvent productCreatedEvent){
        this.productId = productCreatedEvent.getProductId();
        this.title = productCreatedEvent.getTitle();
        this.price = productCreatedEvent.getPrice();
        this.quantity = productCreatedEvent.getQuantity();
    }
    @EventSourcingHandler
    public void on(ProductReverseEvent productReverseEvent){
        this.quantity -= productReverseEvent.getQuantity();
    }
}
