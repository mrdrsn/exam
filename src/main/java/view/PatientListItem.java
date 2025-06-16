/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author Nastya
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PatientListItem extends JPanel {

    private JLabel iconLabel;
    private JLabel nameLabel;
    private boolean isSelected = false;

    public PatientListItem(String fullName) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0)); // выравнивание по левому краю, небольшой отступ
        setOpaque(false); // прозрачный фон

        // Иконка пользователя
        ImageIcon userIcon = new ImageIcon(getClass().getResource("/icon_person.png")); // загружаем иконку
        iconLabel = new JLabel(userIcon);
        add(iconLabel);

        // Фамилия и инициалы
        Font fontSFMediumT = CustomFontLoader.loadCustomFont(20, "fonts/SFProText-Medium.ttf");
        Font fontSF = CustomFontLoader.loadCustomFont(16, "fonts/SFProText-Regular.ttf");
        nameLabel = new JLabel(fullName);
        nameLabel.setFont(fontSFMediumT);
        add(nameLabel);

        // Добавляем MouseListener для обработки кликов
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireActionPerformed();
            }
        });
    }
    
    public JLabel getNameLabel(){
        return this.nameLabel;
    }

    // Слушатель действий
    private ActionEvent actionEvent;
    private ActionListener actionListener;

    public void addActionListener(ActionListener listener) {
        actionListener = AWTEventMulticaster.add(actionListener, listener);
    }

    public void removeActionListener(ActionListener listener) {
        actionListener = AWTEventMulticaster.remove(actionListener, listener);
    }

    protected void fireActionPerformed() {
        if (actionListener != null) {
            if (actionEvent == null) {
                actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "clicked");
            }
            actionListener.actionPerformed(actionEvent);
        }
    }
}