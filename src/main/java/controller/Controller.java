package controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import main.java.model.DataCalculations;
import model.*;
import view.GUIMeasurement;

/**
 * Контроллер приложения, управляющий взаимодействием между моделью данных и
 * пользовательским интерфейсом. Обрабатывает показания измерений, отслеживает
 * критические параметры пациентов, сохраняет данные в лог и обеспечивает
 * обновление графического интерфейса.
 *
 * @author nsoko
 */
public class Controller {

    private GUIMeasurement view;
    private Patient currentPatient;
    private ScheduledGeneration generator;
    private LogHandler logHandler = new LogHandler();

    private LocalTime firstCriticalTempTime;
    private LocalTime firstCriticalHeartRateTime;
    private LocalTime firstCriticalCvpTime;

    private LocalTime recoveryTempTime;
    private LocalTime recoveryHeartRateTime;
    private LocalTime recoveryCvpTime;

    private LocalTime lastTempStart;
    private LocalTime lastTempRecovery;

    private LocalTime lastHrStart;
    private LocalTime lastHrRecovery;

    private LocalTime lastCvpStart;
    private LocalTime lastCvpRecovery;

    /**
     * Создаёт новый экземпляр контроллера с указанным графическим интерфейсом.
     *
     * @param view графический интерфейс, связанный с этим контроллером
     */
    public Controller(GUIMeasurement view) {
        this.view = view;
        this.generator = new ScheduledGeneration(this::onNewMeasurement);
    }

    /**
     * Создаёт новый экземпляр контроллера без связанного графического
     * интерфейса. Используется в случае, когда GUI будет назначен позже.
     */
    public Controller() {
        this.generator = new ScheduledGeneration(this::onNewMeasurement);
    }

    /**
     * Инициализирует графический интерфейс, создавая новую инстанцию
     * GUIMeasurement. Может быть вызван для запуска интерфейса после создания
     * контроллера.
     */
    public void start() {
        view = new GUIMeasurement();
    }

    /**
     * Устанавливает текущего пациента и сбрасывает все временные метки,
     * связанные с критическими состояниями.
     *
     * @param patient объект пациента, который становится текущим
     */
    public void setPatient(Patient patient) {
        this.currentPatient = patient;
        resetCriticalTimes();
    }

    /**
     * Возвращает текущего пациента, связанного с этим контроллером.
     *
     * @return текущий пациент
     */
    public Patient getCurrentPatient() {
        return currentPatient;
    }

    /**
     * Получает последнее измерение, сгенерированное генератором.
     *
     * @return последнее измерение или null, если ещё не было измерений
     */
    public Measurement getLastMeasurement() {
        return generator.getLastMeasurement();
    }

    /**
     * Проверяет, активно ли сейчас наблюдение (мониторинг).
     *
     * @return true, если мониторинг активен, иначе false
     */
    public boolean isMonitoringActive() {
        return generator.isMonitoringActive();
    }

