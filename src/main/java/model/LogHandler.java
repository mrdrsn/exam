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

public class LogHandler {

    private static final String LOGS_DIR_NAME = "logs";
    private static final Path BASE_DIR;

    // Статический блок для определения BASE_DIR до создания экземпляра
    static {
        try {
            // Получаем путь к JAR-файлу или к классу при запуске из IDE
            String pathToThisClass = LogHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            // Убираем начальный '/' на Windows, если он есть
            if (File.separatorChar == '\\') { // это Windows
                if (pathToThisClass.startsWith("/")) {
                    pathToThisClass = pathToThisClass.substring(1);
                }
            }

            Path jarPath = Paths.get(pathToThisClass);

            Path jarDir;
            if (jarPath.toString().endsWith(".jar")) {
                // Запуск из .jar: получаем директорию, где находится .jar
                jarDir = jarPath.getParent();
            } else {
                // Запуск из IDE: можно использовать текущую директорию или корень проекта
                jarDir = Paths.get("").toAbsolutePath();
            }

            BASE_DIR = jarDir.resolve(LOGS_DIR_NAME);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось определить базовую директорию", e);
        }
    }

    public LogHandler() {
        createLogsDirectoryIfNotExists();
    }

    private void createLogsDirectoryIfNotExists() {
        try {
            Files.createDirectories(BASE_DIR); // Создаст всю цепочку, если нужно
        } catch (IOException e) {
            System.err.println("Не удалось создать директорию для логов: " + e.getMessage());
        }
    }

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

    public File getLogFile(String patientId) {
        return BASE_DIR.resolve(patientId + ".log").toFile();
    }

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

    public String getPatientNameFromLogFile(String patientId) {
        File logFile = getLogFile(patientId);
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String firstLine = reader.readLine();
            if (firstLine != null && firstLine.startsWith("name=")) {
                return firstLine.substring(5); // обрезаем "name="
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения имени пациента из файла: " + e.getMessage());
        }
        return "Без имени";
    }

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
                if (parts.length >= 4) { // должно быть: [time] temp hr cvp
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

    public List<Patient> getAllPatients() {
        File dir = BASE_DIR.toFile();
        if (!dir.exists() || !dir.isDirectory()) {
            return Collections.emptyList();
        }
        return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(file -> file.getName().endsWith(".log"))
                .map(file -> {
                    String patientId = file.getName().replace(".log", "");
                    String fullName = getPatientNameFromLogFile(patientId);
                    return new Patient(patientId, fullName);
                })
                .collect(Collectors.toList());
    }

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

    public String generateNewPatientId() {
        File dir = BASE_DIR.toFile();
        if (!dir.exists() || !dir.isDirectory()) {
            return "P001"; // если нет ни одного файла
        }
        File[] files = dir.listFiles((dir1, name) -> name.startsWith("P") && name.matches("P\\d{3}\\.log"));
        if (files == null || files.length == 0) {
            return "P001";
        }
        int maxNumber = Arrays.stream(files)
                .map(file -> file.getName().replace(".log", ""))
                .map(name -> name.substring(1)) // убираем 'P'
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);
        int nextNumber = maxNumber + 1;
        return String.format("P%03d", nextNumber);
    }

    public String parseFullNameFromLog(String patientId) {
        File logFile = getLogFile(patientId);
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String firstLine = reader.readLine();
            if (firstLine != null && firstLine.startsWith("name=")) {
                String fullName = firstLine.substring(5).trim(); // обрезаем "name="
                return formatShortName(fullName);
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения имени из файла: " + e.getMessage());
        }
        return "Без имени";
    }

    private String formatShortName(String fullName) {
        String[] parts = fullName.split("\\s+");
        StringBuilder shortName = new StringBuilder(parts[0]); // фамилия
        if (parts.length > 1) {
            shortName.append(" ").append(parts[1].charAt(0)).append(".");
        }
        if (parts.length > 2) {
            shortName.append(parts[2].charAt(0)).append(".");
        }
        return shortName.toString();
    }
}
