package net.podspace.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class MainTest {
    private static final Logger logger = LoggerFactory.getLogger(MainTest.class);
    private TestBean tb;

    public static void main(String [] args) {
        logger.info("hello world!");
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(Config.class);
        ctx.refresh();

        var mt = new MainTest();
        var bean = ctx.getBean(TestBean.class);
        mt.setValue(bean);
        logger.info(mt.getValue());
        try {
            Thread.sleep(5_000);
        } catch (InterruptedException ignored) {}
        CustomTaskScheduler cts = ctx.getBean(CustomTaskScheduler.class);
//        cts.stop();
        var m = cts.getTasks();
        for (var a: m.values()) {
            logger.info("cancelling is: {}", a.cancel(true));
        }
        cts.shutdown();
    }

    public MainTest() {
    }
    public String getValue() {
        return tb.getValue();
    }
    public void setValue(TestBean tb) {
        this.tb = tb;
    }
}
