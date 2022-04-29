import javax.swing.*;

/**
 * Cell Class represents an empty space surrounded by walls
 */
public class Cell extends JPanel {
    protected int x;
    protected int y;
    protected int width;
    protected int i;
    protected int j;

    protected Cell(int x, int y, int width, int i, int j) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.i = i;
        this.j = j;
    }
}
