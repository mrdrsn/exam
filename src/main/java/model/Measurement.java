
package model;

import java.time.LocalTime;
/**
 * Класс, представляющий измерение физиологических параметров пациента.
 * Хранит значения температуры тела, пульса, центрального венозного давления (ЦВД)
 * и время проведения измерения.
 *
 * @author nsoko
 */
public class Measurement {
    private double temperature; // температура тела (°C)
    private int heartRate;      // сердечный ритм (ударов/мин)
    private int cvp;            // центральное венозное давление (мм рт. ст.)
    private LocalTime timestamp; // время измерения
    /**
     * Создаёт новый объект измерения с указанием температуры, пульса и ЦВД.
     * Время измерения устанавливается как текущее время.
     *
     * @param temperature температура тела в градусах Цельсия
     * @param heartRate пульс в ударах в минуту
     * @param cvp центральное венозное давление в мм рт. ст.
     */
    public Measurement(double temperature, int heartRate, int cvp) {
        this(temperature, heartRate, cvp, LocalTime.now());
    }
    /**
     * Создаёт новый объект измерения с указанием всех параметров,
     * включая точное время измерения.
     *
     * @param temperature температура тела в градусах Цельсия
     * @param heartRate пульс в ударах в минуту
     * @param cvp центральное венозное давление в мм рт. ст.
     * @param timestamp время проведения измерения
     */
    public Measurement(double temperature, int heartRate, int cvp, LocalTime timestamp) {
        this.temperature = temperature;
        this.heartRate = heartRate;
        this.cvp = cvp;
        this.timestamp = timestamp;
    }
    /**
     * Возвращает значение температуры тела.
     *
     * @return температура тела в градусах Цельсия
     */
    public double getTemperature() {
        return temperature;
    }
    /**
     * Возвращает значение пульса.
     *
     * @return пульс в ударах в минуту
     */
    public int getHeartRate() {
        return heartRate;
    }
    /**
     * Возвращает значение центрального венозного давления.
     *
     * @return ЦВД в мм рт. ст.
     */
    public int getCvp() {
        return cvp;
    }
    /**
     * Возвращает время проведения измерения.
     *
     * @return объект типа {@link LocalTime}, представляющий момент измерения
     */
    public LocalTime getTimestamp() {
        return timestamp;
    }
    /**
     * Возвращает строковое представление измерения.
     * Формат: "HH:mm:ss температура пульс ЦВД"
     *
     * @return строка с данными измерения
     */
    @Override
    public String toString() {
        return String.format("%s %.1f %d %d",
                timestamp.toString(),
                temperature,
                heartRate,
                cvp);
    }
}