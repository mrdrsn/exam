/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Кнопка с закруглёнными краями, наследуемая от {@link JButton}.
 * Поддерживает антиалиасинг и визуальные эффекты при нажатии.
 *
 * @author Nastya
 */
public class RoundedButton extends JButton {
    private Shape shape;

    /**
     * Создаёт новую кнопку с заданным текстом и закруглёнными краями.
     *
     * @param text текст, отображаемый на кнопке
     */
    public RoundedButton(String text) {
        super(" " + text); // Добавляем пробел для лучшего отображения текста по центру
        setOpaque(false); // Отключаем стандартный фон
        setContentAreaFilled(false); // Отключаем заполнение области кнопки
        setFocusPainted(false); // Отключаем рамку фокуса
        setBorderPainted(false); // Отключаем границу кнопки
    }

    /**
     * Переопределённый метод для отрисовки закруглённой кнопки.
     * Обеспечивает плавные края и изменение цвета при наведении/нажатии.
     *
     * @param g графический контекст для отрисовки
     */
    @Override
    protected void paintComponent(Graphics g) {
        int radius = 20; // Радиус скругления углов
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isArmed()) {
            g2.setColor(Color.lightGray); // Цвет при нажатии на кнопку
        } else {
            g2.setColor(getBackground()); // Основной цвет кнопки
        }

        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        super.paintComponent(g2);
        g2.dispose();
    }

    /**
     * Переопределённый метод проверки попадания координат в область кнопки.
     * Используется для корректного взаимодействия с мышью на закруглённых краях.
     *
     * @param x координата X точки
     * @param y координата Y точки
     * @return true, если точка находится внутри области кнопки, иначе false
     */
    @Override
    public boolean contains(int x, int y) {
        return new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20).contains(x, y);
    }
}