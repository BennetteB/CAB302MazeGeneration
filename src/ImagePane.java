import javax.swing.*;
import java.awt.*;

/** Class containing the data associated with a master image*/
public class ImagePane {
    private final ImageIcon originalImage;
    private int imageCellWidth;
    private int imageCellHeight;
    private JLabel label;

    /** Initializes the ImagePane */
    protected ImagePane(ImageIcon image, int imageCellWidth, int imageCellHeight) {
        this.originalImage = image;
        this.imageCellWidth = imageCellWidth;
        this.imageCellHeight = imageCellHeight;
    }

    /**
     * Resizes an image to be within the bounds of the width and height
     * @param width the maximum width of the image
     * @param height the maximum height of the image
     * @return a resized image within the bounds of width and height
     */
    public ImageIcon resizeImage(int width, int height) {
        Image img = originalImage.getImage();
        double widthRatio = (double) width / originalImage.getIconWidth();
        double heightRatio = (double) height / originalImage.getIconHeight();
        double ratio = Math.min(widthRatio, heightRatio);
        Image newimg = img.getScaledInstance((int) (originalImage.getIconWidth() * ratio),
                (int) (originalImage.getIconHeight() * ratio), Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }

    /**
     * Returns the cell width of the image
     * @return the cell width of the image
     */
    public int getImageCellWidth() {
        return imageCellWidth;
    }

    /**
     * Returns the cell height of the image
     * @return the cell height of the image
     */
    public int getImageCellHeight() {
        return imageCellHeight;
    }

    /**
     * Sets the cell width of the image
     * @param imageCellWidth cell width of the image
     */
    public void setImageCellWidth(int imageCellWidth) {
        this.imageCellWidth = imageCellWidth;
    }

    /**
     * Sets the cell height of the image
     * @param imageCellHeight cell height of the image
     */
    public void setImageCellHeight(int imageCellHeight) {
        this.imageCellHeight = imageCellHeight;
    }

    /**
     * Returns an unmodified version of the image
     * @return an unmodified version of the image
     */
    public ImageIcon getOriginalImage() {
        return originalImage;
    }

    /**
     * Returns a resized image that is within the bounds of its cell width and height
     * @return a resized image that is within the bounds of its cell width and height
     */
    public ImageIcon getResizedImage() {
        int maxImageWidth = (imageCellWidth * 50) + ((imageCellWidth-2) * 10);
        int maxImageHeight = (imageCellHeight * 50) + ((imageCellHeight-2) * 10);
        return resizeImage(maxImageWidth, maxImageHeight);
    }

    /**
     * Sets the label the image is contained within
     * @param label the label the image is contained within
     */
    public void setLabel(JLabel label) {
        this.label = label;
    }

    /**
     * Returns the label the image is contained within
     * @return the label the image is contained within
     */
    public JLabel getLabel() {
        return this.label;
    }

}
