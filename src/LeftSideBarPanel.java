import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

/**
 * Left Side Bar Panel class
 */
public class LeftSideBarPanel extends JPanel {
    /**
     * properties of the left sidebar panel, which includes
     * width, height, base panel, and inner panel buttons.
     */
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    private JPanel leftsidePanel;
    private JButton mazeStatsButton, solvableButton, optimalSolutionButton, deleteImage;
    private JToggleButton editButton;

    /**
     * constructor for left sidebar panel,
     * calls the method that will begin filling up the panel with its components
     */
    protected LeftSideBarPanel() {
        initLeftPanel();
    }

    /**
     * Creates maze stats, edit, solvable and optimal solution buttons,
     * buttons are added to the panel
     * panel layout, background color and size dimensions are indicated here
     */
    private void initLeftPanel() {
        setSize(WIDTH, HEIGHT);
        setLayout(new BorderLayout());

        leftsidePanel = new JPanel();
        leftsidePanel.setBackground(Color.WHITE);

        mazeStatsButton = new JButton();
        editButton = new JToggleButton();
        solvableButton = new JButton();
        optimalSolutionButton = new JButton();
        deleteImage = new JButton();

        mazeStatsButton.setBackground(Color.WHITE);
        solvableButton.setBackground(Color.WHITE);
        editButton.setBackground(Color.WHITE);
        optimalSolutionButton.setBackground(Color.WHITE);
        deleteImage.setBackground(Color.WHITE);

        solvableButton.setMaximumSize(new Dimension(100,100));
        mazeStatsButton.setMaximumSize(new Dimension(100,100));
        editButton.setMaximumSize(new Dimension(100,100));
        optimalSolutionButton.setMaximumSize(new Dimension(100,100));
        deleteImage.setMaximumSize(new Dimension(100, 100));

        solvableButton.setText("Solvable");
        mazeStatsButton.setText("Maze Stats");
        editButton.setText("Edit");
        optimalSolutionButton.setText("Optimal Solution");
        deleteImage.setText("Delete Image");

        leftsidePanel.setLayout(new BoxLayout(leftsidePanel, BoxLayout.Y_AXIS));
        leftsidePanel.add(mazeStatsButton);
        leftsidePanel.add(editButton);
        leftsidePanel.add(solvableButton);
        leftsidePanel.add(optimalSolutionButton);
        leftsidePanel.add(deleteImage);
        add(leftsidePanel, BorderLayout.CENTER);
    }

    /**
     * @return maze stats button
     */
    protected JButton getMazeStatsButton() {
        return mazeStatsButton;
    }

    /**
     * @return edit button
     */
    protected JToggleButton getEditButton() {
        return editButton;
    }

    /**
     * @return solvable button
     */
    protected JButton getSolvableButton() {
        return solvableButton;
    }

    /**
     * @return optimal solution button
     */
    public JButton getOptimalSolutionButton() {
        return optimalSolutionButton;
    }

    public JButton getDeleteImage() {return deleteImage;}

    /**
     * adds a listener to each of the buttons in the left sidebar panel
     * @param listener the listener that each of the buttons will add
     */
    protected void addActionListener(ActionListener listener) {
        mazeStatsButton.addActionListener(listener);
        solvableButton.addActionListener(listener);
        optimalSolutionButton.addActionListener(listener);
        deleteImage.addActionListener(listener);
    }

    protected void addItemListener(ItemListener listener) {
        editButton.addItemListener(listener);
    }
}