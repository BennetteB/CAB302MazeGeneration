import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Class representing the right side panel of the maze program GUI
 */
public class RightSideBarPanel extends JPanel implements ActionListener {

    /** The maximum width of an image */
    private final int maxImageWidth;

    /** The maximum height of an image */
    private final int maxImageHeight;

    /** An array of JLabels containing images */
    private ArrayList<JLabel> imageList = new ArrayList<>();

    public RightSideBarPanel(int width, int height) {
        maxImageWidth = width;
        maxImageHeight = height;
        initializeRightSideBar();
    }

    /**
     * Initializes the rightSideBar
     */
    public void initializeRightSideBar() {
        setBackground(Color.WHITE);
        JButton newImage = new JButton("Add Image");
        newImage.addActionListener(this);
        add(newImage);
    }

    /**
     * Adds an image to the imageList array using the specified path. Resizes the image so that it remains within
     * bounds. Adds the image to the RightSideBar and updates it
     * @param path The path must be from the main directory. Temporary parameter for the path that an image will be
     *             found at. May be removed when the file picker dialog is implemented
     */
    public void addImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        ImageIcon newIcon = resizeImage(icon, 0, 0);
        JLabel label = new JLabel(newIcon);
        imageList.add(label);
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
    public ImageIcon resizeImage(ImageIcon icon, int numCellsWidth, int numCellsHeight) {
        Image img = icon.getImage();
        double widthRatio = (double) maxImageWidth / icon.getIconWidth();
        double heightRatio = (double) maxImageHeight / icon.getIconHeight();
        double ratio = Math.min(widthRatio, heightRatio);
        Image newimg = img.getScaledInstance((int) (icon.getIconWidth() * ratio), (int) (icon.getIconHeight() * ratio), Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        /** Temporary alternative to a file picker dialog */
        String path = JOptionPane.showInputDialog(this, "Provide a file path: ");
        addImage(path);
    }
}
