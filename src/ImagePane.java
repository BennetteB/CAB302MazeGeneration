import javax.swing.*;
import java.awt.*;

public class ImagePane {
    private final ImageIcon originalImage;
    private ImageIcon resizedImage;
    private int imageCellWidth;
    private int imageCellHeight;
    private JPanel imagePanel;

    protected ImagePane(ImageIcon image, int imageCellWidth, int imageCellHeight) {
        this.originalImage = image;
        this.imageCellWidth = imageCellWidth;
        this.imageCellHeight = imageCellHeight;
        int maxImageWidth = (imageCellWidth * 50) + ((imageCellWidth-1) * 10);
        int maxImageHeight = (imageCellHeight * 50) + ((imageCellHeight-1) * 10);
        //resizedImage = resizeImage(maxImageWidth, maxImageHeight);
    }


    public ImageIcon resizeImage(int width, int height) {
        Image img = originalImage.getImage();
        double widthRatio = (double) width / originalImage.getIconWidth();
        double heightRatio = (double) height / originalImage.getIconHeight();
        double ratio = Math.min(widthRatio, heightRatio);
        Image newimg = img.getScaledInstance((int) (originalImage.getIconWidth() * ratio),
                (int) (originalImage.getIconHeight() * ratio), Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }

    public int getImageCellWidth() {
        return imageCellWidth;
    }

    public int getImageCellHeight() {
        return imageCellHeight;
    }

    public void setImageCellWidth(int imageCellWidth) {
        this.imageCellWidth = imageCellWidth;
    }

    public void setImageCellHeight(int imageCellHeight) {
        this.imageCellHeight = imageCellHeight;
    }

    public ImageIcon getOriginalImage() {
        return originalImage;
    }

    public ImageIcon getResizedImage() {
        int maxImageWidth = (imageCellWidth * 50) + ((imageCellWidth-1) * 10);
        int maxImageHeight = (imageCellHeight * 50) + ((imageCellHeight-1) * 10);
        return resizeImage(maxImageWidth, maxImageHeight);
    }

    public JPanel getImagePanel() {
        return imagePanel;
    }
}
