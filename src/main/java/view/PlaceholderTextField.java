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
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class PlaceholderTextField extends JTextField {

    private String placeholder;
    private Color placeholderColor = Color.GRAY;
    private Color normalColor;

    public PlaceholderTextField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
        this.normalColor = getForeground();
        setForeground(placeholderColor);
        super.setText(placeholder);

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(placeholder)) {
                    setForeground(normalColor);
                    superSetText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().trim().isEmpty()) {
                    setForeground(placeholderColor);
                    superSetText(placeholder);
                }
            }
        });
    }

    @Override
    public void setText(String text) {
        if (text == null || text.isEmpty() && isFocusLost()) {
            setForeground(placeholderColor);
            super.setText(placeholder);
        } else {
            setForeground(normalColor);
            super.setText(text);
        }
    }

    public void superSetText(String text) {
        super.setText(text);
    }

    private boolean isFocusLost() {
        return !this.hasFocus();
    }
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        if (getForeground().equals(placeholderColor)) {
            super.setText(placeholder);
        }
    }

    public String getPlaceholder() {
        return this.placeholder;
    }
}
