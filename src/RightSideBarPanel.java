import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Class representing the right side panel of the maze program GUI
 */
public class RightSideBarPanel extends JPanel {

    /**
     * The maximum width of an image
     */
    private final int maxImageWidth = 200;

    /**
     * The maximum height of an image
     */
    private final int maxImageHeight = 200;

    private JButton newImage;

    protected RightSideBarPanel() {
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
     *
     * @return Returns the button newImage
     */
    protected JButton getNewImage() {
        return newImage;
    }

    /**
     * Adds an action listener from the class implementing the RightSideBarPanel to the newImage button
     *
     * @param listener The listener the newImage button is being added to
     */
    protected void addActionListener(ActionListener listener) {
        newImage.addActionListener(listener);
    }

    /**
     * Adds an image to the imageList array using the specified path. Resizes the image so that it remains within
     * bounds. Adds the image to the RightSideBar and updates it
     *
     * @param label The path must be from the main directory. Temporary parameter for the path that an image will be
     *             found at. May be removed when the file picker dialog is implemented
     */
    protected void addImage(JLabel label) {
        add(label);
        updateUI();
    }
}
