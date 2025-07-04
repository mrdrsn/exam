package view;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import model.MeasurementViewModel;
import model.Patient;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import model.Measurement;

public class GUIMeasurement extends JFrame {

    private Controller controller;
    private JLabel tempLabel = new JLabel("-");
    private JLabel heartRateLabel = new JLabel("-");
    private JLabel cvpLabel = new JLabel("-");
    private JButton backButton;

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
    private JScrollPane monitoringScrollPane;

    private JLabel nonStandartTimeTemp;
    private JLabel nonStandartTimeHr;
    private JLabel nonStandartTimeCvp;

    private JLabel healTimeTemp;
    private JLabel healTimeHr;
    private JLabel healTimeCvp;

    private DefaultTableModel statsTableModel;

    private JTable statsTable;
    private String[] rowNames = {"Температура", "Сердцебиение", "ЦВД"};

    public GUIMeasurement() {
        this.controller = new Controller(this);
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
        setupUI();
        setResizable(false);
    }

    private JButton createCustomButton(String text, String color) {
        Font SFSemiBold = CustomFontLoader.loadCustomFont(16, "fonts/SFProText-Semibold.ttf");
        JButton button = new RoundedButton(text);
        button.setBackground(Color.decode(color));
        button.setPreferredSize(new Dimension(220, 60));
        button.setMaximumSize(button.getPreferredSize());
        button.setMinimumSize(button.getPreferredSize());
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(SFSemiBold);
        return button;
    }

