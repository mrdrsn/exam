package controller;

import java.io.File;
import java.io.IOException;
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

    public Controller(GUIMeasurement view) {
        this.view = view;
        this.generator = new ScheduledGeneration(this::onNewMeasurement);
    }

    public Controller() {
        this.generator = new ScheduledGeneration(this::onNewMeasurement);
    }

    public void setPatient(Patient patient) {
        this.currentPatient = patient;
    }

    public Patient getCurrentPatient() {
        return currentPatient;
    }

    public boolean isMonitoringActive() {
        return generator.isMonitoringActive();
    }

//    public void loadPatient(String patientId) throws IOException {
//        String fullName = logHandler.getPatientNameFromLogFile(patientId);
//        System.out.println(fullName);
//        currentPatient = new Patient(patientId, fullName);
//        List<Measurement> lastMeasurements = logHandler.getLastMeasurements(patientId, 10);
//
//        SwingUtilities.invokeLater(() -> {
//            view.updateUID(new MeasurementViewModel(
//                    lastMeasurements.isEmpty() ? 0 : lastMeasurements.get(0).getTemperature(),
//                    lastMeasurements.isEmpty() ? 0 : lastMeasurements.get(0).getHeartRate(),
//                    lastMeasurements.isEmpty() ? 0 : lastMeasurements.get(0).getCvp()
//            ));
//        });
//    }
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

    private void onNewMeasurement(Measurement m) {
        currentPatient.addMeasurement(m);
        try {
            logHandler.writeMeasurement(currentPatient, m);
        } catch (Exception ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        MeasurementViewModel viewModel = new MeasurementViewModel(
                m.getTemperature(),
                m.getHeartRate(),
                m.getCvp()
        );
        SwingUtilities.invokeLater(() -> view.updateUID(viewModel));
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
}
