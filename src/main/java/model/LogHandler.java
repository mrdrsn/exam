/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import model.Patient;
import model.Measurement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Nastya
 */
public class LogHandler {

    private static final String LOGS_DIR = "logs";

    public LogHandler() {
        createLogsDirectoryIfNotExists();
    }

    private void createLogsDirectoryIfNotExists() {
        Path path = Paths.get(LOGS_DIR);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                System.err.println("Не удалось создать директорию для логов: " + e.getMessage());
            }
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
        return new File(LOGS_DIR + File.separator + patientId + ".log");
    }

    public void writeMeasurement(Patient patient, Measurement measurement) {
        File logFile = getLogFile(patient.getId());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(String.format(Locale.US, "%.1f %d %d%n",
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
                return firstLine.substring(5); //обрезаем "name="
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

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            Deque<Measurement> deque = new LinkedList<>();

            String line;
            boolean firstLineSkipped = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (!firstLineSkipped && line.startsWith("name=")) {
                    firstLineSkipped = true;
                    continue;
                }

                String[] parts = line.split("\\s+");
                if (parts.length == 3) {
                    try {
                        double temp = Double.parseDouble(parts[0]);
                        int hr = Integer.parseInt(parts[1]);
                        int cvp = Integer.parseInt(parts[2]);
                        Measurement m = new Measurement(temp, hr, cvp);
                        deque.addLast(m);
                        if (deque.size() > limit) {
                            deque.pollFirst();
                        }
                    } catch (NumberFormatException ex) {
                        System.err.println("Ошибка парсинга строки: " + line);
                        continue;
                    }
                }
            }

            return new ArrayList<>(deque);
        }
    }

    public List<Patient> getAllPatients() {
        File dir = new File(LOGS_DIR);
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
        File dir = new File(LOGS_DIR);
        if (!dir.exists() || !dir.isDirectory()) {
            return Collections.emptyList();
        }

        return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(file -> file.getName().endsWith(".log"))
                .map(file -> file.getName().replace(".log", ""))
                .collect(Collectors.toList());
    }

    public String generateNewPatientId() {
        File dir = new File(LOGS_DIR);
        if (!dir.exists() || !dir.isDirectory()) {
            return "P001"; // если нет ни одного файла
        }

        // Получаем список всех файлов пациентов
        File[] files = dir.listFiles((dir1, name) -> name.startsWith("P") && name.matches("P\\d{3}\\.log"));
        if (files == null || files.length == 0) {
            return "P001";
        }

        // Извлекаем номера из названий файлов
        int maxNumber = Arrays.stream(files)
                .map(file -> file.getName().replace(".log", ""))
                .map(name -> name.substring(1)) // убираем 'P'
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);

        int nextNumber = maxNumber + 1;

        // Формируем ID с ведущими нулями
        return String.format("P%03d", nextNumber);
    }
}
