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

public class LetterSpacingLabel extends JLabel {
    private float letterSpacing; // положительное или отрицательное значение

    public LetterSpacingLabel(String text, float letterSpacing) {
        super(text);
        this.letterSpacing = letterSpacing;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setFont(getFont());
        g2d.setColor(getForeground());

        String text = getText();
        FontMetrics fm = g2d.getFontMetrics();

        float x = 0;
        float y = fm.getAscent();

        for (int i = 0; i < text.length(); i++) {
            String ch = text.substring(i, i + 1);
            g2d.drawString(ch, x, y);
            x += fm.charWidth(ch.charAt(0)) + letterSpacing;
        }

        g2d.dispose();
    }
}
