import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Left Side Bar Panel class
 */
public class LeftSideBarPanel extends JPanel implements ActionListener {
    /**
     * properties of the left sidebar panel, which includes
     * width, height, base panel, and inner panel buttons.
     */
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;
    private JPanel leftsidePanel;
    private JButton mazeStatsButton, editButton, solvableButton;

    /**
     * constructor for left sidebar panel
     */
    public LeftSideBarPanel() {
        initComponents();
    }

    /**
     * Create maze stats, edit, solvable buttons and add them to the panel
     */
    private void initComponents() {
        setSize(WIDTH, HEIGHT);
        setLayout(new BorderLayout());

        leftsidePanel = new JPanel();
        leftsidePanel.setBackground(Color.LIGHT_GRAY);

        mazeStatsButton = new JButton();
        editButton = new JButton();
        solvableButton = new JButton();
        mazeStatsButton.setBackground(Color.WHITE);
        solvableButton.setBackground(Color.WHITE);
        editButton.setBackground(Color.WHITE);
        solvableButton.setMaximumSize(new Dimension(100,100));
        mazeStatsButton.setMaximumSize(new Dimension(100,100));
        editButton.setMaximumSize(new Dimension(100,100));


        leftsidePanel.setLayout(new BoxLayout(leftsidePanel, BoxLayout.Y_AXIS));
//        editButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//        solvableButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//        mazeStatsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftsidePanel.add(mazeStatsButton);
        leftsidePanel.add(editButton);
        leftsidePanel.add(solvableButton);
        add(leftsidePanel, BorderLayout.CENTER);
    }

    public void addActionListener(ActionListener listener) {
        mazeStatsButton.addActionListener(listener);
        editButton.addActionListener(listener);
        solvableButton.addActionListener(listener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}