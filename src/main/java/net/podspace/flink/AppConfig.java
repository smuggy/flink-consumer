package net.podspace.flink;

import net.podspace.test.YamlPropertySourceFactory;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
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

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource(value = "classpath:application.yaml", factory = YamlPropertySourceFactory.class)
@EnableScheduling
@EnableAsync
public class AppConfig implements SchedulingConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    @Value("${myapp.kafka.groupId}")
    private String groupId;
    @Value("${myapp.kafka.topicName}")
    private String topicName;
    @Value("${myapp.kafka.bootstrapAddress}")
    private String bootstrapAddress;
    @Value("${myapp.thread.size}")
    private String threadSizeString;

//    @Bean
//    public ConsumerFactory<String, String> consumerFactory() {
//        Map<String, Object> configProps = new HashMap<>();
//        logger.info("Consumer: bootstrap server: " + bootstrapAddress);
//        configProps.put(
//                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
//                bootstrapAddress);
//        configProps.put(
//                ConsumerConfig.GROUP_ID_CONFIG,
//                groupId);
//        configProps.put(
//                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
//                StringDeserializer.class.getName());
//        configProps.put(
//                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
//                StringDeserializer.class.getName());
//        return new DefaultKafkaConsumerFactory<>(configProps);
//
//    }

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

//    @Bean
//    public MyTempConsumer myTempConsumer(ConsumerFactory<String, String> consumerFactory) {
//        return new MyTempConsumer(topicName, consumerFactory);
//    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public KafkaSource<String> kafkaSource() {
        return KafkaSource.<String>builder()
                .setBootstrapServers(bootstrapAddress)
                .setTopics(topicName)
                .setGroupId("my-group")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
    }
//    KafkaSource<String> source = KafkaSource.<String>builder()
//            .setBootstrapServers(brokers)
//            .setTopics("input-topic")
//            .setGroupId("my-group")
//            .setStartingOffsets(OffsetsInitializer.earliest())
//            .setValueOnlyDeserializer(new SimpleStringSchema())
//            .build();
//
//env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");

//    @Bean
//    public MyTempProcessor myTempProcessor(@Value("${kafka.producer.topic}") String topics,
//                                           @Value("${kafka.bootstrap.servers}") String bootstrapServers,
//                                           @Value("${kafka.bootstrap.servers.enable}") String bootstrapServersEnable) {
//
//        String KEY_SERIALIZER = "key.serializer";
//        String KEY_SERIALIZER_CLASS = "org.apache.kafka.common.serialization.StringSerializer";
//        String VALUE_SERIALIZER = "value.serializer";
//        String VALUE_SERIALIZER_CLASS = "org.apache.kafka.common.serialization.StringSerializer";
//
//        Properties properties = System.getProperties();
//        if (bootstrapServersEnable.equalsIgnoreCase("on")) {
//            properties.setProperty("bootstrap.servers", bootstrapServers);
//        }
//        properties.setProperty(KEY_SERIALIZER, KEY_SERIALIZER_CLASS);
//        properties.setProperty(VALUE_SERIALIZER, VALUE_SERIALIZER_CLASS);
//
//        return new MyTempProcessor(topics, StreamExecutionEnvironment.createLocalEnvironment(), properties);
//    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(this.taskExecutor());
    }

    @PreDestroy
    public void destroy() {
        logger.info("kafka-flink is closing...");
    }

    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService taskExecutor() {
        return Executors.newScheduledThreadPool(10);
    }

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        System.out.println(this.threadSizeString);
        System.out.println(topicName);
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setKeepAliveSeconds(15);
        executor.initialize();
        return executor;
    }
}
