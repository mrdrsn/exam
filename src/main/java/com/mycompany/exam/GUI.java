
package com.mycompany.exam;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Nastya
 */
public class GUI extends JFrame {

    public GUI() {
        super("экзамен");
        BackgroundPanel mainMenuPanel = new BackgroundPanel("main menu alt.png");
        mainMenuPanel.setLayout(new GridLayout(1, 2));
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        
//        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setLayout(new BorderLayout());
        
        leftPanel.setOpaque(false);

        JButton addPatientButton = new JButton("Новый пациент");
        JButton openPatientButton = new JButton("Открыть пациента");
        JButton exitButton = new JButton("Выход");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
//        buttonPanel.setBackground(Color.red);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createVerticalStrut(235));
        buttonPanel.add(addPatientButton);
        buttonPanel.add(Box.createVerticalStrut(30));
        buttonPanel.add(openPatientButton);
        buttonPanel.add(Box.createVerticalStrut(30));
        buttonPanel.add(exitButton);
//        buttonPanel.add(Box.createVerticalStrut(200));
        
        buttonDesign(addPatientButton);
        buttonDesign(openPatientButton);
        buttonDesign(exitButton);

        rightPanel.add(buttonPanel, BorderLayout.CENTER);
        rightPanel.setOpaque(false);
        mainMenuPanel.add(leftPanel);
        mainMenuPanel.add(rightPanel);

        setSize(1280, 720);
        setContentPane(mainMenuPanel);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void buttonDesign(JButton button) {
//        button.setPreferredSize(new Dimension(220, 60));

        Dimension buttonSize = new Dimension(200, 60);
        button.setMaximumSize(buttonSize);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(Color.PINK);
//        button.setContentAreaFilled(false);
//        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
//        button.setForeground(Color.MAGENTA);
    }
}
