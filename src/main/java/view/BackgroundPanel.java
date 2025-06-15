package view;

/**
 *
 * @author Nastya
 */
import javax.swing.*;
import java.awt.*;



public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private boolean preserveAspectRatio;

    public BackgroundPanel(String imagePath, boolean preserveAspectRatio) {
        this.preserveAspectRatio = preserveAspectRatio;
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(imagePath)); // getResource() может вернуть null!
        
        if (imageIcon.getImageLoadStatus() == MediaTracker.ERRORED) {
            System.err.println("Ошибка загрузки изображения: " + imagePath);
        }

        this.backgroundImage = imageIcon.getImage();
        setOpaque(false); // делаем панель прозрачной
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            if (preserveAspectRatio) {
                drawPreservingAspect((Graphics2D) g, backgroundImage, panelWidth, panelHeight);
            } else {
                g.drawImage(backgroundImage, 0, 0, panelWidth, panelHeight, this);
            }
        }
    }

    private void drawPreservingAspect(Graphics2D g2d, Image img, int panelWidth, int panelHeight) {
        int imgWidth = img.getWidth(this);
        int imgHeight = img.getHeight(this);

        if (imgWidth <= 0 || imgHeight <= 0) return;

        double widthRatio = (double) panelWidth / imgWidth;
        double heightRatio = (double) panelHeight / imgHeight;
        double scale = Math.max(widthRatio, heightRatio);

        int scaledWidth = (int) (imgWidth * scale);
        int scaledHeight = (int) (imgHeight * scale);

        int x = (panelWidth - scaledWidth) / 2;
        int y = (panelHeight - scaledHeight) / 2;

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(img, x, y, scaledWidth, scaledHeight, this);
    }

    @Override
    public Dimension getPreferredSize() {
        if (backgroundImage != null) {
            return new Dimension(backgroundImage.getWidth(null), backgroundImage.getHeight(null));
        }
        return new Dimension(800, 600); // запасной размер
    }
}