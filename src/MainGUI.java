import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Class used to manage the maze program.
 */
public class MainGUI extends JFrame {

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
    private boolean newMaze = false;
    private int currentMazeId;
    private MazeDetails detailsOfMaze;

    private JPanel mainPanel;
    private RightSideBarPanel rightSidePanel;
    private JScrollPane rightSidePanelScroll;
    private LeftSideBarPanel leftSidePanel;
    private GridPanel gridPanel;
    private JScrollPane GridPanel;
    private ArrayList<ImagePane> paneList = new ArrayList<>();
    private FileInputStream imageDataFile;
    private ImageConverter convert;
    private MazeDataSource dataSource;
    private MainGUI frame;

    public MainGUI(){
        super("Maze Creator");
        initGUI();
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
        //Calls the leftSideBarPanel class:
        //Sets up the dimension for the leftSidePanel
        //Returns an interactive sidebar to the left of the main panel
        leftSidePanel = new LeftSideBarPanel();
        leftSidePanel.setPreferredSize(new Dimension(100, 300));
        mainPanel.add(leftSidePanel, BorderLayout.WEST);
        leftSidePanel.addActionListener(new Listener());
        leftSidePanel.addItemListener(new Listener());

        //rightSidePanel
        //Calls the rightSideBarPanel class:
        //Sets up the dimension for the rightSidePanel
        //Return an interactive sidebar to the right side of the main panel
        rightSidePanel = new RightSideBarPanel();
        rightSidePanelScroll = new JScrollPane(rightSidePanel);
        rightSidePanelScroll.setPreferredSize(new Dimension(200, 300));
        mainPanel.add(rightSidePanelScroll, BorderLayout.EAST);
        rightSidePanel.addActionListener(new Listener());

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
        //Sets up the menu bar
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

        frame = this;
        dataSource = new MazeDataSource(frame);
        convert = new ImageConverter();
        detailsOfMaze = new MazeDetails();
    }

    /**
     * Adds an action listener to the buttons in the menu bar
     * @param listener ActionListener
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
     * Saves existing/ new maze into the database with custom parameters
     */
    public void saveMaze() {
        String statusMessage;
        MazeDetails maze = new MazeDetails(author, mazeName,
                mazeToString(gridPanel.getGridMazeCellArray()),
                gridImagesToString(gridPanel.GetImageMap()), mazeCellWidth, mazeCellHeight,
                getCurrentMazeImage(), getCurrentSolutionImage());
        if (newMaze) {
            statusMessage = dataSource.saveNewMaze(maze);
            currentMazeId = maze.id;
        } else {
            maze.id = currentMazeId;
            statusMessage = dataSource.updateMaze(maze);
            dataSource.clearImages(maze.id);
        }
        dataSource.saveImages(maze.id, paneList);
        JOptionPane.showMessageDialog(mainPanel, statusMessage);
        newMaze = false;
    }

    /**
     * Retrieves currently displayed maze image and its optimal solution
     * @return images as a string
     */
    protected String getCurrentMazeImage() {
        boolean optimalSelected = leftSidePanel.getOptimalSolutionButton().isSelected();
        leftSidePanel.getOptimalSolutionButton().setSelected(false);
        String image = convert.imageToString(null, ScreenImage.createImage(gridPanel));
        leftSidePanel.getOptimalSolutionButton().setSelected(optimalSelected);
        return image;
    }

    /**
     * Retrieves currently displayed maze optimal solution
     * @return images as a string
     */
    protected String getCurrentSolutionImage() {
        boolean optimalSelected = leftSidePanel.getOptimalSolutionButton().isSelected();
        leftSidePanel.getOptimalSolutionButton().setSelected(true);
        String image = convert.imageToString(null, ScreenImage.createImage(gridPanel));
        leftSidePanel.getOptimalSolutionButton().setSelected(optimalSelected);
        return image;
    }

