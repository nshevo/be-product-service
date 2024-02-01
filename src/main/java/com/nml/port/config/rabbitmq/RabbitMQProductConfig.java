package com.nml.port.config.rabbitmq;

import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQProductConfig {

    @Value("product-category-exchange")
    private String productExchange;

    @Value("create-product-queue")
    private String createProductQueue;

    @Getter
    @Value("create-product-routing-key")
    private String createProductRoutingKey;

    @Value("update-product-queue")
    private String updateProductQueue;

    @Getter
    @Value("update-product-routing-key")
    private String updateProductRoutingKey;

    @Value("delete-product-queue")
    private String deleteProductQueue;

    @Getter
    @Value("delete-product-routing-key")
    private String deleteProductRoutingKey;

    @Bean
    public Queue createProductQueue(){
        return new Queue(createProductQueue);
    }

    @Bean
    public Queue editProductQueue(){
        return new Queue(updateProductQueue);
    }

    @Bean
    public Queue deleteProductQueue(){
        return new Queue(deleteProductQueue);
    }

    @Bean
    public TopicExchange productExchange(){
        return new TopicExchange(productExchange);
    }

    @Bean
    public Binding createProductBinding(){
        return BindingBuilder
                .bind(createProductQueue())
                .to(productExchange())
                .with(createProductRoutingKey);
    }

    @Bean
    public Binding editProductBinding(){
        return BindingBuilder
                .bind(editProductQueue())
                .to(productExchange())
                .with(updateProductRoutingKey);
    }

    @Bean
    public Binding deleteProductBinding(){
        return BindingBuilder
                .bind(deleteProductQueue())
                .to(productExchange())
                .with(deleteProductRoutingKey);
    }

    @Bean
    public MessageConverter productMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate productTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(productMessageConverter());
        return template;
    }

}
