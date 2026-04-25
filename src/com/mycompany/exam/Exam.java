package com.mycompany.exam;

import controller.Controller;
import java.io.IOException;

/**
 * Класс, являющийся главным (main class) классом проекта. 
 * Запускает программу при помощи контроллера {@link Controller}
 *
 * @author Nastya
 */
public class Exam {

    public static void main(String[] args) throws IOException {
        Controller controller = new Controller();
        controller.start();
    }
}
