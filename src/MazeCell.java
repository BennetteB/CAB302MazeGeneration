public class MazeCell {

    /** The MazeCell above this cell */
    protected MazeCell up;

    /** The MazeCell below this cell*/
    protected MazeCell down;

    /** The MazeCell to the left of this cell */
    protected MazeCell left;

    /** The MazeCell to the right of this cell */
    protected MazeCell right;

    /** Initializes the MazeCell */
    protected MazeCell(MazeCell up, MazeCell down, MazeCell left, MazeCell right) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }
}
