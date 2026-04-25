/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Текстовое поле с закруглёнными краями и поддержкой плейсхолдера.
 * Наследуется от {@link PlaceholderTextField} и добавляет визуальное оформление с закруглениями.
 *
 * @author Nastya
 */
public class RoundedTextField extends PlaceholderTextField {
    private int radius;

    /**
     * Создаёт новое закруглённое текстовое поле с указанным плейсхолдером, количеством колонок и радиусом скругления.
     *
     * @param placeholder текст-подсказка, отображаемый когда поле пусто
     * @param columns количество колонок (ширина поля)
     * @param radius радиус скругления углов
     */
    public RoundedTextField(String placeholder, int columns, int radius) {
        super(placeholder, columns);
        this.radius = radius;
        setBorder(new EmptyBorder(4, 8, 4, 8)); // Внутренний отступ
        setOpaque(false); // Поле не рисует свой фон напрямую
    }

    /**
     * Переопределённый метод отрисовки компонента.
     * Рисует фоновое закруглённое поле с антиалиасингом.
     * Цвет фона меняется в зависимости от фокуса и наличия плейсхолдера.
     *
     * @param g графический контекст для отрисовки
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!isFocusOwner() && getText().equals(getPlaceholder())) {
            g2.setColor(new Color(240, 240, 240)); // Светло-серый при плейсхолдере
        } else {
            g2.setColor(Color.WHITE); // Белый цвет при вводе
        }

        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        super.paintComponent(g2);
        g2.dispose();
    }
}