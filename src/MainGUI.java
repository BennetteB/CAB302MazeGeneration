import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
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
    private String mazeName = "New Maze";
    private String author = "Unknown";
//    private String RANDOM_MAZE_DATA = "abcd1234";
    private boolean newMaze = false;

    private JPanel mainPanel;
    private RightSideBarPanel rightSidePanel;
    private LeftSideBarPanel leftSidePanel;
    private GridPanel gridPanel;
    private JScrollPane GridPanel;
    private ArrayList<ImagePane> paneList = new ArrayList<>();
    private Connection connection;
    private String CREATE_TABLE;
    private String CREATE_USER;
    private FileInputStream imageDataFile = null;

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
        leftSidePanel.addItemListener(new Listener());

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
        GridPanel = new JScrollPane(gridPanel);
        mainPanel.add(GridPanel, BorderLayout.CENTER);
        gridPanel.SetEditState(false);
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
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL UNIQUE,"
                + "maze_name VARCHAR(50) NOT NULL,"
                + "author VARCHAR(50),"
                + "date_time DATETIME,"
                + "maze_data LONGBLOB,"
                + "image LONGBLOB,"
                + "maze_cell_height INT,"
                + "maze_cell_width INT" + ");";
        try {
            connection.createStatement().execute(CREATE_TABLE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Saves existing/ new maze into the database with custom parameters
     */
    public void saveMaze() {
        try {
            if (newMaze) {
                /** Create a new entry for fresh mazes **/
                PreparedStatement saveMazeData = connection.prepareStatement("INSERT INTO " +
                        "maze_program(author, " +
                        "maze_name," +
                        "maze_cell_width, maze_cell_height, image, " +
                        "date_time) VALUES(?,?,?,?,?,?)");
                saveMazeData.setString(1, author);
                saveMazeData.setString(2, mazeName);
//               saveMazeData.set;
                //The line below converts mazedata to a string
                //mazeToString(gridPanel.getGridMazeCellArray());
                saveMazeData.setInt(3, mazeCellWidth);
                saveMazeData.setInt(4, mazeCellHeight);
                saveMazeData.setBlob(5,imageDataFile);
                saveMazeData.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
                saveMazeData.execute();
            } else {
                /** Update existing entry for existing mazes **/

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void openMaze(){
        ResultSet rs = null;
        try {
            /** retrieve data from SQL database **/
            PreparedStatement openMazeData = connection.prepareStatement("SELECT * FROM maze_program");
            rs = openMazeData.executeQuery();
//            while (rs.next()) {
//                Integer id = rs.getInt("id");
//                String mazeName = rs.getString("maze_name");
//                java.sql.Blob mazeImage = rs.getBlob("image");
//            }

            /** display retrieved data as a list into a dialog box **/

            /** add a click listener to each item in the list **/

        } catch (SQLException ex) {
            ex.printStackTrace();
       }

        // Code below converts a database string to MazeCell[][] and then prints that out on the grid
//        // You will need to swap mazeheight and mazewidth below with their corresponding values from the database
//        MazeCell[][] maze = stringToMaze(string, mazeheight, mazewidth);
//        mainPanel.remove(GridPanel);
//        gridPanel = new GridPanel();
//        gridPanel.CreateGrid(mazewidth, mazeheight);
//        GridPanel = new JScrollPane(gridPanel);
//        mainPanel.add(GridPanel, BorderLayout.CENTER);
//        mainPanel.revalidate();
//        mainPanel.repaint();
//        gridPanel.CreateMaze(maze);
//        gridPanel.SetEditState(false);
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

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        new MainGUI();
    }

    @Override
    public void run() {

    }

    public String mazeToString(MazeCell[][] mazeData) {
        StringBuilder mazeString = new StringBuilder();
        for (int i = 0; i < mazeData.length; i++) {
            for (int j = 0; j < mazeData[0].length; j++) {
                if (mazeData[i][j].getWallUp()) {
                    mazeString.append("1");
                } else {
                    mazeString.append("0");
                }
                if (mazeData[i][j].getWallRight()) {
                    mazeString.append("1");
                } else {
                    mazeString.append("0");
                }
                if (mazeData[i][j].getWallDown()) {
                    mazeString.append("1");
                } else {
                    mazeString.append("0");
                }
                if (mazeData[i][j].getWallLeft()) {
                    mazeString.append("1");
                } else {
                    mazeString.append("0");
                }
            }
        }
        return mazeString.toString();
    }

    public MazeCell[][] stringToMaze(String mazeString, int mazeCellHeight, int mazeCellWidth) {
        Maze maze = new Maze(mazeCellHeight, mazeCellWidth, false);
        MazeCell[][] mazeCells = maze.getMaze();
        int x = 0;
        for (int i = 0; i < mazeCells.length; i++) {
            for (int j = 0; j < mazeCells[0].length; j++) {
                if (mazeString.charAt(x) == '1') {
                    mazeCells[i][j].toggleWallUp();
                }
                if (mazeString.charAt(x+1) == '1') {
                    mazeCells[i][j].toggleWallRight();
                }
                if (mazeString.charAt(x+2) == '1') {
                    mazeCells[i][j].toggleWallDown();
                }
                if (mazeString.charAt(x+3) == '1') {
                    mazeCells[i][j].toggleWallLeft();
                }
                x += 4;
            }
        }
        return mazeCells;
    }

    private class Listener implements ActionListener, ItemListener {
        public void actionPerformed(ActionEvent e) {
            Component source = (Component) e.getSource();
            if (source == rightSidePanel.getNewImage() || source == impImage) {
                JPopupMenu popup = new JPopupMenu();
                JMenuItem delete = new JMenuItem("Delete");
                JMenuItem settings = new JMenuItem("Settings");
                popup.add(delete);
                popup.add(settings);

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
                    File file = fileChooser.getSelectedFile();
                    BufferedImage image;
                    try {
                        image = ImageIO.read(file);
                        imageDataFile = new FileInputStream(file);
                    } catch (IOException ev) {
                        return;
                    }
                    ImageIcon icon = new ImageIcon(image);
                    ImagePane pane = new ImagePane(icon, 2, 2);
                    JLabel label = new JLabel(pane.resizeImage(200, 200));
                    label.setAlignmentX(Component.CENTER_ALIGNMENT);
                    rightSidePanel.addImage(label);
                    paneList.add(pane);

                    delete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            rightSidePanel.removeImage(label);
                            paneList.remove(pane);
                        }
                    });
                    settings.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Sourced from https://stackhowto.com/how-to-make-jtextfield-accept-only-numbers/
                            KeyAdapter onlyInt = new KeyAdapter() {
                                public void keyTyped(KeyEvent e) {
                                    char c = e.getKeyChar();
                                    if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                                        e.consume();  // if it's not a number, ignore the event
                                    }
                                }
                            };

                            JTextField imageWidthText = new JTextField(Integer.toString(pane.getImageCellWidth()), 5);
                            imageWidthText.addKeyListener(onlyInt);

                            JTextField imageHeightText = new JTextField(Integer.toString(pane.getImageCellHeight()) , 5);
                            imageHeightText.addKeyListener(onlyInt);

                            JPanel imageSettings = new JPanel();
                            imageSettings.setLayout(new GridLayout(2, 1, 0, 10));
                            imageSettings.add(new JLabel("Maze Cell Height:"));
                            imageSettings.add(imageHeightText);
                            imageSettings.add(new JLabel("Maze Cell Width:"));
                            imageSettings.add(imageWidthText);

                            int result = JOptionPane.showConfirmDialog(null, imageSettings,
                                    "Image Settings", JOptionPane.OK_CANCEL_OPTION);
                            if (result == JOptionPane.OK_OPTION) {
                                pane.setImageCellHeight(Integer.parseInt(imageHeightText.getText()));
                                pane.setImageCellWidth(Integer.parseInt(imageWidthText.getText()));
                            }
                        }
                    });

                    // Sourced from https://stackhowto.com/how-to-get-mouse-position-on-click-relative-to-jframe/
                    label.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (e.getButton() == MouseEvent.BUTTON1) {//Left click
                                leftSidePanel.getDeleteButton().setSelected(false);
                                leftSidePanel.getEditButton().setSelected(false);
                                if (gridPanel.IsPlaceImageState()) {
                                    gridPanel.SetEditState(false);
                                } else {
                                    gridPanel.SetImagePlaceState(pane);
                                }
                            } else if (e.getButton() == MouseEvent.BUTTON3) {   // Right click
                                popup.show(label, e.getX(), e.getY());

                            }
                        }
                    });
                }
            }

            if (source == leftSidePanel.getMazeStatsButton()) {
                float deadCellsPercentage = new Algorithm().showDeadCells(gridPanel.getGridMazeCellArray());
                JOptionPane.showMessageDialog(mainPanel, "Percentage of dead cells: " +
                        deadCellsPercentage + "%");
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
                boolean randomiseMaze;

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

                JTextField mazeNameText = new JTextField("New Maze", 20);
                JTextField mazeAuthorText = new JTextField("Unknown", 20);

                JCheckBox randomMazeOption = new JCheckBox();

                JPanel newMazeOptions = new JPanel();
                newMazeOptions.setLayout(new GridLayout(5, 1, 0, 10));
                newMazeOptions.add(new JLabel("Maze Cell Height:"));
                newMazeOptions.add(mazeHeightText);
                newMazeOptions.add(new JLabel("Maze Cell Width:"));
                newMazeOptions.add(mazeWidthText);
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
                    gridPanel.SetEditState(false);
                    newMaze = true;
                }
            }

            if (source == setting) {
                JTextField mazeNameText = new JTextField(mazeName, 20);
                JTextField mazeAuthorText = new JTextField(author, 20);
                JPanel mazeSettings = new JPanel();
                mazeSettings.setLayout(new GridLayout(2, 1, 0, 10));
                mazeSettings.add(new JLabel("Maze Name"));
                mazeSettings.add(mazeNameText);
                mazeSettings.add(new JLabel("Author"));
                mazeSettings.add(mazeAuthorText);

                int result = JOptionPane.showConfirmDialog(null, mazeSettings,
                        "Maze Settings", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    mazeName = mazeNameText.getText();
                    author = mazeAuthorText.getText();
                }
            }

            if (source == open) {
                openMaze();
            }

            if (source == save) {
                saveMaze();
            }

            if (source == export) {

            }

//            if (source == /* editMaze (?) */) {
//
//            }
        }

        public void itemStateChanged(ItemEvent e) {
            Component source = (Component) e.getSource();
            if (source == leftSidePanel.getEditButton()) {
                if (leftSidePanel.getEditButton().isSelected()) {
                    leftSidePanel.getDeleteButton().setSelected(false);
                    gridPanel.SetEditState(true);
                } else {
                    gridPanel.SetEditState(false);
                }
            }

            if (source == leftSidePanel.getDeleteButton()) {
                if (leftSidePanel.getDeleteButton().isSelected()) {
                    leftSidePanel.getEditButton().setSelected(false);
                    gridPanel.SetRemoveImageState();
                } else {
                    gridPanel.SetEditState(false);
                }
            }
        }
    }
}
