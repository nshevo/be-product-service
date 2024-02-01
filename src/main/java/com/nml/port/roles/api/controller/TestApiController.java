package com.nml.port.roles.api.controller;

import com.nml.core.application.dto.product.ProductRequest;
import com.nml.port.config.rabbitmq.RabbitMQProductConfig;
import jakarta.validation.Valid;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1/product")
@RestController
public class TestApiController {

    @Autowired
    private AmqpTemplate productTemplate;

    @Autowired
    private RabbitMQProductConfig rabbitMQProductConfig;

    @PostMapping("/products")
    public ResponseEntity<String> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        String routingKey = rabbitMQProductConfig.getCreateProductRoutingKey();
        System.out.println(routingKey);
        try{
            productTemplate.convertAndSend(routingKey, productRequest);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok("Product creation request sent to the queue.");
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<String> updateProduct(@RequestBody ProductRequest productRequest) {
        String routingKey = rabbitMQProductConfig.getUpdateProductRoutingKey();
        productTemplate.convertAndSend(routingKey, productRequest);
        return ResponseEntity.ok("Product update request sent to the queue.");
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID id) {
        String routingKey = rabbitMQProductConfig.getDeleteProductRoutingKey();
        productTemplate.convertAndSend(routingKey, id);
        return ResponseEntity.ok("Product deletion request sent to the queue.");
    }
}
