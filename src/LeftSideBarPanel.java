import javax.swing.*;
import java.awt.*;
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
    private JButton mazeStatsButton, solvableButton;
    private JToggleButton editButton, deleteButton, optimalSolutionButton;

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

        mazeStatsButton = new JButton("<html>" + "Maze Stats" + "</html>");
        editButton = new JToggleButton("<html>" + "Edit" + "</html>");
        deleteButton = new JToggleButton("<html>" + "Delete Image" + "</html>");
        solvableButton = new JButton("<html>" + "Solvable" + "</html>");
        optimalSolutionButton = new JToggleButton("<html>" + "Optimal Solution" + "</html>");

        mazeStatsButton.setBackground(Color.WHITE);
        solvableButton.setBackground(Color.WHITE);
        editButton.setBackground(Color.WHITE);
        deleteButton.setBackground(Color.WHITE);
        optimalSolutionButton.setBackground(Color.WHITE);

        solvableButton.setMaximumSize(new Dimension(100,100));
        mazeStatsButton.setMaximumSize(new Dimension(100,100));
        editButton.setMaximumSize(new Dimension(100,100));
        deleteButton.setMaximumSize(new Dimension(100,100));
        optimalSolutionButton.setMaximumSize(new Dimension(100,100));

        leftsidePanel.setLayout(new BoxLayout(leftsidePanel, BoxLayout.Y_AXIS));
        leftsidePanel.add(editButton);
        leftsidePanel.add(deleteButton);
        leftsidePanel.add(solvableButton);
        leftsidePanel.add(optimalSolutionButton);
        leftsidePanel.add(mazeStatsButton);

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
    public JToggleButton getOptimalSolutionButton() {
        return optimalSolutionButton;
    }

    public JToggleButton getDeleteButton() {
        return deleteButton;
    }


    /**
     * adds a listener to each of the buttons in the left sidebar panel
     * @param listener the listener that each of the buttons will add
     */
    protected void addActionListener(ActionListener listener) {
        mazeStatsButton.addActionListener(listener);
        solvableButton.addActionListener(listener);
    }

    protected void addItemListener(ItemListener listener) {
        editButton.addItemListener(listener);
        deleteButton.addItemListener(listener);
        optimalSolutionButton.addItemListener(listener);
    }
}