//package net.podspace.flink;
//
//import org.apache.flink.api.common.serialization.SimpleStringSchema;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Properties;
////import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
//
//public class MyTempProcessor {
//    private static final Logger logger = LoggerFactory.getLogger(MyTempProcessor.class);
//    private final List<String> topicList;
//    private final Properties properties;
//    private final StreamExecutionEnvironment streamExecutionEnvironment;
//
//    public MyTempProcessor(String topicName, StreamExecutionEnvironment env, Properties properties) {
//        logger.info("Creating logger.");
//        this.topicList = Collections.singletonList(topicName);
//        this.properties = properties;
//        this.streamExecutionEnvironment = env;
//    }
//
//
//    public void process(String jsonArray) {
//        try {
//            List<String> data = new ArrayList<>();
//            Collections.addAll(data, jsonArray);
//
//            this.streamExecutionEnvironment.fromCollection(data)
////                    .flatMap(new TuplesFlatMapFunction())
//                    .keyBy(0)
////                    .filter(new FilterFunction())
////                    .map(new ResultMapFunction())
//                    .addSink(new FlinkKafkaProducer<>(this.topicList.getFirst(), new SimpleStringSchema(), this.properties));
//            this.streamExecutionEnvironment.execute("kafka-flink manager");
//        } catch (Exception e) {
//            logger.error("ERROR while procession message with Flink");
//        }
//    }
////    private class TuplesFlatMapFunction implements FlatMapFunction<String, Tuple5<String, String, String, String, String>> {
////        @Override
////        public void flatMap(String s, Collector<Tuple5<String, String, String, String, String>> collector) throws Exception {
////            String[] stringArrayId = s.split(":");
////            collector.collect(new Tuple5<>(stringArrayId[0], stringArrayId[1], stringArrayId[2], stringArrayId[3], stringArrayId[4]));
////        }
////    }
//
//}
