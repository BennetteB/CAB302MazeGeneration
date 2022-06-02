import javax.swing.*;

public class GridImage extends JButton {
    protected int x;
    protected int y;
    protected int i;
    protected int j;

    protected int WIDTH;
    protected int HEIGHT;
    public GridImage(int x, int y, int i, int j, int WIDTH, int HEIGHT) {
        this.x = x;
        this.y = y;
        this.i = i;
        this.j = j;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }


}
