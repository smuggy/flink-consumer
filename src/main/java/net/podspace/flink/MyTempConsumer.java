//package net.podspace.flink;
//
//import org.apache.kafka.clients.consumer.Consumer;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.time.Duration;
//import java.util.Collections;
//import java.util.List;
//
//public class MyTempConsumer {
//    private static final Logger logger = LoggerFactory.getLogger(MyTempConsumer.class);
//    private final Consumer<String,String> consumer;
//    private final List<String> topicList;
//    private final List<String> pushList;
//    @Autowired
//    private @Qualifier("threadPoolTaskExecutor")
//    ThreadPoolTaskExecutor taskExecutor;
//    @Autowired
//    private MyTempProcessor processor;
//
//    public MyTempConsumer(String topicName, ConsumerFactory<String,String> cf) {
//        this.topicList = Collections.singletonList(topicName);
//        this.pushList = Collections.singletonList("other-topic");
//        this.consumer = cf.createConsumer();
//        this.consumer.subscribe(this.topicList);
//
//    }
//
//    @Scheduled(fixedDelayString = "10")
//    public void consume() {
//        try {
//            ConsumerRecords<String, String> records = this.consumer.poll(Duration.ZERO);
//            if (!records.isEmpty()) {
//                logger.info("TempConsumer: read record(s) from topic {}", this.topicList.getFirst());
//                for (ConsumerRecord<String, String> record : records) {
//                    if (record.value() != null) {
//                        Runnable thread = new ConsumerThread(this.pushList, processor, record);
//                        this.taskExecutor.execute(thread);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            logger.error("ERROR while consuming Kafka message");
//        }
//    }
//
//    private static class ConsumerThread implements Runnable {
//        ConsumerRecord<String, String> record;
//        MyTempProcessor processor;
//        List<String> topicList;
//
//        ConsumerThread(List<String> argTopicList, MyTempProcessor argProcessor, ConsumerRecord<String, String> argValue) {
//            this.record = argValue;
//            this.topicList = argTopicList;
//            this.processor = argProcessor;
//        }
//
//        @Override
//        public void run() {
//            if (this.topicList.getFirst().equals(record.topic())) {
//                logger.info("ConsumerThread: processing record.");
//                this.processor.process(record.value());
//            }
//        }
//    }
//}
