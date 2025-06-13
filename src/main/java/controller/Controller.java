package controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
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

    public void setPatient(Patient patient) {
        this.currentPatient = patient;
    }

    public Patient getCurrentPatient() {
        return currentPatient;
    }

    public boolean isMonitoringActive() {
        return generator.isMonitoringActive();
    }

    public void loadPatient(String patientId) throws IOException {
        String fullName = logHandler.getPatientNameFromLogFile(patientId);
        System.out.println(fullName);
        currentPatient = new Patient(patientId, fullName);
        List<Measurement> lastMeasurements = logHandler.getLastMeasurements(patientId, 10);

        SwingUtilities.invokeLater(() -> {
            view.updateUI(new MeasurementViewModel(
                    lastMeasurements.isEmpty() ? 0 : lastMeasurements.get(0).getTemperature(),
                    lastMeasurements.isEmpty() ? 0 : lastMeasurements.get(0).getHeartRate(),
                    lastMeasurements.isEmpty() ? 0 : lastMeasurements.get(0).getCvp()
            ));
        });
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
        SwingUtilities.invokeLater(() -> view.updateUI(viewModel));
    }

    public boolean addPatient(String name, String id) {
        if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            return false;
        }
        //оставить в случае: добавление пациента -> сразу переход на страницу мониторинга
//        this.currentPatient = new Patient(id, name);
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

}
