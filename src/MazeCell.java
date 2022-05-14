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
    private boolean wallUp = false;

    /** State of the wall below the cell */
    private boolean wallDown = false;

    /** State of the wall to the left of the cell */
    private boolean wallLeft = false;

    /** State of the wall to the right of the cell */
    private boolean wallRight = false;

    /** Initializes the MazeCell */
    protected MazeCell(MazeCell up, MazeCell down, MazeCell left, MazeCell right) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
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
}
