/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.*;
import java.nio.file.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalTime;
/**
 * Класс, отвечающий за работу с файлами логов пациентов:
 * создание, чтение и запись данных о пациентах и их измерениях.
 *
 * <p>Файлы хранятся в подкаталоге {@code logs} относительно директории запуска приложения.
 * Формат имени файла: <b>{@code [ID пациента].log}</b>, например: {@code P001.log}.
 * В первых строках файла сохраняется имя пациента, далее — данные измерений.
 *
 * @author nsoko
 */
public class LogHandler {

    private static final String LOGS_DIR_NAME = "logs";
    private static final Path BASE_DIR;

    /**
     * Статический блок инициализации, определяющий базовую директорию,
     * где будут храниться все логи. Выполняется до создания экземпляра класса.
     */
    static {
        try {
            String pathToThisClass = LogHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            if (File.separatorChar == '\\') {
                if (pathToThisClass.startsWith("/")) {
                    pathToThisClass = pathToThisClass.substring(1);
                }
            }

            Path jarPath = Paths.get(pathToThisClass);

            Path jarDir;
            if (jarPath.toString().endsWith(".jar")) {
                jarDir = jarPath.getParent();
            } else {
                jarDir = Paths.get("").toAbsolutePath();
            }

            BASE_DIR = jarDir.resolve(LOGS_DIR_NAME);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось определить базовую директорию", e);
        }
    }
    /**
     * Конструктор класса. Создаёт директорию для логов, если она ещё не существует.
     */
    public LogHandler() {
        createLogsDirectoryIfNotExists();
    }
    /**
     * Проверяет наличие директории для логов и создаёт её, если она отсутствует.
     */
    private void createLogsDirectoryIfNotExists() {
        try {
            Files.createDirectories(BASE_DIR);
        } catch (IOException e) {
            System.err.println("Не удалось создать директорию для логов: " + e.getMessage());
        }
    }
    /**
     * Создаёт новый пустой файл лога для указанного пациента.
     * Если файл уже существует, то он не перезаписывается.
     *
     * @param patientId уникальный идентификатор пациента
     * @param fullName полное имя пациента
     * @return true, если файл успешно создан или уже существовал, иначе false
     */
    public boolean createEmptyLogFile(String patientId, String fullName) {
        File logFile = getLogFile(patientId);
        try {
            createLogsDirectoryIfNotExists();
            if (!logFile.exists()) {
                logFile.createNewFile();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile))) {
                    writer.write("name=" + fullName);
                    writer.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Не удалось создать файл для пациента " + patientId + ": " + e.getMessage());
            return false;
        }
    }
    /**
     * Возвращает файл лога, соответствующий заданному идентификатору пациента.
     *
     * @param patientId идентификатор пациента
     * @return объект типа File, указывающий на файл лога
     */
    public File getLogFile(String patientId) {
        return BASE_DIR.resolve(patientId + ".log").toFile();
    }
    /**
     * Записывает новое измерение в конец файла лога пациента.
     *
     * @param patient пациент, которому принадлежит измерение
     * @param measurement само измерение
     */
    public void writeMeasurement(Patient patient, Measurement measurement) {
        File logFile = getLogFile(patient.getId());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(String.format(Locale.US, "%s %.1f %d %d%n",
                    measurement.getTimestamp().toString(),
                    measurement.getTemperature(),
                    measurement.getHeartRate(),
                    measurement.getCvp()));
        } catch (IOException e) {
            System.err.println("Ошибка записи в лог-файл: " + e.getMessage());
        }
    }
    /**
     * Получает имя пациента из его файла лога.
     *
     * @param patientId идентификатор пациента
     * @return имя пациента или строка "Без имени", если не найдено
     */
    public String getPatientNameFromLogFile(String patientId) {
        File logFile = getLogFile(patientId);
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String firstLine = reader.readLine();
            if (firstLine != null && firstLine.startsWith("name=")) {
                return firstLine.substring(5);
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения имени пациента из файла: " + e.getMessage());
        }
        return "Без имени";
    }
    /**
     * Получает список последних измерений для заданного пациента.
     *
     * @param patientId идентификатор пациента
     * @param limit максимальное количество возвращаемых измерений
     * @return список измерений, или null, если недостаточно данных
     * @throws IOException если произошла ошибка чтения файла
     */
    public List<Measurement> getLastMeasurements(String patientId, int limit) throws IOException {
        File logFile = getLogFile(patientId);
        if (!logFile.exists()) {
            return new ArrayList<>();
        }

        List<Measurement> measurements = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            boolean firstLineSkipped = false;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!firstLineSkipped && line.startsWith("name=")) {
                    firstLineSkipped = true;
                    continue;
                }

                String[] parts = line.split("\\s+");
                if (parts.length >= 4) {
                    try {
                        LocalTime timestamp = LocalTime.parse(parts[0]);
                        double temp = Double.parseDouble(parts[1]);
                        int hr = Integer.parseInt(parts[2]);
                        int cvp = Integer.parseInt(parts[3]);
                        measurements.add(new Measurement(temp, hr, cvp, timestamp));
                    } catch (Exception ex) {
                        System.err.println("Ошибка парсинга строки: " + line);
                        continue;
                    }
                }
            }
        }

        if (measurements.size() < limit) {
            return null;
        }

        int startIndex = measurements.size() - limit;
        return new ArrayList<>(measurements.subList(startIndex, measurements.size()));
    }
    /**
     * Получает список всех идентификаторов пациентов, чьи данные есть в системе.
     *
     * @return список строковых идентификаторов пациентов
     */
    public List<String> getAllPatientIds() {
        File dir = BASE_DIR.toFile();
        if (!dir.exists() || !dir.isDirectory()) {
            return Collections.emptyList();
        }
        return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(file -> file.getName().endsWith(".log"))
                .map(file -> file.getName().replace(".log", ""))
                .collect(Collectors.toList());
    }
    /**
     * Получает список всех идентификаторов пациентов, чьи данные есть в системе.
     *
     * @return список строковых идентификаторов пациентов
     */
    public String generateNewPatientId() {
        File dir = BASE_DIR.toFile();
        if (!dir.exists() || !dir.isDirectory()) {
            return "P001";
        }
        File[] files = dir.listFiles((dir1, name) -> name.startsWith("P") && name.matches("P\\d{3}\\.log"));
        if (files == null || files.length == 0) {
            return "P001";
        }
        int maxNumber = Arrays.stream(files)
                .map(file -> file.getName().replace(".log", ""))
                .map(name -> name.substring(1))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);
        int nextNumber = maxNumber + 1;
        return String.format("P%03d", nextNumber);
    }
    /**
     * Получает полное имя пациента из файла лога и форматирует его в короткий вид.
     *
     * @param patientId идентификатор пациента
     * @return полное имя в формате "Имя И.О." или "Без имени"
     */
    public String parseFullNameFromLog(String patientId) {
        File logFile = getLogFile(patientId);
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String firstLine = reader.readLine();
            if (firstLine != null && firstLine.startsWith("name=")) {
                String fullName = firstLine.substring(5).trim();
                return formatShortName(fullName);
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения имени из файла: " + e.getMessage());
        }
        return "Без имени";
    }
    /**
     * Форматирует полное имя в короткий вид: "Имя И.О."
     *
     * @param fullName полное имя пациента
     * @return отформатированное имя
     */
    private String formatShortName(String fullName) {
        String[] parts = fullName.split("\\s+");
        StringBuilder shortName = new StringBuilder(parts[0]);
        if (parts.length > 1) {
            shortName.append(" ").append(parts[1].charAt(0)).append(".");
        }
        if (parts.length > 2) {
            shortName.append(parts[2].charAt(0)).append(".");
        }
        return shortName.toString();
    }
    /**
     * Получает список температурных значений для указанного пациента.
     *
     * @param patientId идентификатор пациента
     * @return список температур
     */
    public List<Double> getAllTemperatures(String patientId) {
        return getAllMeasurements(patientId).stream()
                .map(Measurement::getTemperature)
                .collect(Collectors.toList());
    }
    /**
     * Получает список значений пульса для указанного пациента.
     *
     * @param patientId идентификатор пациента
     * @return список пульсов
     */
    public List<Double> getAllHeartRates(String patientId) {
        return getAllMeasurements(patientId).stream()
                .map(m -> (double) m.getHeartRate())
                .collect(Collectors.toList());
    }
    /**
     * Получает список значений ЦВД для указанного пациента.
     *
     * @param patientId идентификатор пациента
     * @return список ЦВД
     */
    public List<Double> getAllCvp(String patientId) {
        return getAllMeasurements(patientId).stream()
                .map(m -> (double) m.getCvp())
                .collect(Collectors.toList());
    }
     /**
     * Получает все измерения для указанного пациента из файла лога.
     *
     * @param patientId идентификатор пациента
     * @return список измерений
     */
    private List<Measurement> getAllMeasurements(String patientId) {
        File logFile = getLogFile(patientId);
        List<Measurement> measurements = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            boolean firstLineSkipped = false;
            while ((line = reader.readLine()) != null) {
                if (!firstLineSkipped && line.startsWith("name=")) {
                    firstLineSkipped = true;
                    continue;
                }
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 4) {
                    try {
                        LocalTime timestamp = LocalTime.parse(parts[0]);
                        double temp = Double.parseDouble(parts[1]);
                        int hr = Integer.parseInt(parts[2]);
                        int cvp = Integer.parseInt(parts[3]);
                        measurements.add(new Measurement(temp, hr, cvp, timestamp));
                    } catch (Exception e) {
                        System.err.println("Ошибка чтения строки: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения лог-файла: " + e.getMessage());
        }

        return measurements;
    }

}
