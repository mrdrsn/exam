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
import model.*;
import view.GUIMeasurement;

public class Controller {

    private GUIMeasurement view;
    private Patient currentPatient;
    private ScheduledGeneration generator;
    private LogHandler logHandler = new LogHandler();

    private LocalTime firstCriticalTempTime;
    private LocalTime firstCriticalHeartRateTime;
    private LocalTime firstCriticalCvpTime;
    
        // Хранение времени окончания критических состояний
    private LocalTime recoveryTempTime;
    private LocalTime recoveryHeartRateTime;
    private LocalTime recoveryCvpTime;
    private static final double EPSILON = 0.00001;

    public Controller(GUIMeasurement view) {
        this.view = view;
        this.generator = new ScheduledGeneration(this::onNewMeasurement);
    }

    public Controller() {
        this.generator = new ScheduledGeneration(this::onNewMeasurement);
    }

    public void setPatient(Patient patient) {
        this.currentPatient = patient;
        resetCriticalTimes();
    }

    public Patient getCurrentPatient() {
        return currentPatient;
    }

    public boolean isMonitoringActive() {
        return generator.isMonitoringActive();
    }

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

    public void startMonitoring() {
        generator.start();
    }

    public void stopMonitoring() {
        generator.stop();
    }

    public void onNewMeasurement(Measurement m) {
        currentPatient.addMeasurement(m);
        try {
            logHandler.writeMeasurement(currentPatient, m);
        } catch (Exception ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Проверяем только что добавленное измерение
        checkAndSetCriticalTimes(m);

        MeasurementViewModel viewModel = new MeasurementViewModel(
                m.getTemperature(),
                m.getHeartRate(),
                m.getCvp()
        );

        SwingUtilities.invokeLater(() -> view.updateUID(viewModel));
    }

    private void checkAndSetCriticalTimes(Measurement m) {
        LocalTime timestamp = m.getTimestamp();

        // Температура
        if (m.getTemperature() >= 37.0 && firstCriticalTempTime == null) {
            firstCriticalTempTime = timestamp;
        } else if (m.getTemperature() < 37.0 && firstCriticalTempTime != null) {
            firstCriticalTempTime = null;
        }

        // Сердцебиение
        int hr = m.getHeartRate();
        if ((hr < 60 || hr > 110) && firstCriticalHeartRateTime == null) {
            firstCriticalHeartRateTime = timestamp;
        } else if ( hr >=60 || hr <= 110 && firstCriticalHeartRateTime != null){
            firstCriticalHeartRateTime = null;
        }

        // ЦВД
        int cvp = m.getCvp();
        if ((cvp < 5 || cvp > 12) && firstCriticalCvpTime == null) {
            firstCriticalCvpTime = timestamp;
        } else if ( cvp >= 5 || cvp <=12 && firstCriticalCvpTime != null){
            firstCriticalCvpTime = null;
        }
    }

    public String getFirstCriticalTempTime(String patientId) {
        return formatTime(firstCriticalTempTime);
    }

    public String getFirstCriticalHeartRateTime(String patientId) {
        return formatTime(firstCriticalHeartRateTime);
    }

    public String getFirstCriticalCvpTime(String patientId) {
        return formatTime(firstCriticalCvpTime);
    }

    private String formatTime(LocalTime time) {
        return time != null ? time.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) : null;
    }

    // Метод сброса критических значений при переходе к другому пациенту
    public void resetCriticalTimes() {
        firstCriticalTempTime = null;
        firstCriticalHeartRateTime = null;
        firstCriticalCvpTime = null;
    }

    public boolean addPatient(String name, String id) {
        if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            return false;
        }
        //оставить в случае: добавление пациента -> сразу переход на страницу мониторинга
        this.currentPatient = new Patient(id, name);
        System.out.println("текущий пациент после добавления " + this.currentPatient);
        boolean fileCreated = logHandler.createEmptyLogFile(id, name);
        return fileCreated;
    }

    public void printAllPatients() {
        List<Patient> patients = logHandler.getAllPatients();
        for (Patient p : patients) {
            System.out.println(p.getId() + " — " + p.getFullName());
        }
    }

    public List<String> getAllPatientIds() {
        return logHandler.getAllPatientIds();
    }

    public String generateNewPatientId() {
        return logHandler.generateNewPatientId();
    }

    public String getPatientNameFromLogFile(String patientId) {
        return logHandler.getPatientNameFromLogFile(patientId);
    }

    public String parseFullNameFromLog(String patientId) {
        return logHandler.parseFullNameFromLog(patientId);
    }

    public Map<String, String> getAllPatientFullNames() {
        Map<String, String> patientInfo = new LinkedHashMap<>(); // <-- здесь ключевое изменение

        List<String> patientIds = getAllPatientIds();
        for (String id : patientIds) {
            String fullName = parseFullNameFromLog(id);
            patientInfo.put(id, fullName); // будет сохранён порядок из patientIds
        }

        return patientInfo;
    }

    public List<Measurement> getLastMeasurements(String patientId, int limit) {
        try {
            return logHandler.getLastMeasurements(patientId, limit);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

//    public String getFirstCriticalTempTime(String patientId) {
//        List<Measurement> measurements = null;
//        try {
//            measurements = logHandler.getLastMeasurements(patientId, 100); // берем достаточно записей
//        } catch (IOException ex) {
//            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (measurements == null || measurements.isEmpty()) {
//            return null;
//        }
//
//        for (Measurement m : measurements) {
//            if (m.getTemperature() >= 37.0) {
//                return m.getTimestamp().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
//            }
//        }
//        return null;
//    }
//
//    public String getFirstCriticalHeartRateTime(String patientId) {
//        List<Measurement> measurements = null;
//        try {
//            measurements = logHandler.getLastMeasurements(patientId, 100);
//        } catch (IOException ex) {
//            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (measurements == null || measurements.isEmpty()) {
//            return null;
//        }
//
//        for (Measurement m : measurements) {
//            int hr = m.getHeartRate();
//            if (hr < 60 || hr > 110) {
//                return m.getTimestamp().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
//            }
//        }
//        return null;
//    }
//
//    public String getFirstCriticalCvpTime(String patientId) {
//        List<Measurement> measurements = null;
//        try {
//            measurements = logHandler.getLastMeasurements(patientId, 100);
//        } catch (IOException ex) {
//            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (measurements == null || measurements.isEmpty()) {
//            return null;
//        }
//
//        for (Measurement m : measurements) {
//            int cvp = m.getCvp();
//            if (cvp < 5 || cvp > 12) {
//                return m.getTimestamp().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
//            }
//        }
//        return null;
//    }
}
