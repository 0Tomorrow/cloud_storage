//package com.test.mqservice.config;
//
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.annotation.EnableRabbit;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableRabbit
//public class RabbitConfig {
//    @Bean
//    public Queue uploadFileQueue() {
//        return new Queue("uploadFile");
//    }
//
//    @Bean
//    public Queue createFolderQueue() {
//        return new Queue("createFolder");
//    }
//
//    @Bean
//    public Queue showFileQueue() {
//        return new Queue("showFile");
//    }
//
//    @Bean
//    public Queue showFolderQueue() {
//        return new Queue("showFolder");
//    }
//}
