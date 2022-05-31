public class MazeCell {

    /** The MazeCell above this cell */
    private MazeCell up;

    /** The MazeCell below this cell*/
    private MazeCell down;

    /** The MazeCell to the left of this cell */
    private MazeCell left;

    /** The MazeCell to the right of this cell */
    private MazeCell right;

    /** State of the wall above the cell */
    private boolean wallUp;

    /** State of the wall below the cell */
    private boolean wallDown;

    /** State of the wall to the left of the cell */
    private boolean wallLeft;

    /** State of the wall to the right of the cell */
    private boolean wallRight;

    /** The visited state of the cell */
    private boolean visited = false;

    /** The parent cell of this cell*/
    private MazeCell parent;

    /**
     * Creates a MazeCell
     * @param up the MazeCell above this cell
     * @param down the MazeCell below this cell
     * @param left the MazeCell to the left of this cell
     * @param right The MazeCell to right of this cell
     * @param wallState The initial state of all four walls
     */
    protected MazeCell(MazeCell up, MazeCell down, MazeCell left, MazeCell right, boolean wallState) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.wallUp = wallState;
        this.wallDown = wallState;
        this.wallLeft = wallState;
        this.wallRight = wallState;
    }

    /**
     * Sets the cell above this cell
     * @param cell MazeCell
     */
    public void setCellUp(MazeCell cell) {up = cell;}

    /**
     * Sets the cell below this cell
     * @param cell MazeCell
     */
    public void setCellDown(MazeCell cell) {down = cell;}

    /**
     * Sets the cell to the left of this cell
     * @param cell MazeCell
     */
    public void setCellLeft(MazeCell cell) {left = cell;}

    /**
     * Sets the cell to the right of this cell
     * @param cell MazeCell
     */
    public void setCellRight(MazeCell cell) {right = cell;}

    /**
     * Get the cell above this cell
     * @return the cell above this cell
     */
    public MazeCell getCellUp() {return up;}

    /**
     * Get the cell below this cell
     * @return the cell below this cell
     */
    public MazeCell getCellDown() {return down;}

    /**
     * Get the cell to the left of this cell
     * @return the cell to the left of this cell
     */
    public MazeCell getCellLeft() {return left;}

    /**
     * Get the cell to the right of this cell
     * @return the cell to the right of this cell
     */
    public MazeCell getCellRight() {return right;}

    /**
     * Toggles the wall above the cell
     */
    public void toggleWallUp() {wallUp = !wallUp;}

    /**
     * Toggles the wall below the cell
     */
    public void toggleWallDown() {wallDown = !wallDown;}

    /**
     * Toggles the wall to the left of the cell
     */
    public void toggleWallLeft() {wallLeft = !wallLeft;}

    /**
     * Toggles the wall to the right of the cell
     */
    public void toggleWallRight() {wallRight = !wallRight;}

    /**
     * Get the state of the wall above the cell
     * @return the state of the wall above the cell
     */
    public boolean getWallUp() {return wallUp;}

    /**
     * Get the state of the wall below the cell
     * @return the state of the wall below the cell
     */
    public boolean getWallDown() {return wallDown;}

    /**
     * Get the state of the wall to the left of the cell
     * @return the state of the wall to the left of the cell
     */
    public boolean getWallLeft() {return wallLeft;}

    /**
     * Get the state of the wall to the right of the cell
     * @return the state of the wall to the right of the cell
     */
    public boolean getWallRight() {return wallRight;}

    /**
     * Sets the visited state of the cell
     */
    public void setVisited(boolean bool) {visited = bool;}

    /**
     * Get the toggle state of the cell
     * @return the toggle state of the cell
     */
    public boolean getVisited() {return visited;}

    /**
     * Get the parent cell of this cell
     * @return the parent cell of this cell
     */
    public MazeCell getParent() {return parent;}

    /**
     * Set the parent cell of this cell
     * @param cell the parent cell of this cell
     */
    public void setParent(MazeCell cell) {
        this.parent = cell;
    }
}
