import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainGUI extends JFrame implements Runnable {

    private JMenuItem createMaze;
    private JMenuItem open;
    private JMenuItem save;
    private JMenuItem export;
    private JMenuItem setting;
    private JMenuItem impImage;
    private int mazeHeight;
    private int mazeWidth;
    private String mazeName;
    private String author;

    private RightSideBarPanel rightSidePanel;
    private LeftSideBarPanel leftSidePanel;
    private GridPanel gridPanel;


    /**
     *
     */
    private static final long serialVersionUID = -6063203541510340633L;

    public MainGUI(){
        super("Main GUI");
        initGUI();
    }

    public void initGUI() {
        //region Panels

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Main Panel for the other panels
        JPanel mainPanel = new JPanel(new BorderLayout());

        //leftSidePanel
        leftSidePanel = new LeftSideBarPanel();
        leftSidePanel.setPreferredSize(new Dimension(100, 300));
        mainPanel.add(leftSidePanel, BorderLayout.WEST);
        leftSidePanel.addActionListener(new Listener());

        //rightSidePanel
        rightSidePanel = new RightSideBarPanel(200, 300);
        rightSidePanel.setPreferredSize(new Dimension(200, 300));
        mainPanel.add(rightSidePanel, BorderLayout.EAST);
        rightSidePanel.addActionListener(new Listener());


        //gridPanel
        gridPanel = new GridPanel();
        gridPanel.CreateGrid(10,10);
        JScrollPane GridPanel = new JScrollPane(gridPanel);
        mainPanel.add(GridPanel, BorderLayout.CENTER);
        //endregion

        //region File on Menu bar
        // File Menu Bar Implementation
        createMaze = new JMenuItem("Create new maze");
        setting = new JMenuItem("Settings");
        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        export = new JMenuItem("Export");
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(createMaze);
        fileMenu.add(setting);
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(export);
        //endregion

        //region Edit on Menu bar
        //Edit Menu Bar implementation
        impImage = new JMenuItem("Import image");
        JMenu editMenu = new JMenu("Edit");
        editMenu.add(impImage);
        //endregion

        //region Implementation of the Menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        setJMenuBar(menuBar);
        //endregion

        //Making the panels visible on MainGUI window
        getContentPane().add(mainPanel);

        // Display the window.
        setPreferredSize(new Dimension(1600, 900));
        setLocation(new Point(100, 100));
        pack();
        setVisible(true);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        new MainGUI();
    }

    @Override
    public void run() {

    }

    private class Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Component source = (Component) e.getSource();
            if (source == rightSidePanel.getNewImage()) {
                String path = JOptionPane.showInputDialog("Provide a file path: ");
                rightSidePanel.addImage(path);
            }
            if (source == leftSidePanel.getMazeStatsButton()) {

                // Remove me in submission branch
                String buttons[] = {"button"};
                JOptionPane.showOptionDialog(null, "message", "title,", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, "default");
            }
            if (source == leftSidePanel.getEditButton()) {

            }
            if (source == leftSidePanel.getSolvableButton()) {

            }
        }
    }
}
