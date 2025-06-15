/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Nastya
 */
public class BackgroundPanelStrict extends JPanel {

    private BufferedImage image;
    private float opacity = 1.0f;

    public BackgroundPanelStrict(String path, boolean scale) {
        this(path, scale, 1.0f);
    }

    public BackgroundPanelStrict(String path, boolean scale, float opacity) {
        this.opacity = opacity;
        try {
            image = ImageIO.read(getClass().getResource(path));
            if (!scale && image != null) {
                setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
                setMaximumSize(getPreferredSize());
                setMinimumSize(getPreferredSize());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2d.drawImage(image, 0, 0, this);
            g2d.dispose();
        }
    }
}
