
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Nastya
 */
public class GUI extends JFrame {

    public GUI() {
        super("экзамен");
        BackgroundPanel mainMenuPanel = new BackgroundPanel("/Frame 19 (1).jpg", false);
        mainMenuPanel.setLayout(new GridLayout(1, 2));

        setSize(1280, 720);
        setContentPane(mainMenuPanel);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    private void addPatientUI(){
        
    }

}
