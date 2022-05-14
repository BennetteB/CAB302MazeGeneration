
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
                if (j == 0) {               // left case (Leaves left cell null and toggle left wall to true)
                    maze[i][j].setCellRight(maze[i][j+1]);
                    maze[i][j].toggleWallLeft();
                } else if (j == width-1) {  // right case (Leaves right cell null and toggles right wall to true)
                    maze[i][j].setCellLeft(maze[i][j-1]);
                    maze[i][j].toggleWallRight();
                } else {                    // other (Case for all cells that aren't on an x-axis edge)
                    maze[i][j].setCellLeft(maze[i][j-1]);
                    maze[i][j].setCellRight(maze[i][j+1]);
                }
                if (i == 0) {               // top case (Leaves top cell null and toggles top wall to true)
                    maze[i][j].setCellDown(maze[i+1][j]);
                    maze[i][j].toggleWallUp();
                } else if (i == height-1) { // bottom case (Leaves bottom cell null and toggles bottom wall to true)
                    maze[i][j].setCellUp(maze[i-1][j]);
                    maze[i][j].toggleWallDown();
                } else {                    // other (Case for all cells that aren't on a y-axis edge)
                    maze[i][j].setCellUp(maze[i-1][j]);
                    maze[i][j].setCellDown(maze[i+1][j]);
                }
            }
        }
        return maze;
    }

    /**
     * Returns the maze
     * @return Returns a two-dimensional array representation of the maze
     */
    public MazeCell[][] getMaze() {
        return this.maze;
    }
}
