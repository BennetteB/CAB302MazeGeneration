import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Left Side Bar Panel class
 */
public class LeftSideBarPanel extends JPanel {
    /**
     * properties of the left sidebar panel, which includes
     * width, height, base panel, and inner panel buttons.
     */
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;
    private JPanel leftsidePanel;
    private JButton mazeStatsButton, editButton, solvableButton, optimalSolutionButton;

    /**
     * constructor for left sidebar panel,
     * calls the method that will begin filling up the panel with its components
     */
    public LeftSideBarPanel() {
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
        editButton = new JButton();
        solvableButton = new JButton();
        optimalSolutionButton = new JButton();

        mazeStatsButton.setBackground(Color.WHITE);
        solvableButton.setBackground(Color.WHITE);
        editButton.setBackground(Color.WHITE);
        optimalSolutionButton.setBackground(Color.WHITE);

        solvableButton.setMaximumSize(new Dimension(100,100));
        mazeStatsButton.setMaximumSize(new Dimension(100,100));
        editButton.setMaximumSize(new Dimension(100,100));
        optimalSolutionButton.setMaximumSize(new Dimension(100,100));

        solvableButton.setText("Solvable");
        mazeStatsButton.setText("Maze Stats");
        editButton.setText("Edit");
        optimalSolutionButton.setText("Optimal Solution");

        leftsidePanel.setLayout(new BoxLayout(leftsidePanel, BoxLayout.Y_AXIS));
        leftsidePanel.add(mazeStatsButton);
        leftsidePanel.add(editButton);
        leftsidePanel.add(solvableButton);
        leftsidePanel.add(optimalSolutionButton);
        add(leftsidePanel, BorderLayout.CENTER);
    }

    /**
     * @return maze stats button
     */
    public JButton getMazeStatsButton() {
        return mazeStatsButton;
    }

    /**
     * @return edit button
     */
    public JButton getEditButton() {
        return editButton;
    }

    /**
     * @return solvable button
     */
    public JButton getSolvableButton() {
        return solvableButton;
    }

    /**
     * @return optimal solution button
     */
    public JButton getOptimalSolutionButton() {
        return optimalSolutionButton;
    }

    /**
     * adds a listener to each of the buttons in the left sidebar panel
     * @param listener the listener that each of the buttons will add
     */
    public void addActionListener(ActionListener listener) {
        mazeStatsButton.addActionListener(listener);
        editButton.addActionListener(listener);
        solvableButton.addActionListener(listener);
    }
}