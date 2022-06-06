import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
    private boolean newMaze = false;
    private int currentMazeId;
    private JList dataOpen;
    private HashMap<Integer, List<Object>> mazeDetailsList;
    private JLabel mazeDetailsLabel;

    private JPanel mainPanel;
    private RightSideBarPanel rightSidePanel;
    private JScrollPane rightSidePanelScroll;
    private LeftSideBarPanel leftSidePanel;
    private GridPanel gridPanel;
    private JScrollPane GridPanel;
    private ArrayList<ImagePane> paneList = new ArrayList<>();
    private Connection connection;
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
        rightSidePanelScroll = new JScrollPane(rightSidePanel);
        rightSidePanelScroll.setPreferredSize(new Dimension(200, 300));
        mainPanel.add(rightSidePanelScroll, BorderLayout.EAST);
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
        String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS maze_program ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL UNIQUE,"
                + "maze_name VARCHAR(50) NOT NULL,"
                + "author VARCHAR(50),"
                + "date_time DATETIME,"
                + "maze_data LONGBLOB,"
                + "image_data LONGBLOB,"
                + "maze_cell_height INT,"
                + "maze_cell_width INT" + ");";

        String CREATE_IMAGE_TABLE =
                "CREATE TABLE IF NOT EXISTS maze_images("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL UNIQUE,"
                + "image_data LONGBLOB,"
                + "image_width INT,"
                + "image_height INT,"
                + "maze_id INTEGER,"
                + "FOREIGN KEY (maze_id) REFERENCES maze_program(id) ON DELETE CASCADE" + ");";
        try {
            connection.createStatement().execute(CREATE_TABLE);
            connection.createStatement().execute(CREATE_IMAGE_TABLE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Saves existing/
     * new maze into the database with custom parameters
     */
    public void saveMaze() {
        JLabel statusMessage = new JLabel();
        try{
            if (newMaze) {
                PreparedStatement saveMazeData = connection.prepareStatement("INSERT INTO " +
                        "maze_program(author, " +
                        "maze_name, maze_data, image_data," +
                        "maze_cell_width, maze_cell_height, " +
                        "date_time) VALUES(?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
                saveMazeData.setString(1, author);
                saveMazeData.setString(2, mazeName);
                saveMazeData.setString(3, mazeToString(gridPanel.getGridMazeCellArray()));
                saveMazeData.setString(4, gridImagesToString(gridPanel.GetImageMap()));
                saveMazeData.setInt(5, mazeCellWidth);
                saveMazeData.setInt(6, mazeCellHeight);
                saveMazeData.setTimestamp(7, new java.sql.Timestamp(new java.util.Date().getTime()));
                saveMazeData.executeUpdate();
                ResultSet mazeKey = saveMazeData.getGeneratedKeys();
                if (mazeKey.next()) {
                   saveImagesToDatabase( mazeKey.getInt(1));
                }
                statusMessage.setText("maze successfully saved");
            } else {
               // update current database instead
                PreparedStatement updateMazeData = connection.prepareStatement("UPDATE " +
                        "maze_program SET author = ?, maze_name = ?, maze_data = ?, image_data = ?, " +
                        "maze_cell_width = ?, maze_cell_height = ?, date_time = ? WHERE id = ?");
                updateMazeData.setString(1, author);
                updateMazeData.setString(2, mazeName);
                updateMazeData.setString(3, mazeToString(gridPanel.getGridMazeCellArray()));
                updateMazeData.setString(4, gridImagesToString(gridPanel.GetImageMap()));
                updateMazeData.setInt(5, mazeCellWidth);
                updateMazeData.setInt(6, mazeCellHeight);
                updateMazeData.setTimestamp(7, new java.sql.Timestamp(new java.util.Date().getTime()));
                updateMazeData.setInt(8, currentMazeId);
                updateMazeData.execute();

                PreparedStatement clearImages = connection.prepareStatement("DELETE " +
                        "FROM maze_images WHERE `maze_id` = ?");
                clearImages.setInt(1, currentMazeId);
                clearImages.execute();
                saveImagesToDatabase(currentMazeId);
                statusMessage.setText("maze successfully updated");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            statusMessage.setText("error in saving maze: Error " + ex.getErrorCode());
        }

        JDialog statusDialog = new JDialog();
        JButton okButton = new JButton("ok");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                statusDialog.setVisible(false);
            }
        });

        JPanel statusPanel = new JPanel();
        statusPanel.add(statusMessage);
        statusPanel.add(okButton);
        statusDialog.add(statusPanel);
        statusDialog.setSize(200, 100);
        statusDialog.setVisible(true);
        newMaze = false;
    }

    protected void saveImagesToDatabase(int mazeId) {
        try {
            //insert all images into the database
            for (int i = 0; i < paneList.size(); i++) {
                PreparedStatement saveMazeImages = connection.prepareStatement("INSERT INTO maze_images(maze_id, image_data, " +
                        "image_width, image_height) VALUES (?,?,?,?)");
                saveMazeImages.setInt(1, mazeId);
                saveMazeImages.setString(2, imageToString(paneList.get(i).getOriginalImage()));
                saveMazeImages.setInt(3, paneList.get(i).getImageCellWidth());
                saveMazeImages.setInt(4, paneList.get(i).getImageCellHeight());
                saveMazeImages.execute();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected void SaveGridImage() {
        BufferedImage bi = ScreenImage.createImage(gridPanel);
        try {
            ScreenImage.writeImage(bi, "panel.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** retrieves data from the database,
     * displays retrieved data as a list into a dialog box
     * returns a hashmap with maze and result row id **/
    public HashMap<Integer, List<Object>> openMazeList(DefaultListModel mazeList){
        ResultSet rs = null;
        HashMap<Integer, List<Object>> mazeResultList = new HashMap<Integer, List<Object>>();
        try {
            PreparedStatement openMazeData = connection.prepareStatement("SELECT * FROM maze_program");
            rs = openMazeData.executeQuery();
            while (rs.next()) {
              List mazeDetails = new ArrayList<>(4);
              mazeDetails.add(rs.getInt("id"));
              mazeDetails.add(rs.getString("author"));
              mazeDetails.add(rs.getString("date_time"));
              mazeResultList.put(rs.getRow(), mazeDetails);
              String mazeName = rs.getString("maze_name");
              mazeList.addElement(mazeName);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
       }
        return mazeResultList;
    }

    /** retrieves entire selected maze data from the database,
     * changes current maze data to selected maze data
     * displays maze and any image(s) into the GUI **/
    protected void openSelectedMaze(int selectedMazeId) {
        ResultSet mazeResult = null;
        try {
            PreparedStatement openMaze = connection.prepareStatement("SELECT * FROM maze_program WHERE `id` = ?");
            openMaze.setInt(1, selectedMazeId);
            mazeResult = openMaze.executeQuery();
            while (mazeResult.next()) {
                mazeName = mazeResult.getString("maze_name");
                author = mazeResult.getString("author");
                Blob mazeData = mazeResult.getBlob("maze_data");
                Blob imageData = mazeResult.getBlob("image_data");
                int mazeCellHeight = mazeResult.getInt("maze_cell_height");
                int mazeCellWidth = mazeResult.getInt("maze_cell_width");

                // display maze into gridpanel
                mainPanel.remove(GridPanel);
                gridPanel = new GridPanel();
                gridPanel.CreateGrid(mazeCellWidth, mazeCellHeight);
                GridPanel = new JScrollPane(gridPanel);
                mainPanel.add(GridPanel, BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
                MazeCell[][] maze = stringToMaze(new String(mazeData.getBytes(1, (int) mazeData.length())), mazeCellHeight, mazeCellWidth);

                //get images from database
                ResultSet imageResult;
                PreparedStatement getMazeImages = connection.prepareStatement("SELECT * FROM maze_images " +
                        "WHERE `maze_id` = ?");
                getMazeImages.setInt(1, selectedMazeId);
                imageResult = getMazeImages.executeQuery();
                for (int i = 0; i < paneList.size(); i++) {
                    rightSidePanel.removeImage(paneList.get(i).getLabel());
                }
                paneList.clear();
                while(imageResult.next()) {
                    Blob imageBlob = imageResult.getBlob("image_data");
                    ImageIcon paneImageResult = new ImageIcon(stringToImage(new String(imageBlob.getBytes(1, (int) imageBlob.length()))));
                    int imageWidth  = imageResult.getInt("image_width");
                    int imageHeight = imageResult.getInt("image_height");
                    ImagePane paneImage = new ImagePane(paneImageResult, imageWidth, imageHeight);
                    addImage(paneImage);
                }
                String gridImageData = new String(imageData.getBytes(1, (int) imageData.length()));
                if (!gridImageData.equals("")) {
                    GridImage[] gridImagesResult = stringToGridImages(gridImageData);
                    gridPanel.CreateMaze(maze, gridImagesResult);
                } else {
                    gridPanel.CreateMaze(maze);
                }
                gridPanel.SetEditState(false);
                newMaze = false;
                currentMazeId = selectedMazeId;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected boolean deleteSelectedMaze(int selectedMazeId) {
        try {
            PreparedStatement deleteMaze = connection.prepareStatement("DELETE" +
                    " FROM maze_program WHERE `id` = ?");
            deleteMaze.setInt(1, selectedMazeId);
            deleteMaze.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
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

    protected void addDataListListener(ListSelectionListener listener, JList dataList) {
        dataList.addListSelectionListener(listener);
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        new MainGUI();
    }

    @Override
    public void run() {

    }

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
            if (index == -1) {
            }
            imageDataString.append(cellx).append(",");
            imageDataString.append(celly).append(",");
            imageDataString.append(imageCellWidth).append(",");
            imageDataString.append(imageCellHeight).append(",");
            imageDataString.append(index).append(";");
        }
        return imageDataString.toString();
    }

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

    // Sourced from https://www.tutorialspoint.com/How-to-convert-Byte-Array-to-Image-in-java
    // Sourced from http://electrocarta.blogspot.com/2017/05/how-to-convert-imageicon-to-base64.html
    public String imageToString(ImageIcon icon) {
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", bos );
        } catch (IOException ex){
            return null;
        }
        byte[] data = bos.toByteArray();
        return Base64.getEncoder().encodeToString(data);
    }

    // Sourced from https://www.tutorialspoint.com/How-to-convert-Byte-Array-to-Image-in-java
    public BufferedImage stringToImage(String string) {
        byte[] data = Base64.getDecoder().decode(string);
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        BufferedImage image;
        try {
            image = ImageIO.read(bis);
        } catch (IOException ex){
            return null;
        }
        return image;
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
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    int imageHeight = Integer.parseInt(imageHeightText.getText());
                    int imageWidth = Integer.parseInt(imageWidthText.getText());
                    if (imageHeight < 1 || imageWidth < 1) {
                        JOptionPane.showMessageDialog(mainPanel, "Input was invalid please try again");
                    } else {
                        pane.setImageCellHeight(imageHeight);
                        pane.setImageCellWidth(imageWidth);
                    }
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

    private class Listener implements ListSelectionListener, ActionListener, ItemListener {
        public void actionPerformed(ActionEvent e) {
            Component source = (Component) e.getSource();
            if (source == rightSidePanel.getNewImage() || source == impImage) {
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
                            return;
                        }
                        ImageIcon icon = new ImageIcon(image);
                        ImagePane pane = new ImagePane(icon, 2, 2);
                        addImage(pane);
                    }
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "You cannot add more than 10 images");
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

            if (source == createMaze) {
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
                        leftSidePanel.getEditButton().setSelected(false);
                        leftSidePanel.getDeleteButton().setSelected(false);
                        gridPanel.SetEditState(false);
                        newMaze = true;
                    }
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
                JPanel openMazePanel = new JPanel();
                JPanel buttonPanel = new JPanel();
                JDialog openDialog = new JDialog();
                openDialog.setTitle("Open Maze");
                openDialog.setModal(true);
                JButton delete = new JButton("Delete");
                JButton okButton = new JButton("Ok");
                JButton cancelButton = new JButton("Cancel");
                mazeDetailsLabel = new JLabel();
                mazeDetailsLabel.setMinimumSize(new Dimension(400, 15));
                mazeDetailsLabel.setPreferredSize(new Dimension(400, 15));
                DefaultListModel dataList = new DefaultListModel();
                mazeDetailsList = openMazeList(dataList);
                dataOpen = new JList(dataList);
                addDataListListener(new Listener(), dataOpen);
                JScrollPane openList = new JScrollPane(dataOpen);
                openMazePanel.setLayout(new BoxLayout(openMazePanel, BoxLayout.PAGE_AXIS));
                openMazePanel.add(openList);
                openMazePanel.add(mazeDetailsLabel);
                buttonPanel.add(okButton);
                buttonPanel.add(cancelButton);
                buttonPanel.add(delete);
                openMazePanel.setSize(400, 200);
                openDialog.setLayout(new BorderLayout());
                openDialog.add(openMazePanel, BorderLayout.CENTER);
                buttonPanel.setSize(400,50);
                openDialog.add(buttonPanel,BorderLayout.SOUTH);

                okButton.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (okButton.getModel().isPressed()){
                            if (dataOpen.getSelectedValue() != null && !dataOpen.getSelectedValue().equals("")) {
                                int selectedMazeId = (Integer) mazeDetailsList.get((dataOpen.getSelectedIndex() + 1)).get(0);
                                openSelectedMaze(selectedMazeId);
                                openDialog.setVisible(false);
                            } else {
                                mazeDetailsLabel.setText("please select a maze first");
                            }
                        }
                    }
                });

                delete.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (delete.getModel().isPressed()){
                            int selectedMazeId = (Integer) mazeDetailsList.get((dataOpen.getSelectedIndex() + 1)).get(0);
                            if (deleteSelectedMaze(selectedMazeId)) {
                                dataList.clear();
                                mazeDetailsList = openMazeList(dataList);
                            };
                        }

                    }
                });

                cancelButton.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (cancelButton.getModel().isPressed()){
                            openDialog.setVisible(false);
                        }
                    }
                });

                openDialog.setSize(new Dimension(500, 250));
                openDialog.setModal(true);
                openDialog.setVisible(true);
            }

            if (source == save) {
                JTextField mazeNameText = new JTextField(mazeName, 20);
                JTextField mazeAuthorText = new JTextField(author, 20);
                JPanel saveMaze = new JPanel();
                saveMaze.setLayout(new GridLayout(2, 1, 0, 10));
                saveMaze.add(new JLabel("Maze Name"));
                saveMaze.add(mazeNameText);
                saveMaze.add(new JLabel("Author"));
                saveMaze.add(mazeAuthorText);

                int result = JOptionPane.showConfirmDialog(null, saveMaze,
                        "Maze Settings", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    mazeName = mazeNameText.getText();
                    author = mazeAuthorText.getText();
                    saveMaze();
                }
            }

            if (source == export) {

            }
        }

        public void valueChanged(ListSelectionEvent e) {
            if (dataOpen.getSelectedValue() != null
            && !dataOpen.getSelectedValue().equals("")) {
                String mazeAuthor = (String) mazeDetailsList.get((dataOpen.getSelectedIndex() + 1)).get(1);
                String mazeDate = (String) mazeDetailsList.get((dataOpen.getSelectedIndex() + 1)).get(2);
                mazeDetailsLabel.setText("Last edited: " + mazeDate + "          Author: " + mazeAuthor);
            }
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
}
