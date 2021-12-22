package com.sogeti.filmland.scheduler;

import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class CustomSchedulerService {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void computePaymentTime(Runnable task, int initialDelay, int period) {
        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.DAYS);
    }
}
