package net.podspace.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
//@PropertySource("classpath:app.properties")
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
@EnableScheduling
public class Config implements SchedulingConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    @Value("${myapp.val}")
    private String v;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public TestBean testBean() {
        logger.info(v);
        logger.info("creating test bean.");
        var t = new TestBean();
        t.setValue("config value");
        return t;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        logger.info("creating custom scheduler.");
        return new CustomTaskScheduler();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        logger.info("Setting custom scheduler.");
        scheduledTaskRegistrar.setScheduler(this.taskScheduler());
    }

//    @Bean(destroyMethod="shutdown")
//    public ScheduledExecutorService taskExecutor() {
//        return Executors.newScheduledThreadPool(5);
//    }

//    @Bean(destroyMethod="shutdown")
//    public ScheduledExecutorService taskExecutor() {
//        return Executors.newScheduledThreadPool(3);
//    }
}
