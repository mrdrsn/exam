/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.exam;

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

    //Получить путь к файлу лога пациента
    private File getLogFile(String patientId) {
        return new File(LOGS_DIR + File.separator + patientId + ".log");
    }

    //  Записывает новое измерение в файл
    public void writeMeasurement(Patient patient, Measurement measurement) {
        String patientId = patient.getId();
        File logFile = getLogFile(patientId);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(String.format(Locale.US, "%.1f %d %d%n",
                    measurement.getTemperature(),
                    measurement.getHeartRate(),
                    measurement.getCvp()));
        } catch (IOException e) {
            System.err.println("Ошибка записи в лог-файл: " + e.getMessage());
        }
    }

    //Считывает последние N измерений из файла
    public List<Measurement> getLastMeasurements(String patientId, int limit) throws IOException {
        File logFile = getLogFile(patientId);
        if (!logFile.exists()) {
            return new ArrayList<>();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            Deque<Measurement> deque = new LinkedList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 3) {
                    double temp = Double.parseDouble(parts[0]);
                    int hr = Integer.parseInt(parts[1]);
                    int cvp = Integer.parseInt(parts[2]);
                    Measurement m = new Measurement(temp, hr, cvp);
                    deque.addLast(m);
                    if (deque.size() > limit) {
                        deque.pollFirst();
                    }
                }
            }

            return new ArrayList<>(deque);
        }
    }

    //Получить список всех доступных пациентов (по наличию .log файлов)
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
}
