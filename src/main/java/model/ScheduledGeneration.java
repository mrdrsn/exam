package model;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ScheduledGeneration {

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> task;
    private Consumer<Measurement> onNewMeasurement;
    private volatile boolean active = false;

    public ScheduledGeneration(Consumer<Measurement> onNewMeasurement) {
        this.onNewMeasurement = onNewMeasurement;
    }

    public void start() {
        if (active) {
            return;
        }
        active = true;

        System.out.println("генерация данных начата"); //отладка

        task = scheduler.scheduleAtFixedRate(() -> {
            Measurement measurement = generateRandom();
            
            onNewMeasurement.accept(measurement);
            System.out.println(measurement); //отладка
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        active = false;
        System.out.println("генерация данных приостановлена"); //отладка
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
        }
    }

    private Measurement generateRandom() {
        double temperature = 36.0 + Math.random() * 3;
        int heartRate = 55 + (int) (Math.random() * 100);
        int cvp = 2 + (int) (Math.random() * 15);
        System.out.println(new Measurement(temperature, heartRate, cvp));
        return new Measurement(temperature, heartRate, cvp);
    }

    public boolean isMonitoringActive() {
        return active;
    }
}
