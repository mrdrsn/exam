
package model;

import model.Measurement;
import java.util.List;
import java.util.ArrayList;

/**
 * Класс, представляющий пациента в системе мониторинга физиологических параметров.
 * Хранит информацию об идентификаторе, имени пациента и его измерениях.
 *
 * @author nsoko
 */
public class Patient {
    private String id;
    private String name;
    private List<Measurement> measurements;

    /**
     * Создаёт нового пациента с указанным идентификатором и именем.
     * Инициализирует пустой список измерений.
     *
     * @param id уникальный идентификатор пациента
     * @param name имя пациента
     */
    public Patient(String id, String name) {
        this.id = id;
        this.name = name;
        this.measurements = new ArrayList<>();
    }

    /**
     * Добавляет новое измерение в список измерений пациента.
     *
     * @param measurement объект измерения, который нужно добавить
     */
    public void addMeasurement(Measurement measurement) {
        this.measurements.add(measurement);
    }

    /**
     * Возвращает последние измерения пациента в заданном количестве.
     *
     * @param limit максимальное количество измерений для возврата
     * @return список последних измерений, не превышающий заданный лимит
     */
    public List<Measurement> getLastMeasurements(int limit) {
        int start = Math.max(0, this.measurements.size() - limit);
        return new ArrayList<>(this.measurements.subList(start, this.measurements.size()));
    }

    /**
     * Возвращает уникальный идентификатор пациента.
     *
     * @return строка с идентификатором
     */
    public String getId() {
        return id;
    }

    /**
     * Возвращает полное имя пациента.
     *
     * @return строка с именем
     */
    public String getFullName() {
        return this.name;
    }

    /**
     * Возвращает список всех измерений, связанных с этим пациентом.
     *
     * @return список измерений
     */
    public List<Measurement> getMeasurements() {
        return this.measurements;
    }

    /**
     * Возвращает строковое представление пациента.
     * Формат: "ID Имя"
     *
     * @return строка с данными о пациенте
     */
    @Override
    public String toString() {
        return this.id + " " + this.name;
    }
}