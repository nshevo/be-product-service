package com.nml.port.roles.user.consumer;

import com.nml.core.application.dto.product.ProductRequest;
import com.nml.core.application.service.interfaces.IProductApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductConsumer.class);

    @Autowired
    private IProductApplicationService productApplicationService;

    @RabbitListener(queues = {"productQueue"})
    public void consumeCreateProduct(ProductRequest productRequest){

        LOGGER.info(String.format("Received message -> %s", productRequest));

        productApplicationService.createProduct(productRequest);
    }

    @RabbitListener(queues = {"editProductQueue"})
    public void consumeEditProduct(ProductRequest productRequest){

        LOGGER.info(String.format("Received message -> %s", productRequest));

        if(productRequest.getId() != null) productApplicationService.updateProduct(productRequest);

    }
}