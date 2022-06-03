import javax.swing.*;

public class GridImage extends JToggleButton {
    protected int x;
    protected int y;
    protected int i;
    protected int j;
    protected ImageIcon image;

    protected int WIDTH;
    protected int HEIGHT;
    public GridImage(int x, int y, int i, int j, int WIDTH, int HEIGHT, ImageIcon img) {
        this.x = x;
        this.y = y;
        this.i = i;
        this.j = j;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.image = img;
    }


}