    /**
     * Получает последние значения температуры для указанного пациента.
     * Возвращает до 10 последних измерений, преобразованных в строки.
     *
     * @param patientId идентификатор пациента
     * @return список строковых представлений температур или null, если данные
     * не доступны
     */
    public List<String> getTemperaturesFromMeasurements(String patientId) {
        List<Measurement> measurements = null;
        try {
            measurements = logHandler.getLastMeasurements(patientId, 10);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (measurements == null) {
            return null;
        }

        List<String> temperatures = new ArrayList<>();
        for (Measurement m : measurements) {
            temperatures.add(String.valueOf(m.getTemperature()));
        }
        return temperatures;
    }

    /**
     * Получает последние значения пульса для указанного пациента. Возвращает до
     * 10 последних измерений, преобразованных в строки.
     *
     * @param patientId идентификатор пациента
     * @return список строковых представлений пульса или null, если данные не
     * доступны
     */
    public List<String> getHeartRateFromMeasurements(String patientId) {
        List<Measurement> measurements = null;
        try {
            measurements = logHandler.getLastMeasurements(patientId, 10);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (measurements == null) {
            return null;
        }

        List<String> heartRates = new ArrayList<>();
        for (Measurement m : measurements) {
            heartRates.add(String.valueOf(m.getHeartRate()));
        }
        return heartRates;
    }

    /**
     * Получает последние значения ЦВД (центрального венозного давления) для
     * указанного пациента. Возвращает до 10 последних измерений,
     * преобразованных в строки.
     *
     * @param patientId идентификатор пациента
     * @return список строковых представлений ЦВД или null, если данные не
     * доступны
     */
    public List<String> getCvpFromMeasurements(String patientId) {
        List<Measurement> measurements = null;
        try {
            measurements = logHandler.getLastMeasurements(patientId, 10);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (measurements == null) {
            return null;
        }

        List<String> cvps = new ArrayList<>();
        for (Measurement m : measurements) {
            cvps.add(String.valueOf(m.getCvp()));
        }
        return cvps;
    }

    /**
     * Запускает процесс мониторинга измерений. Используется для активации
     * генератора измерений.
     */
    public void startMonitoring() {
        generator.start();
    }

    /**
     * Останавливает процесс мониторинга измерений. Используется для деактивации
     * генератора измерений.
     */
    public void stopMonitoring() {
        generator.stop();
    }

    /**
     * Обрабатывает новое измерение: добавляет его к текущему пациенту,
     * записывает в лог и обновляет графический интерфейс.
     *
     * @param m объект нового измерения
     */
    public void onNewMeasurement(Measurement m) {
        currentPatient.addMeasurement(m);
        try {
            logHandler.writeMeasurement(currentPatient, m);
        } catch (Exception ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        checkAndSetCriticalTimes(m);

        MeasurementViewModel viewModel = new MeasurementViewModel(
                m.getTemperature(),
                m.getHeartRate(),
                m.getCvp()
        );

        SwingUtilities.invokeLater(() -> view.updateUID(viewModel));
    }

    /**
     * Проверяет параметры измерения и устанавливает или сбрасывает временные
     * метки для критических состояний температуры, пульса и ЦВД.
     *
     * @param m объект измерения, на основе которого выполняется проверка
     */
    private void checkAndSetCriticalTimes(Measurement m) {
        LocalTime timestamp = m.getTimestamp();

        if (m.getTemperature() >= 37.0) {
            if (firstCriticalTempTime == null) {
                firstCriticalTempTime = timestamp;
            }
            recoveryTempTime = null;
        } else {
            if (firstCriticalTempTime != null) {
                lastTempStart = firstCriticalTempTime;
                lastTempRecovery = timestamp;
                firstCriticalTempTime = null;
                recoveryTempTime = timestamp;
            }
        }

        int hr = m.getHeartRate();
        if (hr < 60 || hr > 110) {
            if (firstCriticalHeartRateTime == null) {
                firstCriticalHeartRateTime = timestamp;
            }
            recoveryHeartRateTime = null;
        } else {
            if (firstCriticalHeartRateTime != null) {
                lastHrStart = firstCriticalHeartRateTime;
                lastHrRecovery = timestamp;
                firstCriticalHeartRateTime = null;
                recoveryHeartRateTime = timestamp;
            }
        }

        int cvp = m.getCvp();
        if (cvp < 5 || cvp > 12) {
            if (firstCriticalCvpTime == null) {
                firstCriticalCvpTime = timestamp;
            }
            recoveryCvpTime = null;
        } else {
            if (firstCriticalCvpTime != null) {
                lastCvpStart = firstCriticalCvpTime;
                lastCvpRecovery = timestamp;
                firstCriticalCvpTime = null;
                recoveryCvpTime = timestamp;
            }
        }
    }

    /**
     * Возвращает время восстановления нормальной температуры после критического
     * состояния в секундах.
     *
     * @return время восстановления в секундах или null, если данные отсутствуют
     */
    public String getTimeToRecoveryForTemp() {
        if (lastTempStart != null && lastTempRecovery != null) {
            long seconds = java.time.Duration.between(lastTempStart, lastTempRecovery).getSeconds();
            return String.valueOf(seconds);
        }
        return null;
    }

    /**
     * Возвращает время восстановления нормального пульса после критического
     * состояния в секундах.
     *
     * @return время восстановления в секундах или null, если данные отсутствуют
     */
    public String getTimeToRecoveryForHeartRate() {
        if (lastHrStart != null && lastHrRecovery != null) {
            long seconds = java.time.Duration.between(lastHrStart, lastHrRecovery).getSeconds();
            return String.valueOf(seconds);
        }
        return null;
    }

    /**
     * Возвращает время восстановления нормального уровня ЦВД после критического
     * состояния в секундах.
     *
     * @return время восстановления в секундах или null, если данные отсутствуют
     */
    public String getTimeToRecoveryForCvp() {
        if (lastCvpStart != null && lastCvpRecovery != null) {
            long seconds = java.time.Duration.between(lastCvpStart, lastCvpRecovery).getSeconds();
            return String.valueOf(seconds);
        }
        return null;
    }

    /**
     * Возвращает время первого критического значения температуры в формате
     * "HH:mm:ss". Если критическое значение не было зафиксировано, возвращает
     * null.
     *
     * @param patientId идентификатор пациента (не используется в текущей
     * реализации)
     * @return время в виде строки или null, если данные отсутствуют
     */
    public String getFirstCriticalTempTime(String patientId) {
        return formatTime(firstCriticalTempTime);
    }

    /**
     * Возвращает время первого критического значения пульса в формате
     * "HH:mm:ss". Если критическое значение не было зафиксировано, возвращает
     * null.
     *
     * @param patientId идентификатор пациента (не используется в текущей
     * реализации)
     * @return время в виде строки или null, если данные отсутствуют
     */
    public String getFirstCriticalHeartRateTime(String patientId) {
        return formatTime(firstCriticalHeartRateTime);
    }

    /**
     * Возвращает время первого критического значения ЦВД (центрального
     * венозного давления) в формате "HH:mm:ss". Если критическое значение не
     * было зафиксировано, возвращает null.
     *
     * @param patientId идентификатор пациента (не используется в текущей
     * реализации)
     * @return время в виде строки или null, если данные отсутствуют
     */
    public String getFirstCriticalCvpTime(String patientId) {
        return formatTime(firstCriticalCvpTime);
    }

    /**
     * Форматирует объект LocalTime в строку с заданным форматом "HH:mm:ss".
     * Если переданное время равно null, возвращает null.
     *
     * @param time объект времени, который нужно отформатировать
     * @return строковое представление времени или null
     */
    private String formatTime(LocalTime time) {
        return time != null ? time.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) : null;
    }

    /**
     * Сбрасывает все временные метки, связанные с критическими состояниями.
     * Используется при установке нового пациента или повторном начале
     * мониторинга.
     */
    public void resetCriticalTimes() {
        firstCriticalTempTime = null;
        firstCriticalHeartRateTime = null;
        firstCriticalCvpTime = null;
    }

    /**
     * Добавляет нового пациента в систему с заданным именем и идентификатором.
     * Создаёт соответствующий файл лога для хранения измерений.
     *
     * @param name имя пациента
     * @param id уникальный идентификатор пациента
     * @return true, если пациент был успешно добавлен, иначе false
     */
    public boolean addPatient(String name, String id) {
        if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            return false;
        }
        this.currentPatient = new Patient(id, name);
        System.out.println("текущий пациент после добавления " + this.currentPatient);
        boolean fileCreated = logHandler.createEmptyLogFile(id, name);
        return fileCreated;
    }

    /**
     * Возвращает список идентификаторов всех зарегистрированных пациентов.
     *
     * @return список строковых идентификаторов пациентов
     */
    public List<String> getAllPatientIds() {
        return logHandler.getAllPatientIds();
    }

    /**
     * Генерирует новый уникальный идентификатор пациента.
     *
     * @return строка с новым идентификатором
     */
    public String generateNewPatientId() {
        return logHandler.generateNewPatientId();
    }

    /**
     * Получает имя пациента из файла лога по его идентификатору.
     *
     * @param patientId идентификатор пациента
     * @return имя пациента или null, если пациент не найден
     */
    public String getPatientNameFromLogFile(String patientId) {
        return logHandler.getPatientNameFromLogFile(patientId);
    }

    /**
     * Парсит полное имя пациента из файла лога по его идентификатору.
     *
     * @param patientId идентификатор пациента
     * @return полное имя пациента или null, если данные отсутствуют
     */
    public String parseFullNameFromLog(String patientId) {
        return logHandler.parseFullNameFromLog(patientId);
    }

    /**
     * Возвращает карту, связывающую идентификаторы пациентов с их полными
     * именами. Используется для отображения списка пациентов в пользовательском
     * интерфейсе.
     *
     * @return карта вида <patientId, fullName>
     */
    public Map<String, String> getAllPatientFullNames() {
        Map<String, String> patientInfo = new LinkedHashMap<>();

        List<String> patientIds = getAllPatientIds();
        for (String id : patientIds) {
            String fullName = parseFullNameFromLog(id);
            patientInfo.put(id, fullName);
        }

        return patientInfo;
    }

    /**
     * Получает последние измерения для указанного пациента в заданном
     * количестве.
     *
     * @param patientId идентификатор пациента
     * @param limit максимальное количество измерений для получения
     * @return список последних измерений или null, если произошла ошибка
     */
    public List<Measurement> getLastMeasurements(String patientId, int limit) {
        try {
            return logHandler.getLastMeasurements(patientId, limit);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Возвращает обработчик логов, используемый контроллером.
     *
     * @return текущий экземпляр LogHandler
     */
    public LogHandler getLogHandler() {
        return this.logHandler;
    }

    /**
     * Возвращает список температурных значений для указанного пациента.
     *
     * @param patientId идентификатор пациента
     * @return список температур или пустой список, если данных нет
     */
    public List<Double> getAllTemperatures(String patientId) {
        return logHandler.getAllTemperatures(patientId);
    }

    /**
     * Возвращает список значений пульса для указанного пациента.
     *
     * @param patientId идентификатор пациента
     * @return список пульсов или пустой список, если данных нет
     */
    public List<Double> getAllHeartRates(String patientId) {
        return logHandler.getAllHeartRates(patientId);
    }

    /**
     * Возвращает список значений ЦВД для указанного пациента.
     *
     * @param patientId идентификатор пациента
     * @return список ЦВД или пустой список, если данных нет
     */
    public List<Double> getAllCvp(String patientId) {
        return logHandler.getAllCvp(patientId);
    }

    /**
     * Подготавливает данные для расчётов, выбирая конкретный список из общего
     * набора.
     *
     * @param all общий список списков значений
     * @param i индекс нужного подсписка
     * @return новый объект DataCalculations, содержащий выбранный список
     */
    public DataCalculations prepareForCalculations(List<List<Double>> all, int i) {
        return new DataCalculations(all.get(i));
    }
}
