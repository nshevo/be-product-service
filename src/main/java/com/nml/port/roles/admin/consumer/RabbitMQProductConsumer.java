package com.nml.port.roles.admin.consumer;

import com.nml.core.application.dto.product.ProductRequest;
import com.nml.core.application.service.interfaces.IProductApplicationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Component
public class RabbitMQProductConsumer {

    @Autowired
    private IProductApplicationService productApplicationService;

    private CountDownLatch latch = new CountDownLatch(1);

    @RabbitListener(queues = "create-product-queue")
    public void receiveCreateMessage(ProductRequest productRequest) {
        System.out.println("Received create <" + productRequest + ">");
        latch.countDown();
    }

    @RabbitListener(queues = "update-product-queue")
    public void receiveUpdateMessage(ProductRequest productRequest) {
        System.out.println("Received update <" + productRequest + ">");
        latch.countDown();
    }

    @RabbitListener(queues = "update-product-queue")
    public void receiveDeleteMessage(UUID productId) {
        System.out.println("Received delete for id <" + productId + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}
