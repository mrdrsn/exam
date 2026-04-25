/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Расширенное текстовое поле {@link JTextField}, поддерживающее отображение плейсхолдера (текста-подсказки),
 * который исчезает при фокусировке на поле и восстанавливается, если пользователь не ввёл текст.
 *
 * @author Nastya
 */
public class PlaceholderTextField extends JTextField {

    private String placeholder;
    private Color placeholderColor = Color.GRAY;
    private Color normalColor;

    /**
     * Создаёт новое текстовое поле с указанным плейсхолдером и количеством колонок.
     *
     * @param placeholder текст подсказки, отображаемый, когда поле пусто
     * @param columns количество колонок (ширина поля)
     */
    public PlaceholderTextField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
        this.normalColor = getForeground();
        setForeground(placeholderColor);
        superSetText(placeholder);

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

    /**
     * Переопределённый метод установки текста. Если текст пустой и поле не в фокусе,
     * устанавливает плейсхолдер. В противном случае — обычный текст.
     *
     * @param text текст для установки
     */
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

    /**
     * Устанавливает текст без проверки состояния плейсхолдера.
     * Используется внутри класса для корректного обновления содержимого.
     *
     * @param text текст для установки
     */
    public void superSetText(String text) {
        super.setText(text);
    }

    /**
     * Проверяет, находится ли сейчас фокус в другом месте (поле неактивно).
     *
     * @return true, если фокус утерян, иначе false
     */
    private boolean isFocusLost() {
        return !this.hasFocus();
    }

    /**
     * Устанавливает новый текст плейсхолдера.
     * Если текущий текст совпадает с предыдущим плейсхолдером, он будет заменён новым.
     *
     * @param placeholder новый текст плейсхолдера
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        if (getForeground().equals(placeholderColor)) {
            super.setText(placeholder);
        }
    }

    /**
     * Возвращает текущий текст плейсхолдера.
     *
     * @return строка, представляющая текущий плейсхолдер
     */
    public String getPlaceholder() {
        return this.placeholder;
    }
}