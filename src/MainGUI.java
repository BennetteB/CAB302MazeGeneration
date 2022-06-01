import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainGUI extends JFrame implements Runnable {

    private JMenuItem createMaze;
    private JMenuItem open;
    private JMenuItem save;
    private JMenuItem export;
    private JMenuItem setting;
    private JMenuItem impImage;
    private int mazeCellHeight = 10;
    private int mazeCellWidth = 10;
    private int imageCellHeight = 2;
    private int imageCellWidth = 2;
    private String mazeName = "New Maze";
    private String author = "Unknown";
    private MazeCell[][] mazeData = null;

    private JPanel mainPanel;
    private RightSideBarPanel rightSidePanel;
    private LeftSideBarPanel leftSidePanel;
    private GridPanel gridPanel;
    private JScrollPane GridPanel;
    private ArrayList<ImagePane> paneList = new ArrayList<>();
    private Connection connection;
    private String CREATE_TABLE;
    private String CREATE_USER;
    private File imageFile = null;

    public MainGUI(){
        super("Main GUI");
        initGUI();
        initDatabase();
    }


    /**
     * Initializes the Main GUI
     */
    public void initGUI() {
        //region Panels

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Main Panel for the other panels
        mainPanel = new JPanel(new BorderLayout());

        //leftSidePanel
        /**
         * Calls the leftSideBarPanel class:
         * Sets up the dimension for the leftSidePanel
         * Returns an interactive sidebar to the left of the main panel
         */
        leftSidePanel = new LeftSideBarPanel();
        leftSidePanel.setPreferredSize(new Dimension(100, 300));
        mainPanel.add(leftSidePanel, BorderLayout.WEST);
        leftSidePanel.addActionListener(new Listener());

        //rightSidePanel
        /**
         * Calls the rightSideBarPanel class:
         * Sets up the dimension for the rightSidePanel
         * Return an interactive sidebar to the right side of the main panel
         */
        rightSidePanel = new RightSideBarPanel();
        rightSidePanel.setPreferredSize(new Dimension(200, 300));
        mainPanel.add(rightSidePanel, BorderLayout.EAST);
        rightSidePanel.addActionListener(new Listener());


        //gridPanel
        /**
         * Calls the gridPanel class:
         * Sets up the dimensions for the gridPanel
         * Returns a grid into the center of the main panel
         */
        gridPanel = new GridPanel();
        gridPanel.CreateGrid(mazeCellWidth,mazeCellHeight);
        gridPanel.ImagePlaceState();
        GridPanel = new JScrollPane(gridPanel);
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

        addActionListener(new Listener());

        //region Implementation of the Menu bar
        /**
         * Sets up the menu bar
         */
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
     *  Initialises database,
     *  creates database table for the maze program,
     *  adds current user to the database table */
    public void initDatabase() {
        connection = DataConnect.getInstance();
        CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS maze_program ("
//                + "idx INTEGER PRIMARY KEY /*140101 AUTO_INCREMENT */ NOT NULL UNIQUE,"
                + "maze_name VARCHAR(50) PRIMARY KEY NOT NULL UNIQUE,"
                + "author VARCHAR(50),"
                + "date_time DATETIME,"
                + "maze_data VARCHAR(500),"
                + "image_cell_height INT,"
                + "image_cell_width INT,"
                + "image LONGBLOB,"
                + "maze_cell_height INT,"
                + "maze_cell_width INT" + ");";
        try {
            connection.createStatement().execute(CREATE_TABLE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void saveMaze() {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO maze_program(maze_name, maze_data," +
                    "maze_cell_width, maze_cell_height, image, image_cell_height, image_cell_width) VALUES(?,?)");
            statement.setString(1, mazeName);
            //statement.setString(2, Maze.getMaze().toString());
            //statement.setString(3, )
            // I have commented out the above two lines so that the code still works
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void openMaze(){

    }


    /**
     * Adds an action listener to the newImage button
     * @param listener The listener the newImage button is being added to
     */
    protected void addActionListener(ActionListener listener) {
        createMaze.addActionListener(listener);
        setting.addActionListener(listener);
        open.addActionListener(listener);
        save.addActionListener(listener);
        export.addActionListener(listener);
        impImage.addActionListener(listener);
    }

    /**
     * Converts the maze data received by the Algorithm class into a type readable by the GridPanel class
     * @param mazeData a two-dimensional int that represents a maze
     * @return returns a two-dimensional component that represents a maze
     */
    public Component[][] gridPanelMazeData(int[][] mazeData) {
        return null;
    }

    /**
     * Converts the maze data received by the GridPanel class into a type readable by the Algorithm class
     * @param mazeData a two-dimensional component that represents a maze
     * @return returns a two-dimensional int that represents a maze
     */
    public int[][] algorithmMazeData(Component[][] mazeData) {
        return null;
    }

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
            if (source == rightSidePanel.getNewImage() || source == impImage) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter jpg = new FileNameExtensionFilter("JPG Images", "jpg");
                FileNameExtensionFilter jpeg = new FileNameExtensionFilter("JPEG Images", "jpeg");
                FileNameExtensionFilter png = new FileNameExtensionFilter("PNG Images", "png");
                fileChooser.addChoosableFileFilter(jpg);
                fileChooser.addChoosableFileFilter(jpeg);
                fileChooser.addChoosableFileFilter(png);
                fileChooser.setAcceptAllFileFilterUsed(false);
                int option = fileChooser.showOpenDialog(mainPanel);
                if (option == JFileChooser.APPROVE_OPTION) {
                    imageFile = fileChooser.getSelectedFile();
                    // I have duplicated the above line so that the program still functions
                    File file = fileChooser.getSelectedFile();

                    BufferedImage image;
                    try {
                        image = ImageIO.read(file);
                    } catch (IOException ev) {
                        return;
                    }
                    ImageIcon icon = new ImageIcon(image);
                    ImagePane pane = new ImagePane(icon, 2, 2);
                    JLabel label = new JLabel(pane.resizeImage(200, 200));
                    label.setAlignmentX(Component.CENTER_ALIGNMENT);
                    rightSidePanel.addImage(label);
                    // Sourced from https://stackhowto.com/how-to-get-mouse-position-on-click-relative-to-jframe/
                    label.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (e.getButton() == MouseEvent.BUTTON1) {          //Left click
                                System.out.println("button 1");
                            } else if (e.getButton() == MouseEvent.BUTTON3) {   // Right click
                                System.out.println("button 3");
                            }
                        }
                    });

                    paneList.add(pane);
                }

            }

            if (source == leftSidePanel.getMazeStatsButton()) {
                float deadCellsPercentage = new Algorithm().showDeadCells(gridPanel.getGridMazeCellArray());
                JOptionPane.showMessageDialog(mainPanel, "Percentage of dead cells: " +
                        deadCellsPercentage + "%");
            }
            if (source == leftSidePanel.getEditButton()) {
                // Edit Button
            }

            if (source == leftSidePanel.getSolvableButton()) {
                if (new Algorithm().mazeSolvability(gridPanel.getGridMazeCellArray()) != null) {
                    JOptionPane.showMessageDialog(mainPanel, "The maze is solvable");
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "The maze is not solvable");
                }
            }

            if (source == leftSidePanel.getOptimalSolutionButton()) {
                MazeCell[][] maze = new Algorithm().mazeSolvability(gridPanel.getGridMazeCellArray());
                if (maze != null) {
                    // maze below needs to be used to display the maze
                    maze = new Algorithm().optimalSolution(maze);
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "The maze is not solvable");
                }
            }

            if (source == createMaze) {
                // Possibly make a new method for most of this part
                boolean randomiseMaze = false;

                // Sourced from https://stackhowto.com/how-to-make-jtextfield-accept-only-numbers/
                KeyAdapter onlyInt = new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        char c = e.getKeyChar();
                        if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                            e.consume();  // if it's not a number, ignore the event
                        }
                    }
                };

                JTextField mazeHeightText = new JTextField("10", 5);
                mazeHeightText.addKeyListener(onlyInt);

                JTextField mazeWidthText = new JTextField("10", 5);
                mazeWidthText.addKeyListener(onlyInt);

                JTextField imageWidthText = new JTextField("2", 5);
                imageWidthText.addKeyListener(onlyInt);

                JTextField imageHeightText = new JTextField("2", 5);
                imageHeightText.addKeyListener(onlyInt);

                JTextField mazeNameText = new JTextField("New Maze", 20);
                JTextField mazeAuthorText = new JTextField("Unknown", 20);

                JCheckBox randomMazeOption = new JCheckBox();

                JPanel newMazeOptions = new JPanel();
                newMazeOptions.setLayout(new GridLayout(7, 1, 0, 10));
                newMazeOptions.add(new JLabel("Maze Cell Height:"));
                newMazeOptions.add(mazeHeightText);
                newMazeOptions.add(new JLabel("Maze Cell Width:"));
                newMazeOptions.add(mazeWidthText);
                newMazeOptions.add(new JLabel("Image Cell Height"));
                newMazeOptions.add(imageHeightText);
                newMazeOptions.add(new JLabel("Image Cell Width"));
                newMazeOptions.add(imageWidthText);
                newMazeOptions.add(new JLabel("Maze Name"));
                newMazeOptions.add(mazeNameText);
                newMazeOptions.add(new JLabel("Author"));
                newMazeOptions.add(mazeAuthorText);
                newMazeOptions.add(new JLabel("Randomise Maze"));
                newMazeOptions.add(randomMazeOption);

                int result = JOptionPane.showConfirmDialog(null, newMazeOptions,
                        "Create new maze", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    mazeCellHeight = Integer.parseInt(mazeHeightText.getText());
                    mazeCellWidth = Integer.parseInt(mazeWidthText.getText());
                    imageCellHeight = Integer.parseInt(imageHeightText.getText());
                    imageCellWidth = Integer.parseInt(imageWidthText.getText());
                    mazeName = mazeNameText.getText();
                    author = mazeAuthorText.getText();
                    randomiseMaze = randomMazeOption.isSelected();

                    mainPanel.remove(GridPanel);
                    gridPanel = new GridPanel();
                    gridPanel.CreateGrid(mazeCellWidth,mazeCellHeight);
                    GridPanel = new JScrollPane(gridPanel);
                    mainPanel.add(GridPanel, BorderLayout.CENTER);
                    mainPanel.revalidate();
                    mainPanel.repaint();

                    if (randomiseMaze) {
                        MazeCell[][] newMaze = new Algorithm().generateMaze(mazeCellWidth, mazeCellHeight);
                        gridPanel.CreateMaze(newMaze);
                    }
                }
            }

            if (source == setting) {

            }

            if (source == open) {
                openMaze();
            }

            if (source == save) {
                saveMaze();
            }

            if (source == export) {
            }
        }
    }
}
