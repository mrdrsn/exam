/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author Nastya
 */
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RoundedTextField extends PlaceholderTextField {
    private int radius;

    public RoundedTextField(String placeholder, int columns, int radius) {
        super(placeholder, columns);
        this.radius = radius;
        setBorder(new EmptyBorder(4, 8, 4, 8));
        setOpaque(false); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!isFocusOwner() && getText().equals(getPlaceholder())) {
            g2.setColor(new Color(240, 240, 240)); 
        } else {
            g2.setColor(Color.WHITE);
        }

        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        super.paintComponent(g2);
        g2.dispose();
    }

}