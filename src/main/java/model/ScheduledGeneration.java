package model;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ScheduledGeneration {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> task;
    private final Consumer<Measurement> onNewMeasurement;
    private volatile boolean active = false;
    private Measurement lastMeasurement = null;

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
            Measurement measurement = generateRealistic();

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


    private Measurement generateRealistic() {
        if (lastMeasurement == null) {
            double temperature = roundToOneDecimal(36.6 + Math.random() * 1.0);
            int heartRate = 60 + (int) (Math.random() * 30); 
            int cvp = 5 + (int) (Math.random() * 5); 

            lastMeasurement = new Measurement(temperature, heartRate, cvp);
        } else {
            double tempDelta = (Math.random() * 0.2) - 0.1; 
            double temperature = clamp(roundToOneDecimal(lastMeasurement.getTemperature() + tempDelta), 36.3, 39.2);

            int hrDelta = (int) Math.round((Math.random() * 2) - 1); 
            int heartRate = clamp(lastMeasurement.getHeartRate() + hrDelta, 55, 110);

            int cvpDelta = (int) Math.round((Math.random() * 1) - 0.5);
            int cvp = clamp(lastMeasurement.getCvp() + cvpDelta, 2, 17);

            lastMeasurement = new Measurement(temperature, heartRate, cvp);
        }

        System.out.println(lastMeasurement);
        return lastMeasurement;
    }

    private double roundToOneDecimal(double value) {
        return Math.round(value * 10) / 10.0;
    }

    private <T extends Comparable<T>> T clamp(T value, T min, T max) {
        if (value.compareTo(min) < 0) {
            return min;
        }
        if (value.compareTo(max) > 0) {
            return max;
        }
        return value;
    }

    public boolean isMonitoringActive() {
        return active;
    }

    public Measurement getLastMeasurement() {
        return lastMeasurement;
    }

}
