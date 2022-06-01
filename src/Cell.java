import javax.swing.*;

/**
 * Cell Class represents an empty space surrounded by walls
 */
public class Cell extends GridComponent {
    protected int width;

    protected Cell(int x, int y, int width, int i, int j) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.i = i;
        this.j = j;
        this.isCell = true;
    }
}
