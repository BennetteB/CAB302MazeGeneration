import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Class representing the right side panel of the maze program GUI
 */
public class RightSideBarPanel extends JPanel {

    /** The maximum width of an image */
    private final int maxImageWidth;

    /** The maximum height of an image */
    private final int maxImageHeight;

    /** An array of JLabels containing images */
    private ArrayList<JLabel> imageList = new ArrayList<>();

    private JButton newImage;

    protected RightSideBarPanel(int width, int height) {
        maxImageWidth = width;
        maxImageHeight = height;
        initRightPanel();
    }

    /**
     * Initializes the rightSideBar
     */
    private void initRightPanel() {
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        newImage = new JButton("Add Image");
        newImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(newImage);
    }

    /**
     * Get newImage
     * @return Returns the button newImage
     */
    protected JButton getNewImage() {
        return newImage;
    }

    /**
     * Adds an action listener from the class implementing the RightSideBarPanel to the newImage button
     * @param listener The listener the newImage button is being added to
     */
    protected void addActionListener(ActionListener listener) {
        newImage.addActionListener(listener);
    }

    /**
     * Adds an image to the imageList array using the specified path. Resizes the image so that it remains within
     * bounds. Adds the image to the RightSideBar and updates it
     * @param path The path must be from the main directory. Temporary parameter for the path that an image will be
     *             found at. May be removed when the file picker dialog is implemented
     */
    protected void addImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        ImageIcon newIcon = resizeImage(icon, 0, 0);
        JLabel label = new JLabel(newIcon);
        imageList.add(label);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);
        updateUI();
    }

    /**
     * Resizes the image provided by icon so that it fits within the maximum width and height of the rightSideBarPanel.
     * Ensures the image is also resized so that it fits within the provided cell width and cell height (currently not
     * implemented)
     * @param icon imageIcon to be resized
     * @param numCellsWidth number of horizontal cells the image will encompass
     * @param numCellsHeight number of vertical cells the image will encompass
     * @return resized imageIcon
     */
    private ImageIcon resizeImage(ImageIcon icon, int numCellsWidth, int numCellsHeight) {
        Image img = icon.getImage();
        double widthRatio = (double) maxImageWidth / icon.getIconWidth();
        double heightRatio = (double) maxImageHeight / icon.getIconHeight();
        double ratio = Math.min(widthRatio, heightRatio);
        Image newimg = img.getScaledInstance((int) (icon.getIconWidth() * ratio), (int) (icon.getIconHeight() * ratio),
                Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }
}
