 import java.awt.*;

import javax.swing.*;

public class MainGUI extends JFrame implements Runnable {

    private JMenuItem createMaze;
    private JMenuItem open;
    private JMenuItem save;
    private JMenuItem export;
    private JMenuItem impImage;


    /**
     *
     */
    private static final long serialVersionUID = -6063203541510340633L;

    public MainGUI() {
        //region Panels
        super("Main GUI");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Main Panel for the other panels
        JPanel mainPanel = new JPanel(new BorderLayout());

        //leftSidePanel
        JPanel leftSidePanel = new LeftSideBarPanel();
        mainPanel.add(leftSidePanel, BorderLayout.WEST);

        //rightSidePanel
        JPanel rightSidePanel = new RightSideBarPanel(200, 300);
        rightSidePanel.setSize(200, 300);
        mainPanel.add(rightSidePanel, BorderLayout.EAST);

        //gridPanel
        GridPanel gridPanel = new GridPanel();
        gridPanel.CreateGrid(10,10);
        JScrollPane GridPanel = new JScrollPane(gridPanel);
        mainPanel.add(GridPanel, BorderLayout.CENTER);
        //endregion

        //region File on Menu bar
        // File Menu Bar Implementation
        createMaze = new JMenuItem("Create new maze");
        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        export = new JMenuItem("Export");
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(createMaze);
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
        setPreferredSize(new Dimension(300, 200));
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
}
