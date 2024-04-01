package net.podspace.flink;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@EnableScheduling
@EnableAsync
public class AppConfig implements SchedulingConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    @Value("${myapp.kafka.topicName}")
    private String topicName;
    @Value("${myapp.kafka.bootstrapAddress}")
    private String bootstrapAddress;
    @Value("${myapp.kafka.groupId}")
    private String groupId;
    private int threadSize = 10;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        logger.info("Consumer: bootstrap server: " + bootstrapAddress);
        configProps.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        configProps.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                groupId);
        configProps.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        configProps.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        return new DefaultKafkaConsumerFactory<>(configProps);

    }

//    @Bean
//    public StreamExecutionEnvironment streamExecutionEnvironment() {
//        return StreamExecutionEnvironment.getExecutionEnvironment();
//    }
//
//    @Bean
//    public DataStream<String> dataStream(StreamExecutionEnvironment env) {
//        Properties properties = new Properties();
//        properties.setProperty("bootstrap.servers", bootstrapAddress);
//        properties.setProperty("group.id", groupId);
//        return env.addSource(new FlinkKafkaConsumer<>(
//                "flink-demo", new SimpleStringSchema(), properties) );
//    }
//        var consumer = new DefaultKafkaConsumerFactory<String,String>(configProps).createConsumer();
//        consumer.subscribe(Collections.singletonList(topicName));
//        return consumer;

    @Bean
    public MyTempConsumer myTempConsumer(ConsumerFactory<String,String> consumerFactory) {
        return new MyTempConsumer(topicName, consumerFactory);
    }


    @Bean
    public MyTempProcessor myTempProcessor(@Value("${kafka.producer.topic}") String topics,
                                           @Value("${kafka.bootstrap.servers}") String bootstrapServers,
                                           @Value("${kafka.bootstrap.servers.enable}") String bootstrapServersEnable) {

        String KEY_SERIALIZER = "key.serializer";
        String KEY_SERIALIZER_CLASS = "org.apache.kafka.common.serialization.StringSerializer";
        String VALUE_SERIALIZER = "value.serializer";
        String VALUE_SERIALIZER_CLASS = "org.apache.kafka.common.serialization.StringSerializer";

        Properties properties = System.getProperties();
        if (bootstrapServersEnable.equalsIgnoreCase("on")) {
            properties.setProperty("bootstrap.servers", bootstrapServers);
        }
        properties.setProperty(KEY_SERIALIZER, KEY_SERIALIZER_CLASS);
        properties.setProperty(VALUE_SERIALIZER, VALUE_SERIALIZER_CLASS);

        return new MyTempProcessor(topics, StreamExecutionEnvironment.createLocalEnvironment(), properties);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(this.taskExecutor());
    }

    @PreDestroy
    public void destroy() {
        logger.info("kafka-flink is closing...");
    }

    @Bean(destroyMethod="shutdown")
    public ScheduledExecutorService taskExecutor() {
        return Executors.newScheduledThreadPool(this.threadSize);
    }

    @Bean(destroyMethod="shutdown")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(@Value("${thread.size}") String argThreadSize) {
        this.threadSize = Integer.parseInt(argThreadSize);
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(this.threadSize);
        executor.setKeepAliveSeconds(15);
        executor.initialize();
        return executor;
    }
}
