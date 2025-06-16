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

    private Measurement generateRandom() {
        double temperature = 36.0 + Math.random() * 3;
        int heartRate = 55 + (int) (Math.random() * 100);
        int cvp = 2 + (int) (Math.random() * 15);
        System.out.println(new Measurement(temperature, heartRate, cvp));
        return new Measurement(temperature, heartRate, cvp);
    }

    private Measurement generateRealistic() {
        if (lastMeasurement == null) {
            // Начальные значения при первом запуске
            double temperature = 36.5 + Math.random() * 1.0; // базовая температура
            int heartRate = 60 + (int) (Math.random() * 30); // нормальный пульс
            int cvp = 5 + (int) (Math.random() * 5); // нормальное ЦВД

            lastMeasurement = new Measurement(temperature, heartRate, cvp);
        } else {
            // Маленькие изменения относительно предыдущих значений
            double tempDelta = (Math.random() * 0.2) - 0.1; // ±0.1
            double temperature = clamp(lastMeasurement.getTemperature() + tempDelta, 36.0, 39.0);

            int hrDelta = (int) Math.round((Math.random() * 2) - 1); // ±0 или ±1 или ±2
            int heartRate = clamp(lastMeasurement.getHeartRate() + hrDelta, 55, 110);

            int cvpDelta = (int) Math.round((Math.random() * 1) - 0.5); // ±0 или ±1
            int cvp = clamp(lastMeasurement.getCvp() + cvpDelta, 2, 17);

            lastMeasurement = new Measurement(temperature, heartRate, cvp);
        }

        System.out.println(lastMeasurement);
        return lastMeasurement;
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
}
