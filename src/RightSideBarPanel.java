import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Class representing the right side panel of the maze program GUI
 */
public class RightSideBarPanel extends JPanel {

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
     * Adds a label containing an image to the right side panel
     * @param label a label containing an image
     */
    protected void addImage(JLabel label) {
        add(label);
        updateUI();
    }

    /**
     * Removed a label containing an image from the right side panel
     * @param label a label containing an image
     */
    protected void removeImage(JLabel label) {
        remove(label);
        updateUI();
    }
}
