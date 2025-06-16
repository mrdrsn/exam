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
import java.awt.Point;
import model.MeasurementViewModel;
import model.Patient;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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

    private JLabel patientNameLabel;
    private JLabel patientIdLabel;
    private JLabel currentTempLabel;
    private JLabel currentHrLabel;
    private JLabel currentCvpLabel;

    private JButton startMonitoringButton;
    private JButton endMonitoringButton;

    private JPanel recentTempPanel;
    private JPanel recentHrPanel;
    private JPanel recentCvpPanel;

    private Map<String, String> patientInfo = new HashMap<>();

    private JPanel monitoringPanel = new JPanel();
    private JButton fullScreenButton;

    private Patient currentPatient;

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
        openPatientListUID(currentPatient);
//        setupUI();
    }

    private void setupUI() {
        clearContentPane();

        JPanel panel = new JPanel(new GridLayout(3, 1));
        JButton newPatientButton = new JButton("Новый пациент");
        JButton openPatientButton = new JButton("Открыть пациента");
        JButton exitButton = new JButton("Выход");

        newPatientButton.addActionListener((ActionEvent e) -> newPatientUID());
        openPatientButton.addActionListener((ActionEvent e) -> openPatientListUID(currentPatient));
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

    private void openPatientListUID(Patient patient) {
        clearContentPane();

        // === Основной контейнер с наложением ===
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1296, 720)); // 1280 + 16 на скроллбар
        layeredPane.setLayout(null); // ручное позиционирование

        // === Фоновая панель с компонентами ===
        BackgroundPanelStrict backgroundPanel = new BackgroundPanelStrict("/Frame 24 (3).jpg", false);
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.X_AXIS));
        backgroundPanel.setOpaque(false);

        JPanel helperPanel = new JPanel();
        helperPanel.setPreferredSize(new Dimension(340, 1200));
        helperPanel.setMinimumSize(helperPanel.getPreferredSize());
        helperPanel.setMaximumSize(helperPanel.getPreferredSize());
        helperPanel.setOpaque(false);

        if (patient != null) {
            controller.setPatient(patient); // восстановили
            preparePatientPanel(monitoringPanel); // наполнили данными
        }
        monitoringPanel.setLayout(new BoxLayout(monitoringPanel, BoxLayout.Y_AXIS));
        monitoringPanel.setPreferredSize(new Dimension(940, 1200));
        monitoringPanel.setMaximumSize(monitoringPanel.getPreferredSize());
        monitoringPanel.setMinimumSize(monitoringPanel.getPreferredSize());
        monitoringPanel.setOpaque(false);

        backgroundPanel.add(helperPanel);
        backgroundPanel.add(monitoringPanel);

        JScrollPane scrollPane = new JScrollPane(backgroundPanel);
        scrollPane.setBounds(0, 0, 1280, 720);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Ensure viewport starts at the top
        scrollPane.getViewport().setViewPosition(new Point(0, 0));

        // Adjust the background panel to align content at the top
        backgroundPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        // === Левая PNG панель поверх фона ===
        JPanel leftPanel = new BackgroundPanelStrict("/Vector 10.png", false, 0.8f);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setBounds(0, 0, 339, 720); // фиксировано слева поверх фона

        JPanel patientListPanel = new JPanel();
        patientListPanel.setLayout(new BoxLayout(patientListPanel, BoxLayout.Y_AXIS));
        patientListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        patientListPanel.setPreferredSize(new Dimension(275, 720));
        patientListPanel.setMinimumSize(new Dimension(275, 720));
        patientListPanel.setMaximumSize(new Dimension(275, 720));
        patientListPanel.setOpaque(false);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(300, 45));
        buttonPanel.setMinimumSize(buttonPanel.getPreferredSize());
        buttonPanel.setMaximumSize(buttonPanel.getPreferredSize());

        JButton backToMenuButton = createIconButton("/icon_back.png");
        fullScreenButton = createIconButton("/icon_fullscrean.png");
        if (patient == null) {
            fullScreenButton.setEnabled(false);
        }
        patientListPanel.add(Box.createVerticalStrut(30));
        patientListPanel.add(buttonPanel);
        backToMenuButton.addActionListener(e -> {
            setupUI();
        });
        fullScreenButton.addActionListener(e -> {
            fullScreenMode();
        });
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(backToMenuButton);
        buttonPanel.add(Box.createHorizontalStrut(30));
        buttonPanel.add(fullScreenButton);
        createPatientList(patientListPanel);
        leftPanel.add(patientListPanel);
        // === Добавляем в слои ===
        layeredPane.add(scrollPane, JLayeredPane.DEFAULT_LAYER);      // фон с прокруткой
        layeredPane.add(leftPanel, JLayeredPane.PALETTE_LAYER);       // PNG поверх

        setContentPane(layeredPane);
        setTitle("Окно мониторинга");
        pack();
        setSize(1296, 755);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void fullScreenMode() {
        this.currentPatient = controller.getCurrentPatient();
        clearContentPane();

        BackgroundPanelStrict backgroundPanel = new BackgroundPanelStrict("/Frame 24 (3).jpg", false);
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.X_AXIS));
        backgroundPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(backgroundPanel);
        scrollPane.setBounds(0, 0, 1280, 720);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setViewPosition(new Point(0, 0));
        backgroundPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        JPanel helperPanel = new JPanel();
        helperPanel.setLayout(new BoxLayout(helperPanel, BoxLayout.Y_AXIS));
        helperPanel.setPreferredSize(new Dimension(50, 1200));
        helperPanel.setMinimumSize(helperPanel.getPreferredSize());
        helperPanel.setMaximumSize(helperPanel.getPreferredSize());
        helperPanel.setOpaque(false);

        monitoringPanel.setPreferredSize(new Dimension(940, 1200));
        monitoringPanel.setMaximumSize(monitoringPanel.getPreferredSize());
        monitoringPanel.setMinimumSize(monitoringPanel.getPreferredSize());
        monitoringPanel.setOpaque(false);

        JButton backToSmallButton = createIconButton("/icon_smallscreen.png");
        backToSmallButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToSmallButton.addActionListener(e -> {
            backgroundPanel.removeAll();
            backgroundPanel.repaint();
            backgroundPanel.revalidate();
            monitoringPanel.removeAll();
            monitoringPanel.repaint();
            monitoringPanel.revalidate();
            openPatientListUID(currentPatient);
        });

        helperPanel.add(Box.createVerticalStrut(15));
        helperPanel.add(backToSmallButton);
        backgroundPanel.add(helperPanel);
        backgroundPanel.add(monitoringPanel);
        
        monitoringPanel.removeAll();
        preparePatientPanel(monitoringPanel);

        monitoringPanel.repaint();
        monitoringPanel.revalidate();
        setContentPane(scrollPane);
        setTitle("Окно мониторинга");
        pack();
        setSize(1296, 755);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private JButton createIconButton(String path) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        JButton button = new JButton(icon);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setPreferredSize(new Dimension(35, 35));
        button.setMinimumSize(new Dimension(35, 35));
        button.setMaximumSize(new Dimension(35, 35));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        return button;
    }

    private void createPatientList(JPanel panel) {
        panel.add(Box.createVerticalStrut(30));
        patientInfo = controller.getAllPatientFullNames();

        for (Map.Entry<String, String> entry : patientInfo.entrySet()) {
            PatientListItem item = new PatientListItem(entry.getValue());
            item.setPreferredSize(new Dimension(235, 35));
            item.setMaximumSize(new Dimension(235, 35));
            item.setMinimumSize(new Dimension(235, 35));
            panel.add(item);
            panel.add(Box.createVerticalStrut(13));
            item.addActionListener(e -> {
                fullScreenButton.setEnabled(true);
                if (controller.isMonitoringActive()) {
                    JOptionPane.showMessageDialog(this, "Сначала остановите мониторинг", "Ошибка",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    controller.setPatient(new Patient(entry.getKey(), controller.getPatientNameFromLogFile(entry.getKey())));
                    System.out.println(controller.getCurrentPatient());
                    monitoringPanel.removeAll();
                    preparePatientPanel(monitoringPanel);
                }
            });
        }
    }

    public void updateUID(MeasurementViewModel viewModel) {
        currentTempLabel.setText(viewModel.getTemperatureText());
        currentHrLabel.setText(viewModel.getHeartRateText());
        currentCvpLabel.setText(viewModel.getCvpText());
        String cleaned = viewModel.getTemperatureText().replaceAll("[^\\d,\\.]", "");
        cleaned = cleaned.replace(',', '.');
        if(Double.parseDouble(cleaned) >= 37.0){
            currentTempLabel.setForeground(Color.red);
        } else{
             currentTempLabel.setForeground(Color.black);
        }
        if(Double.parseDouble(viewModel.getHeartRateText()) > 110 || Double.parseDouble(viewModel.getHeartRateText()) < 60){
            currentHrLabel.setForeground(Color.red);
        } else{
            currentHrLabel.setForeground(Color.black);
        }
        if(Double.parseDouble(viewModel.getCvpText()) < 5 || Double.parseDouble(viewModel.getCvpText()) > 12){
            currentCvpLabel.setForeground(Color.red);
        } else{
            currentCvpLabel.setForeground(Color.black);
        }
        
                

        monitoringPanel.revalidate();
        monitoringPanel.repaint();
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

    private void preparePatientPanel(JPanel rightPanel) {
        Font fontSFMedium = CustomFontLoader.loadCustomFont(64, "fonts/SFProText-Medium.ttf");
        Font fontSFMediumS = CustomFontLoader.loadCustomFont(40, "fonts/SFProText-Medium.ttf");
        Font fontSFMediumT = CustomFontLoader.loadCustomFont(24, "fonts/SFProText-Medium.ttf");
        Font fontSF = CustomFontLoader.loadCustomFont(16, "fonts/SFProText-Regular.ttf");

        patientNameLabel = new JLabel();
        patientNameLabel.setText(controller.getCurrentPatient().getFullName());
        patientNameLabel.setFont(fontSFMedium);
        patientNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        patientNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        patientIdLabel = new JLabel();
        patientIdLabel.setText(controller.getCurrentPatient().getId());
        patientIdLabel.setFont(fontSFMedium);
        patientIdLabel.setHorizontalAlignment(SwingConstants.LEFT);
        patientIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel currentMeasurementsLabel = new JLabel("Текущие значения показателей");
        currentMeasurementsLabel.setFont(fontSFMediumS);
        currentMeasurementsLabel.setHorizontalAlignment(SwingConstants.LEFT);
        currentMeasurementsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel currentTempPanel = createPanelForCurrents();
        JPanel currentHrPanel = createPanelForCurrents();
        JPanel currentCvpPanel = createPanelForCurrents();

        JLabel currentTemp = new JLabel("Температура, °C : ");
        currentTemp.setFont(fontSFMediumT);
        currentTemp.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentTempLabel = new JLabel();
        currentTempLabel.setFont(fontSFMediumT);
        currentTempPanel.add(Box.createHorizontalStrut(10));
        currentTempPanel.add(currentTemp);
        currentTempPanel.add(Box.createHorizontalStrut(160));
        currentTempPanel.add(currentTempLabel);

        JLabel currentHr = new JLabel("Сердечный ритм, уд/мин :");
        currentHr.setFont(fontSFMediumT);
        currentHr.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentHrLabel = new JLabel();
        currentHrLabel.setFont(fontSFMediumT);

        currentHrPanel.add(Box.createHorizontalStrut(10));
        currentHrPanel.add(currentHr);
        currentHrPanel.add(Box.createHorizontalStrut(107));
        currentHrPanel.add(currentHrLabel);

        JLabel currentCvp = new JLabel("ЦВД, мм рт. ст. :");
        currentCvp.setFont(fontSFMediumT);
        currentCvp.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentCvpLabel = new JLabel();
        currentCvpLabel.setFont(fontSFMediumT);

        currentCvpPanel.add(Box.createHorizontalStrut(10));
        currentCvpPanel.add(currentCvp);
        currentCvpPanel.add(Box.createHorizontalStrut(235));
        currentCvpPanel.add(currentCvpLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setPreferredSize(new Dimension(475, 65));
        buttonPanel.setMaximumSize(buttonPanel.getPreferredSize());
        buttonPanel.setMinimumSize(buttonPanel.getPreferredSize());
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        startMonitoringButton = new RoundedButton("Начать мониторинг");
        startMonitoringButton.setPreferredSize(new Dimension(230, 60));
        startMonitoringButton.setMinimumSize(startMonitoringButton.getPreferredSize());
        startMonitoringButton.setMaximumSize(startMonitoringButton.getPreferredSize());
        startMonitoringButton.setBackground(Color.decode("#e0eef2"));
        startMonitoringButton.setFont(fontSF);

        endMonitoringButton = new RoundedButton("Завершить мониторинг");
        endMonitoringButton.setPreferredSize(new Dimension(230, 60));
        endMonitoringButton.setMinimumSize(endMonitoringButton.getPreferredSize());
        endMonitoringButton.setMaximumSize(endMonitoringButton.getPreferredSize());
        endMonitoringButton.setBackground(Color.decode("#d9d9d9"));
        endMonitoringButton.setFont(fontSF);

        startMonitoringButton.addActionListener(e -> controller.startMonitoring());
        endMonitoringButton.addActionListener(e -> controller.stopMonitoring());

        buttonPanel.add(startMonitoringButton);
        buttonPanel.add(endMonitoringButton);

        JLabel recentMeasurementsLabel = new JLabel("Предыдущие значения показателей");
        recentMeasurementsLabel.setFont(fontSFMediumS);
        recentMeasurementsLabel.setHorizontalAlignment(SwingConstants.LEFT);
        recentMeasurementsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel recentsPanel = new JPanel();
        recentsPanel.setLayout(new BoxLayout(recentsPanel, BoxLayout.X_AXIS));
        recentsPanel.setPreferredSize(new Dimension(900, 316));
        recentsPanel.setMaximumSize(recentsPanel.getPreferredSize());
        recentsPanel.setMinimumSize(recentsPanel.getPreferredSize());
        recentsPanel.setOpaque(false);
        recentsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel tempPanel = createHolderPanel(213);
        JPanel hrPanel = createHolderPanel(310);
        JPanel cvpPanel = createHolderPanel(215);

        JLabel recentTemp = new JLabel("Температура, °C");
        recentTemp.setFont(fontSFMediumT);
        recentTemp.setAlignmentX(Component.LEFT_ALIGNMENT);
        recentTemp.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel recentHr = new JLabel("Сердечный ритм, уд/мин");
        recentHr.setFont(fontSFMediumT);
        recentHr.setAlignmentX(Component.LEFT_ALIGNMENT);
        recentHr.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel recentCvp = new JLabel("ЦВД, мм рт. ст.");
        recentCvp.setFont(fontSFMediumT);
        recentCvp.setAlignmentX(Component.LEFT_ALIGNMENT);
        recentCvp.setHorizontalAlignment(SwingConstants.LEFT);

        recentTempPanel = createPanelForRecents();
        recentHrPanel = createPanelForRecents();
        recentCvpPanel = createPanelForRecents();

        tempPanel.add(recentTemp);
        tempPanel.add(Box.createVerticalStrut(16));
        tempPanel.add(recentTempPanel);

        hrPanel.add(recentHr);
        hrPanel.add(Box.createVerticalStrut(16));
        hrPanel.add(recentHrPanel);

        cvpPanel.add(recentCvp);
        cvpPanel.add(Box.createVerticalStrut(16));
        cvpPanel.add(recentCvpPanel);

        recentsPanel.add(tempPanel);
        recentsPanel.add(Box.createHorizontalStrut(74));
        recentsPanel.add(hrPanel);
        recentsPanel.add(Box.createHorizontalStrut(34));
        recentsPanel.add(cvpPanel);

        JLabel otherInfoLabel = new JLabel("Доп информация:");
        otherInfoLabel.setFont(fontSF);
        otherInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        otherInfoLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Start content at the top
        rightPanel.add(Box.createVerticalStrut(44)); // Only necessary spacing
        rightPanel.add(patientNameLabel);
        rightPanel.add(patientIdLabel);
        rightPanel.add(Box.createVerticalStrut(44)); // Only necessary spacing
        rightPanel.add(currentMeasurementsLabel);
        rightPanel.add(Box.createVerticalStrut(30));
        rightPanel.add(currentTempPanel);
        rightPanel.add(Box.createVerticalStrut(19));
        rightPanel.add(currentHrPanel);
        rightPanel.add(Box.createVerticalStrut(19));
        rightPanel.add(currentCvpPanel);
        rightPanel.add(Box.createVerticalStrut(40));
        rightPanel.add(buttonPanel);
        rightPanel.add(Box.createVerticalStrut(40));
        rightPanel.add(recentMeasurementsLabel);
        rightPanel.add(Box.createVerticalStrut(80));
        rightPanel.add(recentsPanel);
        rightPanel.add(Box.createVerticalStrut(40));
        rightPanel.add(otherInfoLabel);

        rightPanel.repaint();
        rightPanel.revalidate();

    }

    private JPanel createPanelForCurrents() {
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(new BoxLayout(currentPanel, BoxLayout.X_AXIS));
        currentPanel.setPreferredSize(new Dimension(490, 45));
        currentPanel.setMaximumSize(currentPanel.getPreferredSize());
        currentPanel.setMinimumSize(currentPanel.getPreferredSize());
        currentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentPanel.setBackground(Color.decode("#e2f0f5"));
        return currentPanel;
    }

    private JPanel createPanelForRecents() {
        JPanel recentTempPanel = new RoundedPanel(20);
        recentTempPanel.setPreferredSize(new Dimension(213, 266));
        recentTempPanel.setMaximumSize(recentTempPanel.getPreferredSize());
        recentTempPanel.setMinimumSize(recentTempPanel.getPreferredSize());
        recentTempPanel.setBackground(Color.decode("#e9e9e9"));
        recentTempPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        recentTempPanel.setLayout(new BoxLayout(recentTempPanel, BoxLayout.Y_AXIS));
        return recentTempPanel;
    }

    private JPanel createHolderPanel(int x) {
        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.Y_AXIS));
        tempPanel.setPreferredSize(new Dimension(x, 316));
        tempPanel.setMaximumSize(tempPanel.getPreferredSize());
        tempPanel.setMinimumSize(tempPanel.getPreferredSize());
        tempPanel.setOpaque(false);
        tempPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return tempPanel;
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
