package com.dh.movieservice.Config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConsumerConfig {

    @Value("${queue.movie.name}")
    private String queueMovieName;

    //Linkear con el nombre de la cola de RabbitMQ con la cual nos vamos a conectar
    @Bean
    public Queue queue(){
        return new Queue(this.queueMovieName, true);
    }
}