    private void setupUI() {
        clearContentPane();
        Font fontSF = CustomFontLoader.loadCustomFont(16, "fonts/SFProText-Regular.ttf");

        JPanel backgroundPanel = new BackgroundPanel("/Frame 15 (4).jpg", false);
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.X_AXIS));
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        adjustComponent(leftPanel, 630, 720, false);
        leftPanel.add(Box.createVerticalStrut(650));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
        adjustComponent(rightPanel, 650, 720, false);

        JPanel check = new JPanel();
        adjustComponent(check, 145, 720, false);
        JPanel check2 = new JPanel();
        adjustComponent(check2, 145, 720, false);

        JButton newPatientButton = createCustomButton("Новый пациент", "#ffffff");
        JButton openPatientButton = createCustomButton("Открыть пациента", "#ffffff");
        JButton exitButton = createCustomButton("Выход", "#f9dfdf");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        adjustComponent(buttonPanel, 350, 450, false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createVerticalStrut(108));
        buttonPanel.add(newPatientButton);
        buttonPanel.add(Box.createVerticalStrut(27));
        buttonPanel.add(openPatientButton);
        buttonPanel.add(Box.createVerticalStrut(27));
        buttonPanel.add(exitButton);

        rightPanel.add(check);
        rightPanel.add(buttonPanel);
        rightPanel.add(check2);

        newPatientButton.addActionListener((ActionEvent e) -> newPatientUID());
        openPatientButton.addActionListener((ActionEvent e) -> openPatientListUID(currentPatient));
        exitButton.addActionListener((ActionEvent e) -> System.exit(0));

        backgroundPanel.add(leftPanel);
        backgroundPanel.add(rightPanel);
        setContentPane(backgroundPanel);
        setTitle("Главное меню");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void newPatientUID() {
        clearContentPane();
        JPanel mainPanel = new BackgroundPanel("/Frame 19 (1).jpg", false);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        alignCustomPanel(leftPanel, 612, 720);

        Font fontSFBig = CustomFontLoader.loadCustomFont(48, "fonts/SFProText-Medium.ttf");
        Font fontSF = CustomFontLoader.loadCustomFont(16, "fonts/SFProText-Regular.ttf");
        Font fontSFSemiBold = CustomFontLoader.loadCustomFont(24, "fonts/SFProText-Medium.ttf");

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        adjustComponent(rightPanel, 668, 720, false);

        JLabel newPatientLabel = new JLabel("Новый пациент...");
        adjustLabel(newPatientLabel, fontSFBig);

        JTextField enterSurname = createCustomTextField("Фамилия*", 20, 20);
        JTextField enterName = createCustomTextField("Имя*", 20, 20);
        JTextField enterMiddleName = createCustomTextField("Отчество", 20, 20);

        JPanel idInfoPanel = new JPanel();
        idInfoPanel.setLayout(new BoxLayout(idInfoPanel, BoxLayout.X_AXIS));
        adjustComponent(idInfoPanel, 500, 43, false);

        JPanel leftInfoPanel = new JPanel();
        leftInfoPanel.setLayout(new BoxLayout(leftInfoPanel, BoxLayout.Y_AXIS));
        adjustComponent(leftInfoPanel, 400, 43, false);

        JLabel idInfoLabel1 = new JLabel("Новому пациенту будет присвоен уникальный");
        adjustLabel(idInfoLabel1, fontSF);

        JLabel idInfoLabel2 = new JLabel("идентификационный номер:");
        adjustLabel(idInfoLabel2, fontSF);

        leftInfoPanel.add(idInfoLabel1);
        leftInfoPanel.add(idInfoLabel2);

        JPanel rightInfoPanel = new JPanel();
        rightInfoPanel.setLayout(new BoxLayout(rightInfoPanel, BoxLayout.Y_AXIS));
        adjustComponent(rightInfoPanel, 100, 43,false);

        JLabel idInfoLabel3 = new JLabel(controller.generateNewPatientId());
        adjustLabel(idInfoLabel3, fontSFSemiBold);

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
            openPatientListUID(controller.getCurrentPatient());
        });
        backToMenuButton.addActionListener((ActionEvent e) -> {
            setupUI();
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
        setLocationRelativeTo(null);
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
        Font fontSF = CustomFontLoader.loadCustomFont(16, "fonts/SFProText-Regular.ttf");
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(x, y));
        panel.setMinimumSize(panel.getPreferredSize());
        panel.setMaximumSize(panel.getPreferredSize());
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel label = new JLabel("Заполните всю информацию о пациенте");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(fontSF);
        panel.add(Box.createVerticalStrut(575));
        panel.add(label);
    }

    private void openPatientListUID(Patient patient) {
        clearContentPane();
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1296, 720));
        layeredPane.setLayout(null);

        BackgroundPanelStrict backgroundPanel = new BackgroundPanelStrict("/Gradient.jpg", false);
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.X_AXIS));
        backgroundPanel.setOpaque(false);

        JPanel helperPanel = new JPanel();
        adjustComponent(helperPanel, 340, 1948, false);

        if (patient != null) {
            preparePatientPanel(monitoringPanel);
            Measurement last = controller.getLastMeasurement();
            if (last != null) {
                MeasurementViewModel viewModel = new MeasurementViewModel(
                        last.getTemperature(),
                        last.getHeartRate(),
                        last.getCvp()
                );
                updateUID(viewModel);
            }
            prepareRecentsPanel();
            updateStatisticsTable();

        }
        monitoringPanel.setLayout(new BoxLayout(monitoringPanel, BoxLayout.Y_AXIS));
        adjustComponent(monitoringPanel, 940, 1948, false);

        backgroundPanel.add(helperPanel);
        backgroundPanel.add(monitoringPanel);

        monitoringScrollPane = new JScrollPane(backgroundPanel);
        monitoringScrollPane.setBounds(0, 0, 1280, 720);
        monitoringScrollPane.setOpaque(false);
        monitoringScrollPane.getViewport().setOpaque(false);
        monitoringScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        monitoringScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        monitoringScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        monitoringScrollPane.getViewport().setViewPosition(new Point(0, 0));
        backgroundPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        JPanel leftPanel = new BackgroundPanelStrict("/Vector 10 (4).png", false, 0.8f);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setBounds(0, 0, 339, 720);

        JPanel patientListPanel = new JPanel();
        patientListPanel.setLayout(new BoxLayout(patientListPanel, BoxLayout.Y_AXIS));
        adjustComponent(patientListPanel, 275, 900, false);

        JScrollPane patientListScrollPane = new JScrollPane(patientListPanel);
        patientListScrollPane.setBounds(0, 0, 275, 720);
        patientListScrollPane.setOpaque(false);
        patientListScrollPane.getViewport().setOpaque(false);
        patientListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        patientListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        patientListScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        patientListScrollPane.setBackground(new Color(0, 0, 0, 0));

        patientListScrollPane.setBorder(null);
        JScrollBar verticalScrollBar = patientListScrollPane.getVerticalScrollBar();
        verticalScrollBar.setVisible(false);

        patientListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
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

        leftPanel.add(patientListScrollPane);
        layeredPane.add(monitoringScrollPane, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(leftPanel, JLayeredPane.PALETTE_LAYER);

        setContentPane(layeredPane);
        setTitle("Окно мониторинга");
        pack();
        setSize(1280, 755);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void fullScreenMode() {
        this.currentPatient = controller.getCurrentPatient();
        clearContentPane();

        BackgroundPanelStrict backgroundPanel = new BackgroundPanelStrict("/Frame 27.jpg", false);
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
        adjustComponent(helperPanel, 50, 1948, false);
        adjustComponent(monitoringPanel, 1200, 1948, false);

        JButton backToSmallButton = createIconButton("/icon_smallscreen.png");
        backToSmallButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToSmallButton.addActionListener(e -> {
            backgroundPanel.removeAll();
            monitoringPanel.removeAll();
            openPatientListUID(currentPatient);
            prepareRecentsPanel();
            updateStatisticsTable();

            backgroundPanel.repaint();
            backgroundPanel.revalidate();
            monitoringPanel.repaint();
            monitoringPanel.revalidate();
        });

        helperPanel.add(Box.createVerticalStrut(15));
        helperPanel.add(backToSmallButton);
        backgroundPanel.add(helperPanel);
        backgroundPanel.add(monitoringPanel);

        monitoringPanel.removeAll();
        preparePatientPanel(monitoringPanel);
        Measurement last = controller.getLastMeasurement();
        if (last != null) {
            MeasurementViewModel viewModel = new MeasurementViewModel(
                    last.getTemperature(),
                    last.getHeartRate(),
                    last.getCvp()
            );
            updateUID(viewModel);
        }

        prepareRecentsPanel();
        updateStatisticsTable();

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

    private void prepareRecentsPanel() {
        recentTempPanel.removeAll();
        recentHrPanel.removeAll();
        recentCvpPanel.removeAll();
        Font fontSF = CustomFontLoader.loadCustomFont(16, "fonts/SFProText-Regular.ttf");
        List<String> recentTemps = controller.getTemperaturesFromMeasurements(controller.getCurrentPatient().getId());
        List<String> recentHrs = controller.getHeartRateFromMeasurements(controller.getCurrentPatient().getId());
        List<String> recentCvps = controller.getCvpFromMeasurements(controller.getCurrentPatient().getId());
        if (recentTemps == null) {
            JLabel nullLabel = new JLabel("-");
            recentTempPanel.setLayout(new BorderLayout());
            nullLabel.setHorizontalAlignment(SwingConstants.CENTER);
            nullLabel.setFont(fontSF);
            recentTempPanel.add(nullLabel, BorderLayout.CENTER);
        } else {
            recentTempPanel.setLayout(new GridLayout(5, 2));
            for (String t : recentTemps) {
                JLabel tl = new JLabel(t);
                tl.setHorizontalAlignment(SwingConstants.CENTER);
                tl.setFont(fontSF);
                recentTempPanel.add(tl);
            }
        }
        if (recentHrs == null) {
            recentHrPanel.setLayout(new BorderLayout());
            JLabel nullLabel = new JLabel("-");
            nullLabel.setHorizontalAlignment(SwingConstants.CENTER);
            nullLabel.setFont(fontSF);
            recentHrPanel.add(nullLabel, BorderLayout.CENTER);
        } else {
            recentHrPanel.setLayout(new GridLayout(5, 2));
            for (String hr : recentHrs) {
                JLabel hrl = new JLabel(hr);
                hrl.setFont(fontSF);
                hrl.setHorizontalAlignment(SwingConstants.CENTER);
                recentHrPanel.add(hrl);
            }
        }
        if (recentCvps == null) {
            recentCvpPanel.setLayout(new BorderLayout());
            JLabel nullLabel = new JLabel("-");
            nullLabel.setHorizontalAlignment(SwingConstants.CENTER);
            nullLabel.setFont(fontSF);
            recentCvpPanel.add(nullLabel, BorderLayout.CENTER);
        } else {
            recentCvpPanel.setLayout(new GridLayout(5, 2));
            for (String cvp : recentCvps) {
                JLabel cvpl = new JLabel(cvp);
                cvpl.setHorizontalAlignment(SwingConstants.CENTER);
                cvpl.setFont(fontSF);
                recentCvpPanel.add(cvpl);
            }
        }

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
                    patientNameLabel = new JLabel(item.getNameLabel().getText());
                    controller.setPatient(new Patient(entry.getKey(), controller.getPatientNameFromLogFile(entry.getKey())));
                    System.out.println(controller.getCurrentPatient());
                    monitoringPanel.removeAll();
                    preparePatientPanel(monitoringPanel);
                    prepareRecentsPanel();
                    monitoringScrollPane.getViewport().setViewPosition(new Point(0, 0));
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
        if (Double.parseDouble(cleaned) >= 37.0) {
            currentTempLabel.setForeground(Color.red);
        } else {
            currentTempLabel.setForeground(Color.black);
        }
        if (Double.parseDouble(viewModel.getHeartRateText()) > 110 || Double.parseDouble(viewModel.getHeartRateText()) < 60) {
            currentHrLabel.setForeground(Color.red);
        } else {
            currentHrLabel.setForeground(Color.black);
        }
        if (Double.parseDouble(viewModel.getCvpText()) < 5 || Double.parseDouble(viewModel.getCvpText()) > 12) {
            currentCvpLabel.setForeground(Color.red);
        } else {
            currentCvpLabel.setForeground(Color.black);
        }

        prepareRecentsPanel();
        nonStandartTimeTemp.setText(controller.getFirstCriticalTempTime(controller.getCurrentPatient().getId()));
        nonStandartTimeHr.setText(controller.getFirstCriticalHeartRateTime(controller.getCurrentPatient().getId()));
        nonStandartTimeCvp.setText(controller.getFirstCriticalCvpTime(controller.getCurrentPatient().getId()));
        healTimeTemp.setText(controller.getTimeToRecoveryForTemp());
        healTimeHr.setText(controller.getTimeToRecoveryForHeartRate());
        healTimeCvp.setText(controller.getTimeToRecoveryForCvp());
        updateStatisticsTable();

        monitoringPanel.revalidate();
        monitoringPanel.repaint();
    }

    private void preparePatientPanel(JPanel rightPanel) {
        rightPanel.removeAll();
        rightPanel.repaint();
        rightPanel.revalidate();
        Font fontSFMedium = CustomFontLoader.loadCustomFont(64, "fonts/SFProText-Medium.ttf");
        Font fontSFMediumS = CustomFontLoader.loadCustomFont(40, "fonts/SFProText-Medium.ttf");
        Font fontSFMediumT = CustomFontLoader.loadCustomFont(24, "fonts/SFProText-Medium.ttf");
        Font fontSF = CustomFontLoader.loadCustomFont(16, "fonts/SFProText-Regular.ttf");

        System.out.println("внутри preparePatientPanel()" + controller.getCurrentPatient());
        patientNameLabel = new JLabel();
        patientNameLabel.setText(controller.parseFullNameFromLog(controller.getCurrentPatient().getId()));
        patientNameLabel.setFont(fontSFMedium);
        patientNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        patientNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        patientIdLabel = new JLabel();
        patientIdLabel.setText(controller.getCurrentPatient().getId());
        patientIdLabel.setFont(fontSFMedium);
        patientIdLabel.setHorizontalAlignment(SwingConstants.LEFT);
        patientIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel currentHolder = new JPanel();
        currentHolder.setLayout(new BoxLayout(currentHolder, BoxLayout.Y_AXIS));
        currentHolder.setPreferredSize(new Dimension(945, 270));
        currentHolder.setMaximumSize(currentHolder.getPreferredSize());
        currentHolder.setMinimumSize(currentHolder.getPreferredSize());
        currentHolder.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentHolder.setOpaque(false);

        JPanel labelHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelHolder.setPreferredSize(new Dimension(945, 60));
        labelHolder.setMaximumSize(new Dimension(945, 60));
        labelHolder.setMinimumSize(new Dimension(945, 60));
        labelHolder.setOpaque(false);

        JPanel infoHolder = new JPanel();
        infoHolder.setLayout(new BoxLayout(infoHolder, BoxLayout.X_AXIS));
        infoHolder.setPreferredSize(new Dimension(945, 210));
        infoHolder.setMaximumSize(new Dimension(945, 210));
        infoHolder.setMinimumSize(new Dimension(945, 210));
        infoHolder.setOpaque(false);

        JPanel leftCurrentHolder = new JPanel();
        leftCurrentHolder.setLayout(new BoxLayout(leftCurrentHolder, BoxLayout.Y_AXIS));
        leftCurrentHolder.setPreferredSize(new Dimension(579, 210));
        leftCurrentHolder.setMaximumSize(new Dimension(579, 210));
        leftCurrentHolder.setMinimumSize(new Dimension(579, 210));
        leftCurrentHolder.setOpaque(false);

        JPanel pictureHolder = new JPanel(new BorderLayout());
        pictureHolder.setPreferredSize(new Dimension(366, 210));
        pictureHolder.setMaximumSize(new Dimension(366, 210));
        pictureHolder.setMinimumSize(new Dimension(366, 210));
        pictureHolder.setOpaque(false);

        JPanel picture = new BackgroundPanelStrict("/Lifesavers Card ID (1).png", false);
        pictureHolder.add(picture, BorderLayout.CENTER);

        infoHolder.add(leftCurrentHolder);
        infoHolder.add(pictureHolder);

        currentHolder.add(labelHolder);
        currentHolder.add(infoHolder);

        JLabel currentMeasurementsLabel = new JLabel("Текущие значения показателей");
        currentMeasurementsLabel.setFont(fontSFMediumS);
        currentMeasurementsLabel.setHorizontalAlignment(SwingConstants.LEFT);
        currentMeasurementsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        labelHolder.add(currentMeasurementsLabel);

        JPanel currentTempPanel = createPanelForCurrents();
        JPanel currentHrPanel = createPanelForCurrents();
        JPanel currentCvpPanel = createPanelForCurrents();

        JPanel labelHolderT = new JPanel();
        labelHolderT.setLayout(new BoxLayout(labelHolderT, BoxLayout.X_AXIS));
        labelHolderT.setOpaque(false);
        labelHolderT.setPreferredSize(new Dimension(300, 45));
        labelHolderT.setMaximumSize(labelHolderT.getPreferredSize());
        labelHolderT.setMinimumSize(labelHolderT.getPreferredSize());

        JPanel valueHolderT = new JPanel();
        valueHolderT.setLayout(new FlowLayout(FlowLayout.RIGHT));
        valueHolderT.setOpaque(false);
        valueHolderT.setPreferredSize(new Dimension(185, 45));
        valueHolderT.setMaximumSize(valueHolderT.getPreferredSize());
        valueHolderT.setMinimumSize(valueHolderT.getPreferredSize());

        JLabel currentTemp = new JLabel("Температура, °C : ");
        currentTemp.setFont(fontSFMediumT);
        currentTemp.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentTempLabel = new JLabel();
        currentTempLabel.setFont(fontSFMediumT);
        currentTempLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        currentTempLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        labelHolderT.add(Box.createHorizontalStrut(10));
        labelHolderT.add(currentTemp);
        valueHolderT.add(currentTempLabel);

        currentTempPanel.add(labelHolderT);
        currentTempPanel.add(valueHolderT);

        JPanel labelHolderH = new JPanel();
        labelHolderH.setLayout(new BoxLayout(labelHolderH, BoxLayout.X_AXIS));
        labelHolderH.setOpaque(false);
        labelHolderH.setPreferredSize(new Dimension(300, 45));
        labelHolderH.setMaximumSize(labelHolderH.getPreferredSize());
        labelHolderH.setMinimumSize(labelHolderH.getPreferredSize());

        JPanel valueHolderH = new JPanel();
        valueHolderH.setLayout(new FlowLayout(FlowLayout.RIGHT));
        valueHolderH.setOpaque(false);
        valueHolderH.setPreferredSize(new Dimension(185, 45));
        valueHolderH.setMaximumSize(valueHolderH.getPreferredSize());
        valueHolderH.setMinimumSize(valueHolderH.getPreferredSize());

        JLabel currentHr = new JLabel("Сердечный ритм, уд/мин :");
        currentHr.setFont(fontSFMediumT);
        currentHr.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentHrLabel = new JLabel();
        currentHrLabel.setFont(fontSFMediumT);

        labelHolderH.add(Box.createHorizontalStrut(10));
        labelHolderH.add(currentHr);
        valueHolderH.add(currentHrLabel);

        currentHrPanel.add(labelHolderH);
        currentHrPanel.add(valueHolderH);

        JPanel labelHolderC = new JPanel();
        labelHolderC.setLayout(new BoxLayout(labelHolderC, BoxLayout.X_AXIS));
        labelHolderC.setOpaque(false);
        labelHolderC.setPreferredSize(new Dimension(300, 45));
        labelHolderC.setMaximumSize(labelHolderC.getPreferredSize());
        labelHolderC.setMinimumSize(labelHolderC.getPreferredSize());

        JPanel valueHolderC = new JPanel();
        valueHolderC.setLayout(new FlowLayout(FlowLayout.RIGHT));
        valueHolderC.setOpaque(false);
        valueHolderC.setPreferredSize(new Dimension(185, 45));
        valueHolderC.setMaximumSize(valueHolderH.getPreferredSize());
        valueHolderC.setMinimumSize(valueHolderH.getPreferredSize());

        JLabel currentCvp = new JLabel("ЦВД, мм рт. ст. :");
        currentCvp.setFont(fontSFMediumT);
        currentCvp.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentCvpLabel = new JLabel();
        currentCvpLabel.setFont(fontSFMediumT);

        labelHolderC.add(Box.createHorizontalStrut(10));
        labelHolderC.add(currentCvp);
        valueHolderC.add(currentCvpLabel);

        currentCvpPanel.add(labelHolderC);
        currentCvpPanel.add(valueHolderC);

        leftCurrentHolder.add(Box.createVerticalStrut(30));
        leftCurrentHolder.add(currentTempPanel);
        leftCurrentHolder.add(Box.createVerticalStrut(19));
        leftCurrentHolder.add(currentHrPanel);
        leftCurrentHolder.add(Box.createVerticalStrut(19));
        leftCurrentHolder.add(currentCvpPanel);

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
        startMonitoringButton.setBackground(Color.decode("#cec6eb"));
        startMonitoringButton.setFont(fontSF);

        endMonitoringButton = new RoundedButton("Завершить мониторинг");
        endMonitoringButton.setPreferredSize(new Dimension(230, 60));
        endMonitoringButton.setMinimumSize(endMonitoringButton.getPreferredSize());
        endMonitoringButton.setMaximumSize(endMonitoringButton.getPreferredSize());
        endMonitoringButton.setBackground(Color.decode("#a190d3"));
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

        rightPanel.add(Box.createVerticalStrut(44)); 
        rightPanel.add(patientNameLabel);
        rightPanel.add(patientIdLabel);
        rightPanel.add(Box.createVerticalStrut(44)); 
        rightPanel.add(currentHolder);
        rightPanel.add(Box.createVerticalStrut(40));
        rightPanel.add(buttonPanel);
        rightPanel.add(Box.createVerticalStrut(40));
        createExtraData(rightPanel);
        rightPanel.add(recentMeasurementsLabel);
        rightPanel.add(Box.createVerticalStrut(40));
        rightPanel.add(recentsPanel);

        rightPanel.repaint();
        rightPanel.revalidate();

    }

    private void createExtraData(JPanel panel) {
        Font fontSFMediumT = CustomFontLoader.loadCustomFont(24, "fonts/SFProText-Medium.ttf");
        Font fontSFMediumS = CustomFontLoader.loadCustomFont(20, "fonts/SFProText-Medium.ttf");
        Font fontSF = CustomFontLoader.loadCustomFont(16, "fonts/SFProText-Regular.ttf");

        JPanel timePanel = new JPanel();
        timePanel.setLayout(new GridLayout(1, 2));
        adjustComponent(timePanel, 1200, 250, false);

        JPanel nonStandartPanel = new JPanel();
        nonStandartPanel.setOpaque(false);
        nonStandartPanel.setLayout(new BoxLayout(nonStandartPanel, BoxLayout.Y_AXIS));

        JLabel nonStandartLabel = new JLabel("Фиксация нестандартного режима");
        nonStandartLabel.setFont(fontSFMediumT);
        nonStandartLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        nonStandartLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel nonStandartTempPanel = new JPanel();
        nonStandartTempPanel.setLayout(new BoxLayout(nonStandartTempPanel, BoxLayout.X_AXIS));
        adjustComponent(nonStandartTempPanel, 420, 35, true);
        JLabel nonStandartTemp = new JLabel("Температура, °C : ");
        adjustLabel(nonStandartTemp, fontSFMediumS);

        JPanel holderNonStandartTemp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        adjustComponent(holderNonStandartTemp, 235, 35, false);

        nonStandartTimeTemp = new JLabel();
        nonStandartTimeTemp.setFont(fontSFMediumS);
        holderNonStandartTemp.add(nonStandartTimeTemp);

        nonStandartTempPanel.add(nonStandartTemp);
        nonStandartTempPanel.add(holderNonStandartTemp);

        JPanel nonStandartHrPanel = new JPanel();
        nonStandartHrPanel.setLayout(new BoxLayout(nonStandartHrPanel, BoxLayout.X_AXIS));
        adjustComponent(nonStandartHrPanel, 420, 35, true);
        JLabel nonStandartHr = new JLabel("Сердечный ритм, уд/мин :");
        adjustLabel(nonStandartHr, fontSFMediumS);

        JPanel holderNonStandartHr = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        adjustComponent(holderNonStandartHr, 157, 35, false);

        nonStandartTimeHr = new JLabel();
        nonStandartTimeHr.setFont(fontSFMediumS);
        holderNonStandartHr.add(nonStandartTimeHr);

        nonStandartHrPanel.add(nonStandartHr);
        nonStandartHrPanel.add(holderNonStandartHr);

        JPanel nonStandartCvpPanel = new JPanel();
        nonStandartCvpPanel.setLayout(new BoxLayout(nonStandartCvpPanel, BoxLayout.X_AXIS));
        adjustComponent(nonStandartCvpPanel, 420, 35, true);
        JLabel nonStandartCvp = new JLabel("ЦВД, мм рт. ст. :");
        adjustLabel(nonStandartCvp, fontSFMediumS);

        JPanel holderNonStandartCvp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        adjustComponent(holderNonStandartCvp, 256, 35, false);

        nonStandartTimeCvp = new JLabel();
        nonStandartTimeCvp.setFont(fontSFMediumS);
        holderNonStandartCvp.add(nonStandartTimeCvp);

        nonStandartCvpPanel.add(nonStandartCvp);
        nonStandartCvpPanel.add(holderNonStandartCvp);

        nonStandartPanel.add(nonStandartLabel);
        nonStandartPanel.add(Box.createVerticalStrut(35));
        nonStandartPanel.add(nonStandartTempPanel);
        nonStandartPanel.add(Box.createVerticalStrut(15));
        nonStandartPanel.add(nonStandartHrPanel);
        nonStandartPanel.add(Box.createVerticalStrut(15));
        nonStandartPanel.add(nonStandartCvpPanel);

        JPanel healTimePanel = new JPanel();
        healTimePanel.setOpaque(false);
        healTimePanel.setLayout(new BoxLayout(healTimePanel, BoxLayout.Y_AXIS));

        JLabel healTimeLabel = new JLabel("Восстановление стандартного режима");
        healTimeLabel.setFont(fontSFMediumT);
        healTimeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        healTimeLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel healTimeTempPanel = new JPanel();
        healTimeTempPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        adjustComponent(healTimeTempPanel, 420, 35, true);

        healTimeTemp = new JLabel();
        healTimeTemp.setFont(fontSFMediumS);

        healTimeTempPanel.add(healTimeTemp);

        JPanel healTimeHrPanel = new JPanel();
        healTimeHrPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        adjustComponent(healTimeHrPanel, 420, 35, true);

        healTimeHr = new JLabel();
        healTimeHr.setFont(fontSFMediumS);

        healTimeHrPanel.add(healTimeHr);

        JPanel healTimeCvpPanel = new JPanel();
        healTimeCvpPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        adjustComponent(healTimeCvpPanel, 420, 35, true);

        healTimeCvp = new JLabel();
        healTimeCvp.setFont(fontSFMediumS);

        healTimeCvpPanel.add(healTimeCvp);

        healTimePanel.add(healTimeLabel);
        healTimePanel.add(Box.createVerticalStrut(35));
        healTimePanel.add(healTimeTempPanel);
        healTimePanel.add(Box.createVerticalStrut(15));
        healTimePanel.add(healTimeHrPanel);
        healTimePanel.add(Box.createVerticalStrut(15));
        healTimePanel.add(healTimeCvpPanel);

        timePanel.add(nonStandartPanel);
        timePanel.add(healTimePanel);
        panel.add(timePanel);
        JLabel statsLabel = new JLabel("Статистические показатели");
        statsLabel.setFont(fontSFMediumT);
        statsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsLabel.setHorizontalAlignment(SwingConstants.LEFT);

        String[] extendedColumns = {"Показатель", "Среднее", "Мат. ожидание", "Дисперсия", "1 квартиль", "4 квартиль"};
        Object[][] initialData = {
            {"Температура", "-", "-", "-", "-", "-"},
            {"Сердцебиение", "-", "-", "-", "-", "-"},
            {"ЦВД", "-", "-", "-", "-", "-"}
        };

        statsTableModel = new DefaultTableModel(initialData, extendedColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        statsTable = new JTable(statsTableModel);
        statsTable.setFont(fontSF);
        statsTable.setRowHeight(30);
        statsTable.getTableHeader().setFont(fontSFMediumS);
        statsTable.setPreferredScrollableViewportSize(new Dimension(1000, 110));
        

        JScrollPane scrollPane = new JScrollPane(statsTable);
        scrollPane.setPreferredSize(new Dimension(1000, 150));
        scrollPane.setMaximumSize(new Dimension(1000, 150));
        scrollPane.setMinimumSize(new Dimension(1000, 150));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(Box.createVerticalStrut(50));
        panel.add(statsLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(scrollPane);
    }

    public void updateStatisticsTable() {
        String patientId = controller.getCurrentPatient().getId();

        List<Double> temps = controller.getAllTemperatures(patientId);
        List<Double> hr = controller.getAllHeartRates(patientId);
        List<Double> cvp = controller.getAllCvp(patientId);

        List<List<Double>> all = List.of(temps, hr, cvp);

        for (int i = 0; i < all.size(); i++) {

            statsTableModel.setValueAt(String.format("%.2f", controller.prepareForCalculations(all, i).getMean()), i, 1);
            statsTableModel.setValueAt(String.format("%.2f", controller.prepareForCalculations(all, i).getExpectedValue()), i, 2);
            statsTableModel.setValueAt(String.format("%.2f", controller.prepareForCalculations(all, i).getVariance()), i, 3);
            statsTableModel.setValueAt(String.format("%.2f", controller.prepareForCalculations(all, i).getFirstQuartile()), i, 4);
            statsTableModel.setValueAt(String.format("%.2f", controller.prepareForCalculations(all, i).getFourthQuartile()), i, 5);
        }
    }

    private void adjustLabel(JLabel label, Font font) {
        label.setFont(font);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.LEFT);
    }

    private void adjustComponent(JComponent component, int width, int height, boolean opaque) {
        component.setPreferredSize(new Dimension(width, height));
        component.setMaximumSize(component.getPreferredSize());
        component.setMinimumSize(component.getPreferredSize());
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        component.setOpaque(opaque);
    }

    private JPanel createPanelForCurrents() {
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(new BoxLayout(currentPanel, BoxLayout.X_AXIS));
        currentPanel.setPreferredSize(new Dimension(490, 45));
        currentPanel.setMaximumSize(currentPanel.getPreferredSize());
        currentPanel.setMinimumSize(currentPanel.getPreferredSize());
        currentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentPanel.setBackground(Color.decode("#e2e2f5"));
        return currentPanel;
    }

    private JPanel createPanelForRecents() {
        JPanel recentPanel = new RoundedPanel(20);
        recentPanel.setPreferredSize(new Dimension(213, 266));
        recentPanel.setMaximumSize(recentPanel.getPreferredSize());
        recentPanel.setMinimumSize(recentPanel.getPreferredSize());
        recentPanel.setBackground(Color.decode("#e9e9e9"));
        recentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        recentPanel.setLayout(new BoxLayout(recentPanel, BoxLayout.Y_AXIS));
        return recentPanel;
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

    private void clearContentPane() {
        getContentPane().removeAll();
        revalidate();
        repaint();
    }
}
