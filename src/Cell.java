import javax.swing.*;

/**
 * Cell Class represents an empty space surrounded by walls
 */
public class Cell extends JPanel {
    private int x;
    private int y;
    private int width;
    private int i;
    private int j;

    public Cell(int x, int y, int width, int i, int j) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.i = i;
        this.j = j;
    }
}
