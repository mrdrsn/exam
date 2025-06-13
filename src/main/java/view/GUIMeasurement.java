package view;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import model.MeasurementViewModel;
import model.Patient;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class GUIMeasurement extends JFrame {
    private Controller controller;
    private JLabel tempLabel = new JLabel("-");
    private JLabel heartRateLabel = new JLabel("-");
    private JLabel cvpLabel = new JLabel("-");
    private JButton backButton; // кнопка "Назад" для мониторинга

    public GUIMeasurement() {
        this.controller = new Controller(this);
        setupUI();
    }

    private void setupUI() {
        clearContentPane();

        JPanel panel = new JPanel(new GridLayout(3, 1));
        JButton newPatientButton = new JButton("Новый пациент");
        JButton openPatientButton = new JButton("Открыть пациента");
        JButton exitButton = new JButton("Выход");

        newPatientButton.addActionListener((ActionEvent e) -> newPatientUI());
        openPatientButton.addActionListener((ActionEvent e) -> openPatientListUI());
        exitButton.addActionListener((ActionEvent e) -> System.exit(0));

        panel.add(newPatientButton);
        panel.add(openPatientButton);
        panel.add(exitButton);

        setContentPane(panel);
        setTitle("Главное меню");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // по центру экрана
        setVisible(true);
    }

    private void newPatientUI() {
        clearContentPane();

        JPanel newPatientPanel = new JPanel(new GridLayout(3, 2));
        JLabel idLabel = new JLabel("ID пациента: ");
        JTextField idArea = new JTextField();
        JLabel nameLabel = new JLabel("ФИО: ");
        JTextField nameArea = new JTextField();
        JButton addPatientButton = new JButton("Сохранить пациента");
        JButton goBackToMenuButton = new JButton("Вернуться обратно");

        // === Обработчик "Сохранить пациента" ===
        addPatientButton.addActionListener((ActionEvent e) -> {
            String id = idArea.getText().trim();
            String name = nameArea.getText().trim();

            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Пожалуйста, заполните все поля.",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = controller.addPatient(name, id);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Пациент успешно создан!",
                        "Успех",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Не удалось создать пациента.",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        goBackToMenuButton.addActionListener((ActionEvent e) -> setupUI());

        newPatientPanel.add(idLabel);
        newPatientPanel.add(idArea);
        newPatientPanel.add(nameLabel);
        newPatientPanel.add(nameArea);
        newPatientPanel.add(addPatientButton);
        newPatientPanel.add(goBackToMenuButton);

        setContentPane(newPatientPanel);
        setTitle("Создание пациента");
        setSize(400, 300);
        setVisible(true);
    }

    private void openPatientListUI() {
        clearContentPane();

        List<String> patientIds = controller.getAllPatientIds();

        if (patientIds.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Нет доступных пациентов",
                    "Список пуст",
                    JOptionPane.INFORMATION_MESSAGE);
            setupUI();
            return;
        }

        JList<String> patientList = new JList<>(patientIds.toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(patientList);

        JButton selectButton = new JButton("Выбрать");
        JButton backButton = new JButton("Назад");

        selectButton.addActionListener(e -> {
            String selectedId = patientList.getSelectedValue();
            if (selectedId != null) {
                try {
                    controller.loadPatient(selectedId);
                    monitoringWindow(); // переходим в окно мониторинга
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Ошибка при загрузке пациента: " + ex.getMessage(),
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(e -> setupUI());

        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        panel.add(new JLabel("Выберите пациента:"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        buttonPanel.add(selectButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(panel);
        setTitle("Список пациентов");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void monitoringWindow() {
        System.out.println("загрузка окна мониторинга");
        clearContentPane();

        JPanel panel = new JPanel(new GridLayout(5, 2)); // добавим кнопку "Вернуться"
        JButton startBtn = new JButton("Начать мониторинг");
        JButton stopBtn = new JButton("Завершить мониторинг");
        backButton = new JButton("Вернуться назад");

        panel.add(new JLabel("Температура"));
        panel.add(tempLabel);
        panel.add(new JLabel("Сердечный ритм"));
        panel.add(heartRateLabel);
        panel.add(new JLabel("ЦВД"));
        panel.add(cvpLabel);
        panel.add(startBtn);
        panel.add(stopBtn);
        panel.add(backButton);

        startBtn.addActionListener(e -> controller.startMonitoring());
        stopBtn.addActionListener(e -> controller.stopMonitoring());
        backButton.addActionListener(e -> {
            if (controller.isMonitoringActive()) {
                JOptionPane.showMessageDialog(this,
                        "Сначала остановите мониторинг",
                        "Ошибка",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                setupUI(); // возвращаемся в главное меню
            }
        });

        setContentPane(panel);
        setTitle("Мониторинг пациента");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateUI(MeasurementViewModel viewModel) {
        tempLabel.setText(viewModel.getTemperatureText());
        heartRateLabel.setText(viewModel.getHeartRateText());
        cvpLabel.setText(viewModel.getCvpText());
    }

    private void clearContentPane() {
        getContentPane().removeAll();
        revalidate();
        repaint();
    }
}