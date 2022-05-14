public class MazeCell {

    protected MazeCell up;
    protected MazeCell down;
    protected MazeCell left;
    protected MazeCell right;

    protected MazeCell(MazeCell up, MazeCell down, MazeCell left, MazeCell right) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }
}
