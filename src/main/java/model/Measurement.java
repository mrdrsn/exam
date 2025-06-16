
package model;

/**
 *
 * @author Nastya
 */

import java.time.LocalTime;

public class Measurement {
    private double temperature; // температура тела (°C)
    private int heartRate;      // сердечный ритм (ударов/мин)
    private int cvp;            // центральное венозное давление (мм рт. ст.)
    private LocalTime timestamp; // время измерения

    public Measurement(double temperature, int heartRate, int cvp) {
        this(temperature, heartRate, cvp, LocalTime.now());
    }

    public Measurement(double temperature, int heartRate, int cvp, LocalTime timestamp) {
        this.temperature = temperature;
        this.heartRate = heartRate;
        this.cvp = cvp;
        this.timestamp = timestamp;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public int getCvp() {
        return cvp;
    }

    public LocalTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s %.1f %d %d",
                timestamp.toString(),
                temperature,
                heartRate,
                cvp);
    }
}