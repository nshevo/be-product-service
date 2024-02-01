package com.nml.port.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQCategoryConfig {

    @Value("category-exchange")
    private String categoryExchange;

    @Value("create-category-queue")
    private String createCategoryQueue;

    @Value("create-category-routing-key")
    private String createCategoryRoutingKey;

    @Value("update-category-queue")
    private String updateCategoryQueue;

    @Value("update-category-routing-key")
    private String updateCategoryRoutingKey;

    @Value("delete-category-queue")
    private String deleteCategoryQueue;

    @Value("delete-category-routing-key")
    private String deleteCategoryRoutingKey;

    @Bean
    public Queue createCategoryQueue(){
        return new Queue(createCategoryQueue);
    }

    @Bean
    public Queue editCategoryQueue(){
        return new Queue(updateCategoryQueue);
    }

    @Bean
    public Queue deleteCategoryQueue(){
        return new Queue(deleteCategoryQueue);
    }

    @Bean
    public TopicExchange categoryExchange(){
        return new TopicExchange(categoryExchange);
    }

    @Bean
    public Binding createCategoryBinding(){
        return BindingBuilder
                .bind(createCategoryQueue())
                .to(categoryExchange())
                .with(createCategoryRoutingKey);
    }

    @Bean
    public Binding updateCategoryBinding(){
        return BindingBuilder
                .bind(editCategoryQueue())
                .to(categoryExchange())
                .with(updateCategoryRoutingKey);
    }

    @Bean
    public Binding deleteCategoryBinding(){
        return BindingBuilder
                .bind(deleteCategoryQueue())
                .to(categoryExchange())
                .with(deleteCategoryRoutingKey);
    }

    @Bean
    public MessageConverter categoryConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate categoryTemplate(ConnectionFactory connectionFactory){
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(categoryConverter());
        return rabbitTemplate;
    }

}
