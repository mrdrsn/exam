
package model;

/**
 * Класс, представляющий модель данных для отображения измерений в пользовательском интерфейсе.
 * Содержит значения температуры, пульса и центрального венозного давления (ЦВД),
 * а также предоставляет методы для их форматированного вывода.
 *
 * @author nsoko
 */
public class MeasurementViewModel {
    private double temperature; // Температура тела в градусах Цельсия
    private int heartRate;      // Пульс в ударах в минуту
    private int cvp;            // Центральное венозное давление в мм рт. ст.

    /**
     * Создаёт новый экземпляр модели измерения с заданными параметрами.
     *
     * @param temperature температура тела в градусах Цельсия
     * @param heartRate пульс в ударах в минуту
     * @param cvp центральное венозное давление в мм рт. ст.
     */
    public MeasurementViewModel(double temperature, int heartRate, int cvp) {
        this.temperature = temperature;
        this.heartRate = heartRate;
        this.cvp = cvp;
    }

    /**
     * Возвращает строковое представление температуры с одним знаком после запятой и символом °C.
     *
     * @return строка с температурой в формате "X.X°C"
     */
    public String getTemperatureText() {
        return String.format("%.1f°C", temperature);
    }

    /**
     * Возвращает строковое представление пульса.
     *
     * @return строка с числовым значением пульса
     */
    public String getHeartRateText() {
        return String.valueOf(heartRate);
    }

    /**
     * Возвращает строковое представление центрального венозного давления (ЦВД).
     *
     * @return строка с числовым значением ЦВД
     */
    public String getCvpText() {
        return String.valueOf(cvp);
    }
}