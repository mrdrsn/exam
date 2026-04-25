package view;

/**
 *
 * @author Nastya
 */
import javax.swing.*;
import java.awt.*;


/**
 * Панель с пользовательским фоновым изображением.
 * Поддерживает отрисовку фона с возможностью сохранения пропорций изображения.
 *
 * @author Nastya
 */
public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private boolean preserveAspectRatio;

    /**
     * Создаёт новую панель с указанным фоновым изображением.
     *
     * @param imagePath путь к изображению, относительно корня classpath
     * @param preserveAspectRatio если true — изображение будет масштабироваться с сохранением пропорций,
     *                            если false — растягивается на всю панель
     */
    public BackgroundPanel(String imagePath, boolean preserveAspectRatio) {
        this.preserveAspectRatio = preserveAspectRatio;
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(imagePath));
        
        if (imageIcon.getImageLoadStatus() == MediaTracker.ERRORED) {
            System.err.println("Ошибка загрузки изображения: " + imagePath);
        }

        this.backgroundImage = imageIcon.getImage();
        setOpaque(false); 
    }

    /**
     * Переопределённый метод для отрисовки фонового изображения.
     * Вызывается автоматически при перерисовке компонента.
     *
     * @param g графический контекст для отрисовки
     */
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

    /**
     * Рисует изображение с сохранением пропорций, центрируя его внутри панели.
     *
     * @param g2d двумерный графический контекст
     * @param img изображение для отрисовки
     * @param panelWidth ширина панели
     * @param panelHeight высота панели
     */
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

    /**
     * Возвращает предпочтительный размер панели, равный размеру фонового изображения.
     * Если изображение не загружено, возвращает стандартный размер 800x600.
     *
     * @return объект типа Dimension с предпочтительным размером
     */
    @Override
    public Dimension getPreferredSize() {
        if (backgroundImage != null) {
            return new Dimension(backgroundImage.getWidth(null), backgroundImage.getHeight(null));
        }
        return new Dimension(800, 600); 
    }
}