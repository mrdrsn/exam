/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Компонент, представляющий отдельную запись пациента в списке.
 * Отображает иконку и имя пациента, поддерживает обработку кликов мыши и уведомление слушателей.
 *
 * @author Nastya
 */
public class PatientListItem extends JPanel {

    private JLabel iconLabel;
    private JLabel nameLabel;
    private boolean isSelected = false;

    /**
     * Создаёт новый элемент списка пациентов с указанным именем.
     * Инициализирует интерфейс: добавляет иконку пользователя и имя пациента.
     *
     * @param fullName полное имя пациента (например, "Иванов И.И.")
     */
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

    /**
     * Возвращает метку с именем пациента.
     * Может использоваться для изменения текста или стиля имени динамически.
     *
     * @return объект типа {@link JLabel}, содержащий имя пациента
     */
    public JLabel getNameLabel() {
        return this.nameLabel;
    }

    // Слушатель действий
    private ActionEvent actionEvent;
    private ActionListener actionListener;

    /**
     * Добавляет указанного слушателя событий действия.
     *
     * @param listener слушатель, который будет уведомлен о клике по элементу
     */
    public void addActionListener(ActionListener listener) {
        actionListener = AWTEventMulticaster.add(actionListener, listener);
    }

    /**
     * Удаляет указанного слушателя событий действия.
     *
     * @param listener слушатель, который больше не должен получать уведомления
     */
    public void removeActionListener(ActionListener listener) {
        actionListener = AWTEventMulticaster.remove(actionListener, listener);
    }

    /**
     * Вызывает событие действия, уведомляя всех зарегистрированных слушателей.
     * Используется при клике на элемент списка.
     */
    protected void fireActionPerformed() {
        if (actionListener != null) {
            if (actionEvent == null) {
                actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "clicked");
            }
            actionListener.actionPerformed(actionEvent);
        }
    }
}