/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.BoxLayout;

/**
 * Панель с закруглёнными краями, наследуемая от {@link JPanel}.
 * Поддерживает настройку радиуса скругления и цвета фона.
 *
 * @author Nastya
 */
public class RoundedPanel extends JPanel {
    private int arcWidth;
    private int arcHeight;
    private Color backgroundColor = Color.WHITE;

    /**
     * Создаёт новую панель с одинаковым радиусом скругления по горизонтали и вертикали.
     *
     * @param radius радиус скругления углов (ширина и высота дуги)
     */
    public RoundedPanel(int radius) {
        this(radius, radius); 
    }

    /**
     * Создаёт новую панель с заданным скруглением углов.
     *
     * @param arcWidth ширина дуги скругления
     * @param arcHeight высота дуги скругления
     */
    public RoundedPanel(int arcWidth, int arcHeight) {
        this(arcWidth, arcHeight, Color.WHITE);
    }

    /**
     * Создаёт новую панель с заданными параметрами скругления и цветом фона.
     *
     * @param arcWidth ширина дуги скругления
     * @param arcHeight высота дуги скругления
     * @param background цвет фона панели
     */
    public RoundedPanel(int arcWidth, int arcHeight, Color background) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        this.backgroundColor = background;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        setPreferredSize(new Dimension(Short.MAX_VALUE, arcHeight)); 
        setMinimumSize(new Dimension(100, arcHeight));
        setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));

        setOpaque(false); // Отключаем стандартный фон, чтобы рисовать свой
    }

    /**
     * Переопределённый метод для отрисовки закруглённой панели.
     * Обеспечивает антиалиасинг и заполнение области указанным цветом.
     *
     * @param g графический контекст для отрисовки
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

        super.paintComponent(g2);
        g2.dispose();
    }
}