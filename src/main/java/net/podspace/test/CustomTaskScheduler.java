package net.podspace.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class CustomTaskScheduler extends ThreadPoolTaskScheduler {
    private final static Logger logger = LoggerFactory.getLogger(CustomTaskScheduler.class);
    private final Map<Object, ScheduledFuture<?>> scheduledTasks = new IdentityHashMap<>();

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Duration period) {
        logger.info("scheduling fixed rate item");
        ScheduledFuture<?> future = super.scheduleAtFixedRate(task, period);

        ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
        scheduledTasks.put(runnable.getTarget(), future);

        return future;
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Instant startTime, Duration period) {
        ScheduledFuture<?> future = super.scheduleAtFixedRate(task, startTime, period);

        ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
        scheduledTasks.put(runnable.getTarget(), future);

        return future;
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Duration period) {
        logger.info("scheduling fixed delay item 1");
        ScheduledFuture<?> future = super.scheduleWithFixedDelay(task, period);

        ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
        scheduledTasks.put(runnable.getTarget(), future);

        return future;
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Instant startTime, Duration period) {
        logger.info("scheduling fixed delay item 2");
        ScheduledFuture<?> future = super.scheduleWithFixedDelay(task, startTime, period);

        ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
        scheduledTasks.put(runnable.getTarget(), future);

        return future;
    }

    public Map<Object, ScheduledFuture<?>> getTasks() {
        return scheduledTasks;
    }
}
