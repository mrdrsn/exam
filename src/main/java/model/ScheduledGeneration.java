package model;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Класс, реализующий генерацию реалистичных данных измерений пациента с заданным интервалом.
 * Использует {@link ScheduledExecutorService} для периодической генерации новых значений.
 * 
 * <p>Каждое новое измерение формируется на основе предыдущего значения с небольшими случайными отклонениями,
 * в рамках допустимых физиологических диапазонов температуры, пульса и ЦВД.</p>
 *
 * @author nsoko
 */
public class ScheduledGeneration {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> task;
    private final Consumer<Measurement> onNewMeasurement;
    private volatile boolean active = false;
    private Measurement lastMeasurement = null;

    /**
     * Создаёт новый экземпляр генератора измерений.
     *
     * @param onNewMeasurement функциональный интерфейс, принимающий каждое сгенерированное измерение
     */
    public ScheduledGeneration(Consumer<Measurement> onNewMeasurement) {
        this.onNewMeasurement = onNewMeasurement;
    }

    /**
     * Запускает циклическую генерацию измерений с интервалом 1 секунда.
     * Генерация начинается с начальных параметров или продолжается от последнего значения.
     */
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

    /**
     * Останавливает генерацию измерений.
     * Текущая задача отменяется, но последнее значение сохраняется.
     */
    public void stop() {
        active = false;
        System.out.println("генерация данных приостановлена"); //отладка
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
        }
    }

    /**
     * Генерирует новое измерение на основе предыдущего значения с небольшими случайными изменениями.
     * Температура, пульс и ЦВД корректируются так, чтобы оставаться в допустимых пределах.
     *
     * @return объект типа {@link Measurement}, содержащий сгенерированные данные
     */
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

    /**
     * Округляет значение до одного знака после десятичной точки.
     *
     * @param value исходное число
     * @return округлённое значение с точностью до одной десятой
     */
    private double roundToOneDecimal(double value) {
        return Math.round(value * 10) / 10.0;
    }

    /**
     * Ограничивает значение заданным диапазоном [min, max].
     *
     * @param value значение, которое нужно ограничить
     * @param min минимально допустимое значение
     * @param max максимально допустимое значение
     * @return значение, находящееся в указанном диапазоне
     */
    private <T extends Comparable<T>> T clamp(T value, T min, T max) {
        if (value.compareTo(min) < 0) {
            return min;
        }
        if (value.compareTo(max) > 0) {
            return max;
        }
        return value;
    }

    /**
     * Проверяет, активна ли в данный момент генерация измерений.
     *
     * @return true, если генерация активна, иначе false
     */
    public boolean isMonitoringActive() {
        return active;
    }

    /**
     * Возвращает последнее сгенерированное измерение.
     *
     * @return объект типа {@link Measurement} или null, если ещё не было измерений
     */
    public Measurement getLastMeasurement() {
        return lastMeasurement;
    }
}