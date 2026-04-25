
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
 * Панель с фоновым изображением, поддерживающая настройку прозрачности.
 * Изображение может отображаться в исходном размере или растягиваться под размеры панели.
 *
 * @author Nastya
 */
public class BackgroundPanelStrict extends JPanel {

    private BufferedImage image;
    private float opacity = 1.0f; // Прозрачность: 1.0 — непрозрачный, 0.0 — полностью прозрачный

    /**
     * Создаёт новую фоновую панель с указанным изображением и параметрами отображения.
     *
     * @param path путь к изображению (относительно classpath)
     * @param scale если true — изображение будет растягиваться под размеры панели,
     *              если false — панель принимает размеры изображения
     */
    public BackgroundPanelStrict(String path, boolean scale) {
        this(path, scale, 1.0f);
    }

    /**
     * Создаёт новую фоновую панель с указанным изображением, режимом масштабирования и прозрачностью.
     *
     * @param path путь к изображению (относительно classpath)
     * @param scale если true — изображение будет растягиваться под размеры панели,
     *              если false — панель принимает размеры изображения
     * @param opacity уровень прозрачности от 0.0f (полная прозрачность) до 1.0f (непрозрачность)
     */
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

    /**
     * Переопределённый метод для отрисовки фонового изображения с заданной прозрачностью.
     * Вызывается автоматически при перерисовке компонента.
     *
     * @param g графический контекст для отрисовки
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2d.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
            g2d.dispose();
        }
    }
}