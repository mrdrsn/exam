package view;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import model.MeasurementViewModel;
import model.Patient;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GUIMeasurement extends JFrame {

    private Controller controller;
    private JLabel tempLabel = new JLabel("-");
    private JLabel heartRateLabel = new JLabel("-");
    private JLabel cvpLabel = new JLabel("-");
    private JButton backButton; // кнопка "Назад" для мониторинга

    public GUIMeasurement() {
        this.controller = new Controller(this);
//        newPatientUID();
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                Component component = GUIMeasurement.this.getFocusOwner();
                if (component != null && component instanceof JTextField) {
                    GUIMeasurement.this.setFocusable(true);
                    GUIMeasurement.this.requestFocusInWindow();
                }
            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        openPatientListUID();
//        setupUI();
    }

    private void setupUI() {
        clearContentPane();

        JPanel panel = new JPanel(new GridLayout(3, 1));
        JButton newPatientButton = new JButton("Новый пациент");
        JButton openPatientButton = new JButton("Открыть пациента");
        JButton exitButton = new JButton("Выход");

        newPatientButton.addActionListener((ActionEvent e) -> newPatientUID());
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

    private void newPatientUID() {
        clearContentPane();
        JPanel mainPanel = new BackgroundPanel("/Frame 19 (1).jpg", false);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        alignCustomPanel(leftPanel, 612, 720);

        Font fontSFBig = CustomFontLoader.loadCustomFont(48, "fonts/SFProText-SemiBold.ttf");
        Font fontSF = CustomFontLoader.loadCustomFont(16, "fonts/SFProText-Regular.ttf");
        Font fontSFMedium = CustomFontLoader.loadCustomFont(22, "fonts/SFProText-Medium.ttf");
        Font fontSFSemiBold = CustomFontLoader.loadCustomFont(24, "fonts/SFProText-SemiBold.ttf");

        // === Правая панель ===
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(668, 720));
        rightPanel.setMaximumSize(rightPanel.getPreferredSize());
        rightPanel.setMinimumSize(rightPanel.getPreferredSize());

        JLabel newPatientLabel = new JLabel("Новый пациент...");
        newPatientLabel.setFont(fontSFBig);
        newPatientLabel.setHorizontalAlignment(SwingConstants.LEFT);
        newPatientLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField enterSurname = createCustomTextField("Фамилия*", 20, 20);
        JTextField enterName = createCustomTextField("Имя*", 20, 20);
        JTextField enterMiddleName = createCustomTextField("Отчество", 20, 20);

        JPanel idInfoPanel = new JPanel();
        idInfoPanel.setLayout(new BoxLayout(idInfoPanel, BoxLayout.X_AXIS));
        alignCustomPanel(idInfoPanel, 500, 43);

        JPanel leftInfoPanel = new JPanel();
        leftInfoPanel.setLayout(new BoxLayout(leftInfoPanel, BoxLayout.Y_AXIS));
        alignCustomPanel(leftInfoPanel, 400, 43);

        JLabel idInfoLabel1 = new JLabel("Новому пациенту будет присвоен уникальный");
        idInfoLabel1.setFont(fontSF);
        idInfoLabel1.setAlignmentX(Component.LEFT_ALIGNMENT);
        idInfoLabel1.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel idInfoLabel2 = new JLabel("идентификационный номер:");
        idInfoLabel2.setFont(fontSF);
        idInfoLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        idInfoLabel2.setHorizontalAlignment(SwingConstants.LEFT);

        leftInfoPanel.add(idInfoLabel1);
        leftInfoPanel.add(idInfoLabel2);

        JPanel rightInfoPanel = new JPanel();
        rightInfoPanel.setLayout(new BoxLayout(rightInfoPanel, BoxLayout.Y_AXIS));
        alignCustomPanel(rightInfoPanel, 100, 43);

        JLabel idInfoLabel3 = new JLabel(controller.generateNewPatientId());
        idInfoLabel3.setFont(fontSFSemiBold);
        idInfoLabel3.setAlignmentX(Component.LEFT_ALIGNMENT);
        idInfoLabel3.setHorizontalAlignment(SwingConstants.LEFT);

        rightInfoPanel.add(Box.createVerticalStrut(7));
        rightInfoPanel.add(idInfoLabel3);

        idInfoPanel.add(leftInfoPanel);
        idInfoPanel.add(rightInfoPanel);

        JButton createPatientButton = createCustomButton("Создать", 600, 53);
        JButton backToMenuButton = createCustomButton("Назад", 143, 53);

        createPatientButton.addActionListener((ActionEvent e) -> {
            String surname = enterSurname.getText().trim();
            String name = enterName.getText().trim();
            String middleName = enterMiddleName.getText().trim();
            System.out.println("surname");
            System.out.println(surname);
            if (surname.equals("Фамилия*") || name.equals("Имя*")) {
                JOptionPane.showMessageDialog(this,
                        "Пожалуйста, заполните все поля.",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (middleName.equals("Отчество")) {
                middleName = "";
            }
            String fullName = surname + " " + name + " " + middleName;
            String id = idInfoLabel3.getText();
            boolean success = controller.addPatient(fullName, id);
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
            monitoringWindow();
        });
        rightPanel.add(Box.createVerticalStrut(74));
        rightPanel.add(newPatientLabel);
        rightPanel.add(Box.createVerticalStrut(36));
        rightPanel.add(enterSurname);
        rightPanel.add(Box.createVerticalStrut(36));
        rightPanel.add(enterName);
        rightPanel.add(Box.createVerticalStrut(33));
        rightPanel.add(enterMiddleName);
        rightPanel.add(Box.createVerticalStrut(33));
        rightPanel.add(idInfoPanel);
        rightPanel.add(Box.createVerticalStrut(27));
        rightPanel.add(createPatientButton);
        rightPanel.add(Box.createVerticalStrut(27));
        rightPanel.add(backToMenuButton);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        setContentPane(mainPanel);
        setTitle("Создание пациента");
        setSize(1280, 720);
        setVisible(true);
    }

    private JButton createCustomButton(String text, int x, int y) {
        Font fontSFMedium = CustomFontLoader.loadCustomFont(22, "fonts/SFProText-Medium.ttf");
        JButton button = new RoundedButton(text);
        button.setPreferredSize(new Dimension(x, y));
        button.setMinimumSize(button.getPreferredSize());
        button.setMaximumSize(button.getPreferredSize());
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setFont(fontSFMedium);
        button.setBackground(Color.decode("#2f2f2f"));
        button.setForeground(Color.WHITE);
        return button;
    }

    private JTextField createCustomTextField(String placeholder, int columns, int radius) {
        Font fontSF = CustomFontLoader.loadCustomFont(16, "fonts/SFProText-Regular.ttf");
        JTextField textField = new RoundedTextField(placeholder, columns, radius);
        textField.setFont(fontSF);
        textField.setHorizontalAlignment(SwingConstants.LEFT);
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        textField.setPreferredSize(new Dimension(400, 50));
        textField.setMaximumSize(textField.getPreferredSize());
        textField.setMinimumSize(textField.getPreferredSize());
        return textField;
    }

    private void alignCustomPanel(JPanel panel, int x, int y) {
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(x, y));
        panel.setMinimumSize(panel.getPreferredSize());
        panel.setMaximumSize(panel.getPreferredSize());
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
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

   

    private void openPatientListUID() {
        clearContentPane();

        // === Основной контейнер с наложением ===
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1296, 720)); // 1280 + 16 на скроллбар

        layeredPane.setLayout(null); // ручное позиционирование

        // === Фоновая панель с компонентами ===
        BackgroundPanelStrict backgroundPanel = new BackgroundPanelStrict("/Frame 24 (1).jpg", false);
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        backgroundPanel.setOpaque(false);

        // Пример добавления компонентов на фон:
//         addExampleComponents(backgroundPanel);
        JScrollPane scrollPane = new JScrollPane(backgroundPanel);
        scrollPane.setBounds(0, 0, 1280, 720);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // === Левая PNG панель поверх фона ===
        JPanel leftPanel = new BackgroundPanelStrict("/Vector 10 (1).png", false, 0.8f); // <- PNG с кривым краем
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setBounds(0, 0, 339, 720); // фиксировано слева поверх фона

        // === Добавляем в слои ===
        layeredPane.add(scrollPane, JLayeredPane.DEFAULT_LAYER);      // фон с прокруткой
        layeredPane.add(leftPanel, JLayeredPane.PALETTE_LAYER);       // PNG поверх

        setContentPane(layeredPane);
        setTitle("Окно мониторинга");
        pack(); // ← сначала pack!
        setSize(1296, 720); // ← подбираешь вручную, чтобы всё влезло
        setLocationRelativeTo(null);
        setVisible(true);

    }
    private void addExampleComponents(JPanel panel) {
        Font fontSF = CustomFontLoader.loadCustomFont(24, "/fonts/SFProText-Bold.ttf");

        for (int i = 1; i <= 15; i++) {
            JTextField field = new RoundedTextField("Поле " + i, 20, 20);
            field.setFont(fontSF);
            field.setAlignmentX(Component.LEFT_ALIGNMENT);
            field.setHorizontalAlignment(SwingConstants.LEFT);
            field.setPreferredSize(new Dimension(400, 50));
            field.setMaximumSize(new Dimension(400, 50));
            field.setMinimumSize(new Dimension(400, 50));
            field.setMargin(new Insets(5, 10, 5, 10));

            panel.add(field);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JButton monitoringStartButton = new JButton("Начать мониторинг");
        JButton monitoringStopButton = new JButton("Завершить мониторинг");

        monitoringStartButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        monitoringStopButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        monitoringStartButton.addActionListener(e -> controller.startMonitoring());
        monitoringStopButton.addActionListener(e -> controller.stopMonitoring());

        panel.add(monitoringStartButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(monitoringStopButton);
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
        System.out.println("температурный текст" + viewModel.getTemperatureText());
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
