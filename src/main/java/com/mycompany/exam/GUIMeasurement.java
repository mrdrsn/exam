package com.mycompany.exam;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Nastya
 */
public class GUIMeasurement extends JFrame {

    public Patient patient = new Patient("P001", "Иван Иванов");
    public JLabel temperatureLabel = new JLabel();
    public JLabel heartRateLabel = new JLabel();
    public JLabel cvpLabel = new JLabel();

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> monitoringTask;
    private AtomicBoolean monitoringActive = new AtomicBoolean(false);
    private LogHandler logHandler = new LogHandler();

    public GUIMeasurement() {
        JPanel mainPanel = new JPanel(new GridLayout(4, 2));
        JButton startButton = new JButton("Начать мониторинг");
        JButton endButton = new JButton("Закончить мониторинг");

        startButton.addActionListener((ActionEvent e) -> {
            startMonitoring();
        });
        endButton.addActionListener((ActionEvent e) -> {
            try {
                stopMonitoring();
            } catch (IOException ex) {
                Logger.getLogger(GUIMeasurement.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        JLabel t = new JLabel("Температура");
        JLabel h = new JLabel("Сердечный ритм");
        JLabel p = new JLabel("Центральное венозное давление");
        mainPanel.add(t);
        mainPanel.add(temperatureLabel);
        mainPanel.add(h);
        mainPanel.add(heartRateLabel);
        mainPanel.add(p);
        mainPanel.add(cvpLabel);
        mainPanel.add(startButton);
        mainPanel.add(endButton);
        setContentPane(mainPanel);
        setSize(400, 300);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void startMonitoring() {
        if (monitoringActive.get()) {
            System.out.println("процесс уже запущен");
            return;
        }
        monitoringActive.set(true);
        System.out.println("процесс мониторинга запущен");
        monitoringTask = scheduler.scheduleAtFixedRate(() -> {
            Measurement measurement = generateRandomMeasurement();
            patient.addMeasurement(measurement);
            logHandler.writeMeasurement(patient, measurement);
            SwingUtilities.invokeLater(() -> updateUI(measurement));
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void stopMonitoring() throws IOException {
        monitoringActive.set(false);
        if (monitoringTask != null && !monitoringTask.isCancelled()) {
            monitoringTask.cancel(true);
            System.out.println("процесс мониторинга приостановлен");
            System.out.println(logHandler.getLastMeasurements(patient.getId(), 10));
//            logHandler.getLastMeasurements(patient.getId(), 10);
        }
    }

    private Measurement generateRandomMeasurement() {
        double temperature = 36.0 + Math.random() * 3; // от 36.0 до 39.0
        int heartRate = 60 + (int) (Math.random() * 30); // от 60 до 90
        int cvp = 5 + (int) (Math.random() * 5); // от 5 до 10

        return new Measurement(temperature, heartRate, cvp);
    }

    private void updateUI(Measurement m) {
        temperatureLabel.setText(String.format("%.1f°C", m.getTemperature()));
        heartRateLabel.setText(String.valueOf(m.getHeartRate()));
        cvpLabel.setText(String.valueOf(m.getCvp()));
    }
}
