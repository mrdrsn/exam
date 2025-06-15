/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author Nastya
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class RoundedPanel extends JPanel {
    private int arcWidth;
    private int arcHeight;
    private Color backgroundColor = Color.WHITE;

    public RoundedPanel(int radius) {
        this(radius, radius); // если радиус одинаковый
    }

    public RoundedPanel(int arcWidth, int arcHeight) {
        this(arcWidth, arcHeight, Color.WHITE);
    }

    public RoundedPanel(int arcWidth, int arcHeight, Color background) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        this.backgroundColor = background;

        // Убедимся, что layout правильно распределит компоненты
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // Установи минимальные/предпочитаемые размеры
        setPreferredSize(new Dimension(Short.MAX_VALUE, arcHeight)); // можно указать нужную высоту
        setMinimumSize(new Dimension(100, arcHeight));
        setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));

        setOpaque(false); // важно для отрисовки фона
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Рисуем закруглённый фон
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

        // Вызываем paintComponent для дочерних элементов
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Можно оставить пустым, если не нужна рамка
        // Либо рисовать границу отдельно
    }
}

//public class RoundedPanel extends JPanel {
//    private int arcWidth;
//    private int arcHeight;
//    private Color backgroundColor;
//
//    public RoundedPanel(int radius) {
//        this(radius, radius, Color.WHITE); // Use radius for both arcWidth and arcHeight
//    }
//
//    public RoundedPanel(int radius, Color background) {
//        this(radius, radius, background);
//    }
//
//    public RoundedPanel(int arcWidth, int arcHeight, Color background) {
//        this.arcWidth = arcWidth;
//        this.arcHeight = arcHeight;
//        this.backgroundColor = background;
//        setOpaque(false); // Ensure transparency for custom painting
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        Graphics2D g2 = (Graphics2D) g.create();
//        
//        // Enable anti-aliasing for smooth edges
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//
//        // Clear the background (important when opaque is false)
//        g2.setColor(getBackground());
//        g2.fillRect(0, 0, getWidth(), getHeight());
//
//        // Draw the rounded rectangle
//        if (backgroundColor != null) {
//            g2.setColor(backgroundColor);
//            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight));
//        }
//
//        // Let the superclass handle its components
//        super.paintComponent(g2);
//        g2.dispose();
//    }
//
////    @Override
////    protected void paintBorder(Graphics g) {
////        // Optional: Add border if needed
////        Graphics2D g2 = (Graphics2D) g.create();
////        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
////        g2.setColor(Color.GRAY); // Example border color
////        g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight));
////        g2.dispose();
////    }
//
//    @Override
//    public boolean isOptimizedDrawingEnabled() {
//        return false; // Ensures proper rendering of child components
//    }
//}


//public class RoundedPanel extends JPanel {
//    private int arcWidth;
//    private int arcHeight;
//    private Color backgroundColor;
//
//    public RoundedPanel(int radius) {
//        this(radius, 50, Color.WHITE); // стандартный цвет фона — белый
//    }
//
//    public RoundedPanel(int radius, Color background) {
//        this(radius, 50, background);
//    }
//
//    public RoundedPanel(int arcWidth, int arcHeight, Color background) {
//        this.arcWidth = arcWidth;
//        this.arcHeight = arcHeight;
//        this.backgroundColor = background;
//        setOpaque(false); // важная строка для правильной отрисовки
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        Graphics2D g2 = (Graphics2D) g.create();
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        // Рисуем фон с закруглением
//        if (getBackground() != null) {
//            g2.setColor(backgroundColor);
//            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arcWidth, arcHeight));
//        }
//
//        // Вызываем родительский метод для отрисовки компонентов
//        super.paintComponent(g2);
//        g2.dispose();
//    }
//
//    @Override
//    protected void paintBorder(Graphics g) {
//        // Если нужно рисовать рамку вокруг — добавь здесь логику
//        // Например, можно реализовать обводку нужным цветом
//    }
//
//    @Override
//    public boolean isOptimizedDrawingEnabled() {
//        return false; // позволяет правильно отображаться дочерним компонентам
//    }
//}