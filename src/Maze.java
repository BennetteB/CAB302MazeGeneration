
/**
 * Class representing a maze
 */
public class Maze {
    /** The cell height of the maze **/
    private int height;

    /** The cell width of the maze **/
    private int width;

    /** An array of MazeCells that represent the maze **/
    private MazeCell[][] maze;

    /**
     * Initializes the maze
     * @param height The cell height of the maze
     * @param width The cell width of the maze
     */
    protected Maze(int height, int width) {
        this.height = height;
        this.width = width;
        this.maze = generateMaze();
    }

    /**
     * Generates a blank maze with a cell width of width and a cell height of height
     * @return Returns a blank maze
     */
    private MazeCell[][] generateMaze() {
        MazeCell[][] maze = new MazeCell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = new MazeCell(null, null, null, null);
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (j == 0) {               // left case
                    maze[i][j].right = maze[i][j+1];
                } else if (j == width-1) {  // right case
                    maze[i][j].left = maze[i][j-1];
                } else {                    // other
                    maze[i][j].left = maze[i][j-1];
                    maze[i][j].right = maze[i][j+1];
                }
                if (i == 0) {               // top case
                    maze[i][j].down = maze[i+1][j];
                } else if (i == height-1) { // bottom case
                    maze[i][j].up = maze[i-1][j];
                } else {                    // other
                    maze[i][j].up = maze[i - 1][j];
                    maze[i][j].down = maze[i + 1][j];
                }
            }
        }
        return maze;
    }

    /**
     * Returns the maze
     * @return Returns the maze
     */
    public MazeCell[][] getMaze() {
        return this.maze;
    }
}
