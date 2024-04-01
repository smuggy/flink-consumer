package net.podspace.test;

import org.springframework.scheduling.annotation.Scheduled;

public class TestBean {
    private String value;
    public TestBean() {}

    public void setValue(String v){
        this.value = v;
    }
    public String getValue() {
        return this.value;
    }
    @Scheduled(fixedDelay = 1_000)
    public void scheduleFixedDelayTask() {
        System.out.println(
                "Fixed delay task - " + System.currentTimeMillis() / 1000);
    }
}
