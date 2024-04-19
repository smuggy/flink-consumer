package net.podspace.flink;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

//@SpringBootApplication
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String [] args) {
        logger.info("hello world!");
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();

        KafkaSource<String> source = ctx.getBean(KafkaSource.class);
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        var stream = streamEnv.fromSource(source, WatermarkStrategy.noWatermarks(),"temp-source");
        stream.print().name("print-sink");
        try {
            streamEnv.execute("temperature-process");
        } catch (Exception e) {
            logger.error("Exception occurred: ", e);
        }
    }

//    public static void main(String [] args) {
//        logger.info("Hello world!");
//        SpringApplication.run(Main.class, args);
//    }
//
//    @Bean
//    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//        return args -> System.out.println("we're off and running");
//    }
}
