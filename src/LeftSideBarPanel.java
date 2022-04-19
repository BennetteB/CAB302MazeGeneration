import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LeftSideBarPanel extends JFrame implements ActionListener {

    public static final int WIDTH = 300;

    public static final int HEIGHT = 200;

    private JPanel leftsidePanel;

    private JButton mazeStatsButton, editButton, solvableButton;


    public LeftSideBarPanel() {
        super("Left Side Panel");
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setSize(WIDTH, HEIGHT);
        setLayout(new BorderLayout());

        leftsidePanel = new JPanel();
        leftsidePanel.setBackground(Color.LIGHT_GRAY);

        mazeStatsButton = new JButton("Maze Stats");
        editButton = new JButton("Edit");
        solvableButton = new JButton("Solvable");
        mazeStatsButton.setBackground(Color.GRAY);
        editButton.setBackground(Color.GRAY);
        solvableButton.setBackground(Color.GRAY);

        leftsidePanel.setLayout(new FlowLayout());
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

    public static void main(String[] args) {
        LeftSideBarPanel leftside = new LeftSideBarPanel();
    }

}
