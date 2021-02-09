package me.jko.demoreplyingkafkatemplateserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.SimpleKafkaHeaderMapper;
import org.springframework.kafka.support.converter.MessagingMessageConverter;
import org.springframework.messaging.handler.annotation.SendTo;

import org.apache.kafka.clients.admin.NewTopic;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @KafkaListener(id = "server", topics = "kRequests")
  @SendTo // use default replyTo expression
  public String listen(String in) {
    System.out.println("Server received: " + in);
    return in.toUpperCase();
  }

  @Bean
  public NewTopic kRequests() {
    return TopicBuilder.name("kRequests")
        .partitions(3)
        .replicas(2)
        .build();
  }

  @Bean // not required if Jackson is on the classpath
  public MessagingMessageConverter simpleMapperConverter() {
    MessagingMessageConverter messagingMessageConverter = new MessagingMessageConverter();
    messagingMessageConverter.setHeaderMapper(new SimpleKafkaHeaderMapper());
    return messagingMessageConverter;
  }
}
