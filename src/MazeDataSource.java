import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MazeDataSource {

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS maze_program ("
            + "id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL UNIQUE,"
            + "maze_name VARCHAR(50) NOT NULL,"
            + "author VARCHAR(50),"
            + "created_date DATETIME,"
            + "last_edited DATETIME,"
            + "maze_data LONGBLOB,"
            + "image_data LONGBLOB,"
            + "maze_cell_height INT,"
            + "maze_cell_width INT,"
            + "maze_image LONGBLOB,"
            + "maze_optimal_solution LONGBLOB" + ");";

    public static final String CREATE_IMAGE_TABLE = "CREATE TABLE IF NOT EXISTS maze_images("
            + "id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL UNIQUE,"
            + "image_data LONGBLOB,"
            + "image_width INT,"
            + "image_height INT,"
            + "maze_id INTEGER,"
            + "FOREIGN KEY (maze_id) REFERENCES maze_program(id) ON DELETE CASCADE" + ");";

    public static final String OPEN_MAZE = "SELECT * FROM maze_program WHERE `id` = ?";

    public static final String SAVE_MAZE = "INSERT INTO " +
            "maze_program(author, " +
            "maze_name, maze_data, image_data," +
            "maze_cell_width, maze_cell_height, " +
            "created_date, last_edited, maze_image, maze_optimal_solution) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?)";

    public static final String UPDATE_MAZE = "UPDATE " +
            "maze_program SET author = ?, maze_name = ?, maze_data = ?, image_data = ?, " +
            "maze_cell_width = ?, maze_cell_height = ?, last_edited = ?, maze_image = ?, " +
            "maze_optimal_solution = ? WHERE id = ?";

    public static final String SAVE_IMAGES = "INSERT INTO maze_images(maze_id, image_data, " +
            "image_width, image_height) VALUES (?,?,?,?)";

    public static final String CLEAR_MAZE_IMAGES = "DELETE " + "FROM maze_images WHERE `maze_id` = ?";

    public static final String GET_MAZE_IMAGES = "SELECT * FROM maze_images WHERE `maze_id` = ?";

    public String DELETE_MAZE = "DELETE FROM maze_program WHERE `id` IN (";

    public String GET_MAZE_IMAGE = "SELECT maze_name, maze_image, maze_optimal_solution " +
            "FROM maze_program WHERE `id` IN (";

    private Connection connection;
    private ImageConverter convert;

    /**
     * Initialises database,
     * creates database table for the maze program,
     * adds current user to the database table
     */
    public MazeDataSource(MainGUI frame) {
        connection = DataConnect.getInstance(frame);
        try {
            connection.createStatement().execute(CREATE_TABLE);
            connection.createStatement().execute(CREATE_IMAGE_TABLE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        convert = new ImageConverter();
    }

    /**
     * saves a new maze into the database
     * @param maze details of the maze in question
     * @return status message of whether the task was successfull
     */
    public String saveNewMaze(MazeDetails maze) {
        try {
            PreparedStatement saveMazeData = connection.prepareStatement(SAVE_MAZE, Statement.RETURN_GENERATED_KEYS);
            saveMazeData.setString(1, maze.author);
            saveMazeData.setString(2, maze.name);
            saveMazeData.setString(3, maze.mazeData);
            saveMazeData.setString(4, maze.imageData);
            saveMazeData.setInt(5, maze.width);
            saveMazeData.setInt(6, maze.height);
            saveMazeData.setTimestamp(7, new java.sql.Timestamp(new java.util.Date().getTime()));
            saveMazeData.setTimestamp(8, new java.sql.Timestamp(new java.util.Date().getTime()));
            saveMazeData.setString(9, maze.mazeImage);
            saveMazeData.setString(10, maze.mazeSolution);
            saveMazeData.execute();
            ResultSet mazeKey = saveMazeData.getGeneratedKeys();
            if (mazeKey.next()) maze.id = mazeKey.getInt(1);
            return "maze successfully saved";
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "there was an error saving your maze";
        }
    }

    /**
     * saves maze images into the database
     * @param mazeId the id of the maze in question
     * @param paneList the list of images in the panelist to be saved
     */
    public void saveImages(int mazeId, List<ImagePane> paneList) {
        try {
            for (ImagePane imagePane : paneList) {
                PreparedStatement saveMazeImages = connection.prepareStatement(SAVE_IMAGES);
                saveMazeImages.setInt(1, mazeId);
                saveMazeImages.setString(2, convert.imageToString(imagePane.getOriginalImage(), null));
                saveMazeImages.setInt(3, imagePane.getImageCellWidth());
                saveMazeImages.setInt(4, imagePane.getImageCellHeight());
                saveMazeImages.execute();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * updates a specific maze
     * @param maze the details of the maze in question
     * @return a string of the status of whether the update was successfull
     */
    public String updateMaze(MazeDetails maze) {
        try {
            PreparedStatement updateMazeData = connection.prepareStatement(UPDATE_MAZE);
            updateMazeData.setString(1, maze.author);
            updateMazeData.setString(2, maze.name);
            updateMazeData.setString(3, maze.mazeData);
            updateMazeData.setString(4, maze.imageData);
            updateMazeData.setInt(5, maze.width);
            updateMazeData.setInt(6, maze.height);
            updateMazeData.setTimestamp(7, new java.sql.Timestamp(new java.util.Date().getTime()));
            updateMazeData.setString(8, maze.mazeImage);
            updateMazeData.setString(9, maze.mazeSolution);
            updateMazeData.setInt(10, maze.id);
            updateMazeData.execute();
            return "maze successfully updated";
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "there was an error in saving your maze";
        }
    }

    /**
     * deletes all images of a maze
     * @param id the id of the maze in question
     */
    public void clearImages(int id) {
        try {
            PreparedStatement clearImages = connection.prepareStatement(CLEAR_MAZE_IMAGES);
            clearImages.setInt(1, id);
            clearImages.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * deletes a maze
     * @param selectedMazeId id of the maze in question
     * @return a boolean value of whether the deletion was successfull or not
     */
    public boolean deleteMaze(int[] selectedMazeId) {
        try {
            String statement = DELETE_MAZE;
            int i = 0;
            for (int id : selectedMazeId) {
                statement += id;
                if (i < (selectedMazeId.length - 1)) statement += ",";
                else statement += ")";
                i++;
            }
            PreparedStatement deleteMaze = connection.prepareStatement(statement);
            deleteMaze.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * retrieves data from the database,
     * displays retrieved data as a list into a dialog box
     * returns a hashmap with maze and result row id
     **/
    public List<MazeDetails> getMazeList(String order, String secondOrder) {
        ResultSet rs = null;
        List<MazeDetails> mazes = new ArrayList<>();
        switch (order) {
            case "last edited" -> order = "last_edited";
            case "creation date" -> order = "created_date";
            case "maze name" -> order = "maze_name";
        }
        try {
            PreparedStatement openMazeData = connection.prepareStatement("SELECT * FROM maze_program " +
                    "ORDER BY " + order + (secondOrder.equals("ascending") ? " ASC" : " DESC"));
            rs = openMazeData.executeQuery();
            while (rs.next()) {
                MazeDetails maze = new MazeDetails();
                maze.id = (rs.getInt("id"));
                maze.author = rs.getString("author");
                maze.lastEdited = rs.getTimestamp("last_edited");
                maze.name = rs.getString("maze_name");
                mazes.add(maze);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return mazes;
    }

    /**
     * retrieves entire selected maze data from the database,
     * changes current maze data to selected maze data
     * displays maze and any image(s) into the GUI
     **/
    public MazeDetails getSelectedMaze(int selectedMazeId) {
        ResultSet mazeResult;
        MazeDetails maze = new MazeDetails();
        try {
            PreparedStatement openMaze = connection.prepareStatement(OPEN_MAZE);
            openMaze.setInt(1, selectedMazeId);
            mazeResult = openMaze.executeQuery();
            while (mazeResult.next()) {
                maze.name = mazeResult.getString("maze_name");
                maze.author = mazeResult.getString("author");
                Blob mazeData = mazeResult.getBlob("maze_data");
                maze.mazeData = new String(mazeData.getBytes(1, (int) mazeData.length()));
                Blob imageData = mazeResult.getBlob("image_data");
                maze.imageData = new String(imageData.getBytes(1, (int) imageData.length()));
                maze.height = mazeResult.getInt("maze_cell_height");
                maze.width = mazeResult.getInt("maze_cell_width");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return maze;
    }

    /**
     * get all images of a specific maze
     * @param selectedMazeId the id of the maze in question
     * @param maze the MazeDetails object of which the images should be stored in
     */
    public void getMazeImages(int selectedMazeId, MazeDetails maze) {
        try {
            ResultSet imageResult;
            PreparedStatement getMazeImages = connection.prepareStatement(GET_MAZE_IMAGES);
            getMazeImages.setInt(1, selectedMazeId);
            imageResult = getMazeImages.executeQuery();
            while(imageResult.next()) {
                Blob imageBlob = imageResult.getBlob("image_data");
                ImageIcon paneImageResult = new ImageIcon(convert.stringToImage(new String(imageBlob.getBytes(1, (int) imageBlob.length()))));
                maze.imageWidth  = imageResult.getInt("image_width");
                maze.imageHeight = imageResult.getInt("image_height");
                maze.allImages.add(new ImagePane(paneImageResult, maze.imageWidth, maze.imageHeight));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * gets all maze images of multiple mazes
     * @param mazeIds id of the maze in question
     * @return a list of MazeDetails with the relevant information updated
     */
    public MazeDetails[] getMazeImage(int[] mazeIds) {
        MazeDetails mazes[] = new MazeDetails[mazeIds.length];
        String statement = GET_MAZE_IMAGE;
        try {
            for (int i = 0; i < mazeIds.length; i++) {
                statement += mazeIds[i];
                if (i < (mazeIds.length - 1)) statement += ",";
                else if (i == (mazeIds.length - 1)) statement += ")";
            }
            PreparedStatement getMazeImage = connection.prepareStatement(statement);
            ResultSet rs = getMazeImage.executeQuery();
            int i = 0;
            while (rs.next()) {
                MazeDetails maze = new MazeDetails();
                maze.name = rs.getString("maze_name");
                Blob mazeImage = rs.getBlob("maze_image");
                maze.mazeImage = new String(mazeImage.getBytes(1, (int) mazeImage.length()));
                Blob mazeOptimalImage = rs.getBlob("maze_optimal_solution");
                if (mazeOptimalImage != null) {
                    maze.mazeSolution = new String(mazeOptimalImage.getBytes(1, (int) mazeOptimalImage.length()));
                }
                mazes[i] = maze;
                i++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return mazes;
    }
}

