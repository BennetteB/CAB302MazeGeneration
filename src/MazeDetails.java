import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MazeDetails {
    public int id;
    public String author;
    public String name;
    public Timestamp created;
    public Timestamp lastEdited;
    public String mazeData;
    public String imageData;
    public int width;
    public int height;
    public String mazeImage;
    public String mazeSolution;
    public List<ImagePane> allImages;
    public int imageWidth;
    public int imageHeight;
    private List<MazeDetails> allMazeDetails;

    /**
     * Default constructor
     */
    public MazeDetails(){
        this.allImages = new ArrayList<>();
        this.allMazeDetails = new ArrayList<>();
    }

    /**
     * Constructor for MazeDetails
     * @param author maze author
     * @param name maze name
     * @param mazeData binary data of the maze in the gridpanel
     * @param imageData binary data of all image details
     * @param width maze cell width
     * @param height maze cell height
     * @param mazeImage binary data of all maze image
     * @param mazeSolution binary data of maze optimal solution image
     */
    public MazeDetails(String author, String name, String mazeData, String imageData,
                       int width, int height, String mazeImage, String mazeSolution) {
        this.author = author;
        this.name = name;
        this.mazeData = mazeData;
        this.imageData = imageData;
        this.width = width;
        this.height = height;
        this.mazeImage = mazeImage;
        this.mazeSolution = mazeSolution;
        this.allMazeDetails = new ArrayList<>();
        this.allImages = new ArrayList<>();
    }

    /**
     * sets the MazeDetails list
     * @param mazes the list of MazeDetails to be set
     */
    public void setList(List<MazeDetails> mazes) {
        allMazeDetails = mazes;
    }

    /**
     * gets the MazeDetails list
     * @return list of MazeDetails
     */
    public List<MazeDetails> getList() {
       return allMazeDetails;
    }
}
