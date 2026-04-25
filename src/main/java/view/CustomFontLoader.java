/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

/**
 * Класс для загрузки и инициализации пользовательских шрифтов из ресурсов приложения.
 * Поддерживает регистрацию шрифта в системе и возврат шрифта заданного размера.
 *
 * @author Nastya
 */
public class CustomFontLoader {

    /**
     * Загружает пользовательский шрифт из указанного файла ресурса, регистрирует его
     * в графической среде и возвращает объект шрифта с заданным размером.
     *
     * @param size размер шрифта в пунктах
     * @param fontName имя файла шрифта (в формате .ttf), относительно classpath
     * @return объект типа {@link Font} с заданным размером, или null, если загрузка не удалась
     */
    public static Font loadCustomFont(int size, String fontName) {
        try (InputStream fontStream = CustomFontLoader.class.getClassLoader().getResourceAsStream(fontName)) {
            if (fontStream == null) {
                System.err.println("Файл шрифта не найден: " + fontName);
                return null;
            }

            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

            return customFont.deriveFont(Font.PLAIN, size);
        } catch (FontFormatException e) {
            System.err.println("Неверный формат файла шрифта: " + fontName);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла шрифта: " + fontName);
            e.printStackTrace();
        }
        return null;
    }
}