    /**
     * displays retrieved maze data into the MainGUI as a maze
     */
    protected MazeCell[][] displayMaze(String mazeData, int width, int height) {
        if (GridPanel != null) {
            mainPanel.remove(GridPanel);
        }
        gridPanel = new GridPanel();
        gridPanel.CreateGrid(width, height);
        GridPanel = new JScrollPane(gridPanel);
        mainPanel.add(GridPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
        MazeCell[][] maze = stringToMaze(mazeData, width, height);
        return maze;
    }

    /**
     * Clears the panelist entirely of images
     */
    protected void clearPaneList() {
        if (paneList.size() > 0) {
            for (ImagePane pane : paneList) {
                rightSidePanel.removeImage(pane.getLabel());
            }
            paneList.clear();
        }
    }

    /**
     * retrieves entire selected maze data from the database,changes current maze data to selected maze data displays
     * maze and any image(s) into the GUI
     * @param selectedMazeId the selected maze's id
     */
    protected void openSelectedMaze(int selectedMazeId) {
        MazeDetails maze = dataSource.getSelectedMaze(selectedMazeId);
        dataSource.getMazeImages(selectedMazeId, maze);
        mazeName = maze.name;
        author = maze.author;
        MazeCell[][] mazeCellData = displayMaze(maze.mazeData, maze.width, maze.height);
        clearPaneList();
        for (ImagePane image : maze.allImages) {
            addImage(image);
        }
        if (!maze.imageData.equals("")) {
            GridImage[] gridImagesResult = stringToGridImages(maze.imageData);
            gridPanel.CreateMaze(mazeCellData, gridImagesResult);
        } else {
            gridPanel.CreateMaze(mazeCellData);
        }
        gridPanel.SetEditState(false);
        newMaze = false;
        currentMazeId = selectedMazeId;
    }

    /**
     * Converts all the data from the images located within the grid panel into a single string
     * @param gridImages The data for all the images located within the grid panel
     * @return a string containing all the data from the images located in the grid panel
     */
    public String gridImagesToString(HashMap<List<Integer>, GridImage> gridImages) {
        StringBuilder imageDataString = new StringBuilder();
        int cellx;
        int celly;
        int imageCellWidth;
        int imageCellHeight;
        ImageIcon image;
        int index = -1;
        for (List<Integer> list : gridImages.keySet()) {
            cellx = list.get(0);
            celly = list.get(1);
            imageCellWidth = gridImages.get(list).WIDTH;
            imageCellHeight = gridImages.get(list).HEIGHT;
            image = gridImages.get(list).image;
            for (int i = 0; i < paneList.size(); i++) {
                if (image == paneList.get(i).getOriginalImage()) {
                    index = i;
                }
            }
            imageDataString.append(cellx).append(",");
            imageDataString.append(celly).append(",");
            imageDataString.append(imageCellWidth).append(",");
            imageDataString.append(imageCellHeight).append(",");
            imageDataString.append(index).append(";");
        }
        return imageDataString.toString();
    }

    /**
     * Converts a single string containing data about images for a grid panel into a GridImage list which the grid panel
     * can use to populate the grid.
     * @param string A string containing data about images inside a grid panel.
     * @return a GridImage list of data the grid panel can use
     */
    public GridImage[] stringToGridImages(String string) {
        int cellx;
        int celly;
        int imageCellWidth;
        int imageCellHeight;
        ImageIcon image;
        String[] imageData = string.split(";", 0);
        GridImage[] gridImages = new GridImage[imageData.length];
        int i = 0;
        for (String imageDataNum : imageData) {
            String[] data = imageDataNum.split(",", 0);
            cellx = Integer.parseInt(data[0]);
            celly = Integer.parseInt(data[1]);
            imageCellWidth = Integer.parseInt(data[2]);
            imageCellHeight = Integer.parseInt(data[3]);
            image = paneList.get(Integer.parseInt(data[4])).getOriginalImage();
            GridImage gridImage = new GridImage(0, 0,cellx ,celly ,imageCellWidth, imageCellHeight, image);
            gridImages[i] = gridImage;
            i++;
        }
        return gridImages;
    }

    /**
     * Converts the wall data from a two-dimensional MazeCell into a string format
     * @param mazeData A two dimensional MazeCell representing a maze
     * @return A string representing the wall states for each cell in a maze
     */
    public String mazeToString(MazeCell[][] mazeData) {
        StringBuilder mazeString = new StringBuilder();
        for (MazeCell[] mazeDatum : mazeData) {
            for (int j = 0; j < mazeData[0].length; j++) {
                if (mazeDatum[j].getWallUp()) {
                    mazeString.append("1");
                } else {
                    mazeString.append("0");
                }
                if (mazeDatum[j].getWallRight()) {
                    mazeString.append("1");
                } else {
                    mazeString.append("0");
                }
                if (mazeDatum[j].getWallDown()) {
                    mazeString.append("1");
                } else {
                    mazeString.append("0");
                }
                if (mazeDatum[j].getWallLeft()) {
                    mazeString.append("1");
                } else {
                    mazeString.append("0");
                }
            }
        }
        return mazeString.toString();
    }

    /**
     * Converts a string representing the states of each cell of a maze into a two-dimensional MazeCell array.
     * @param mazeString A string representing the states of each cell in a maze
     * @param mazeCellHeight The cell height of a maze
     * @param mazeCellWidth The cell width of a maze
     * @return A two-dimensional MazeCell array representing a maze
     */
    public MazeCell[][] stringToMaze(String mazeString, int mazeCellHeight, int mazeCellWidth) {
        Maze maze = new Maze(mazeCellHeight, mazeCellWidth, false);
        MazeCell[][] mazeCells = maze.getMaze();
        int x = 0;
        for (MazeCell[] mazeCell : mazeCells) {
            for (int j = 0; j < mazeCells[0].length; j++) {
                if (mazeString.charAt(x) == '1') {
                    mazeCell[j].toggleWallUp();
                }
                if (mazeString.charAt(x + 1) == '1') {
                    mazeCell[j].toggleWallRight();
                }
                if (mazeString.charAt(x + 2) == '1') {
                    mazeCell[j].toggleWallDown();
                }
                if (mazeString.charAt(x + 3) == '1') {
                    mazeCell[j].toggleWallLeft();
                }
                x += 4;
            }
        }
        return mazeCells;
    }

    /**
     * Opens a file chooser dialog and adds the image to the rightSideBarPanel
     * @return True if there is an exception and false otherwise
     */
    private boolean newImageButton() {
        if (paneList.size() < 10) {
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
                    return true;
                }
                ImageIcon icon = new ImageIcon(image);
                ImagePane pane = new ImagePane(icon, 2, 2);
                addImage(pane);
            }
        } else {
            JOptionPane.showMessageDialog(mainPanel, "You cannot add more than 10 images");
        }
        return false;
    }

    /**
     * Adds an image to the rightSideBarPanel and sets up a delete and settings option in a JMenuItem when the image
     * is right-clicked
     * @param pane The ImagePane object containing the image and its data
     */
    public void addImage(ImagePane pane) {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem delete = new JMenuItem("Delete");
        JMenuItem settings = new JMenuItem("Settings");
        popup.add(delete);
        popup.add(settings);
        JLabel label = new JLabel(pane.resizeImage(200, 200));
        pane.setLabel(label);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightSidePanel.addImage(label);
        paneList.add(pane);
        delete.addActionListener(e -> {
            int numGridImages = 0;
            for (GridImage gridImage : gridPanel.GetImageMap().values()) {
                if (gridImage.image == pane.getOriginalImage()) {
                    numGridImages++;
                }
            }
            if (numGridImages != 0) {
                JOptionPane.showMessageDialog(mainPanel, "This image is still located on the maze");
            } else {
                rightSidePanel.removeImage(label);
                paneList.remove(pane);
            }
        });
        settings.addActionListener(e -> {
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
                int imageHeight = Integer.parseInt(imageHeightText.getText());
                int imageWidth = Integer.parseInt(imageWidthText.getText());
                if (imageHeight < 1 || imageWidth < 1) {
                    JOptionPane.showMessageDialog(mainPanel, "Input was invalid please try again");
                } else {
                    pane.setImageCellHeight(imageHeight);
                    pane.setImageCellWidth(imageWidth);
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
                    leftSidePanel.getOptimalSolutionButton().setSelected(false);
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

    /**
     * Opens up a dialog box for changing maze settings
     */
    private void settingsButton() {
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

    /**
     * Runs the showOptimalCells and showDeadCells algorithms in the Algorithm class and displays the results as a
     * percentage in a dialog box
     */
    private void mazeStatsButton() {
        float deadCellsPercentage = new Algorithm().showDeadCells(gridPanel.getGridMazeCellArray());
        float optimalCellsPercentage;
        MazeCell[][] maze = new Algorithm().mazeSolvability(gridPanel.getGridMazeCellArray());
        if (maze != null) {
            optimalCellsPercentage = new Algorithm().showOptimalCells(maze);
        } else {
            optimalCellsPercentage = 0;
        }

        JOptionPane.showMessageDialog(mainPanel, "Percentage of dead cells: " +
                deadCellsPercentage + "%\n" + "Percentage of optimal cells: " + optimalCellsPercentage + "%");
    }

    /**
     * Opens a dialog box containing maze options for the user to modify and then generates a new maze using those
     * options
     */
    private void createMazeButton() {
        // Possibly make a new method for most of this part
        boolean randomiseMaze;

        // Sourced from https://stackhowto.com/how-to-make-jtextfield-accept-only-numbers/
        KeyAdapter onlyInt = new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
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
            int mazeHeight = Integer.parseInt(mazeHeightText.getText());
            int mazeWidth = Integer.parseInt(mazeWidthText.getText());
            if (mazeHeight < 2 || mazeHeight > 100 || mazeWidth < 2 || mazeWidth > 100) {
                JOptionPane.showMessageDialog(mainPanel, "Input was invalid please try again");
            } else {
                mazeCellHeight = Integer.parseInt(mazeHeightText.getText());
                mazeCellWidth = Integer.parseInt(mazeWidthText.getText());
                mazeName = mazeNameText.getText();
                author = mazeAuthorText.getText();
                randomiseMaze = randomMazeOption.isSelected();

                if (GridPanel != null) {
                    mainPanel.remove(GridPanel);
                }
                gridPanel = new GridPanel();
                gridPanel.CreateGrid(mazeCellWidth, mazeCellHeight);
                GridPanel = new JScrollPane(gridPanel);
                mainPanel.add(GridPanel, BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();

                if (randomiseMaze) {
                    MazeCell[][] newMaze = new Algorithm().generateMaze(mazeCellWidth, mazeCellHeight);
                    gridPanel.CreateMaze(newMaze);
                }
                clearPaneList();
                leftSidePanel.getEditButton().setSelected(false);
                leftSidePanel.getDeleteButton().setSelected(false);
                gridPanel.SetEditState(false);
                newMaze = true;
            }
        }
    }

    /**
     * gets MazeDetails object
     * @return mazedetails
     */
    public MazeDetails getMazeDetails() {
        return detailsOfMaze;
    }

    /**
     * gets currently displayed maze id
     * @return maze id
     */
    public int getCurrentMazeId() {
        return currentMazeId;
    }

    /**
     * sets whether the maze is new or not
     * @param isNew boolean value of whether the maze is new or not
     */
    public void setNewMaze(boolean isNew) {
        newMaze = isNew;
    }

    /**
     * An ActionListener for all the buttons and toggle buttons in the maze program
     */
    private class Listener implements ActionListener, ItemListener {
        public void actionPerformed(ActionEvent e) {
            Component source = (Component) e.getSource();
            if ((source == rightSidePanel.getNewImage() || source == impImage) && GridPanel != null) {
                if (newImageButton()) return;
            }

            if (source == leftSidePanel.getMazeStatsButton() && GridPanel != null) {
                mazeStatsButton();
            }

            if (source == leftSidePanel.getSolvableButton() && GridPanel != null) {
                if (new Algorithm().mazeSolvability(gridPanel.getGridMazeCellArray()) != null) {
                    JOptionPane.showMessageDialog(mainPanel, "The maze is solvable");
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "The maze is not solvable");
                }
            }

            if (source == createMaze) {
                createMazeButton();
            }

            if (source == setting && GridPanel != null) {
                settingsButton();
            }

            if (source == open) {
               OpenOptions openOptionsDialog = new OpenOptions(dataSource, frame);
            }

            if (source == save && GridPanel != null) {
                JTextField mazeNameText = new JTextField(mazeName, 20);
                JTextField mazeAuthorText = new JTextField(author, 20);
                JPanel saveMaze = new JPanel();
                saveMaze.setLayout(new GridLayout(2, 1, 0, 10));
                saveMaze.add(new JLabel("Maze Name"));
                saveMaze.add(mazeNameText);
                saveMaze.add(new JLabel("Author"));
                saveMaze.add(mazeAuthorText);
                int result = JOptionPane.showConfirmDialog(null, saveMaze,
                        "Save Maze", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    mazeName = mazeNameText.getText();
                    author = mazeAuthorText.getText();
                    saveMaze();
                }
            }

            if (source == export && GridPanel != null) {
                MazeDetails maze = new MazeDetails();
                maze.name = mazeName;
                maze.mazeImage = getCurrentMazeImage();
                maze.mazeSolution = getCurrentSolutionImage();
                ExportMaze exportDialog = new ExportMaze(new MazeDetails[]{maze}, detailsOfMaze.getList());
            }
        }

        public void itemStateChanged(ItemEvent e) {
            Component source = null;
            if (gridPanel != null) {
                source = (Component) e.getSource();
            }
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

            if (source == leftSidePanel.getOptimalSolutionButton()) {
                if (leftSidePanel.getOptimalSolutionButton().isSelected()) {
                    MazeCell[][] maze = new Algorithm().mazeSolvability(gridPanel.getGridMazeCellArray());
                    if (maze != null) {
                        // maze below needs to be used to display the maze
                        maze = new Algorithm().optimalSolution(maze);
                        gridPanel.ShowSolutionLine(maze);
                    } else {
                        JOptionPane.showMessageDialog(mainPanel, "The maze is not solvable");
                        leftSidePanel.getOptimalSolutionButton().setSelected(false);
                    }
                } else {
                    gridPanel.ShowSolutionLine(false);
                    gridPanel.ResetGridColors();
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        new MainGUI();
    }
}
