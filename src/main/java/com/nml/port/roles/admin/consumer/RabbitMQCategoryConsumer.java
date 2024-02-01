package com.nml.port.roles.admin.consumer;

import com.nml.core.application.dto.category.CategoryRequest;
import com.nml.core.application.service.interfaces.ICategoryApplicationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class RabbitMQCategoryConsumer {

    @Autowired
    private ICategoryApplicationService categoryApplicationService;

    private CountDownLatch latch = new CountDownLatch(1);

    @RabbitListener(queues = "create-category-queue")
    public void receiveCreateMessage(CategoryRequest categoryRequest) {
        System.out.println("Received create <" + categoryRequest + ">");
        latch.countDown();
    }

    @RabbitListener(queues = "update-category-queue")
    public void receiveUpdateMessage(CategoryRequest categoryRequest) {
        System.out.println("Received update <" + categoryRequest + ">");
        latch.countDown();
    }

    @RabbitListener(queues = "update-category-queue")
    public void receiveDeleteMessage(String categoryId) {
        System.out.println("Received delete for id <" + categoryId + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
