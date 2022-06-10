import javax.swing.*;

/**
 * Class representing Wall
 */
public class WallButton extends GridComponent {
    protected int width;
    protected int height;
    protected GridPanel.Orientation orientation;


    /**
     * JToggle Button with embedded information about button properties and location
     * @param x GridBagConstraints x position
     * @param y GridBagConstraints y position
     * @param width GridBagConstraints width
     * @param height GridBagConstraints height
     * @param i GridComponentArray x index
     * @param j GridComponentArray y index
     */
    protected WallButton(int x, int y, int width, int height, int i, int j) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.i = i;
        this.j = j;
        if(width < height) {
            orientation = GridPanel.Orientation.VERTICAl;
        }
        else {
            orientation = GridPanel.Orientation.HORIZONTAL;
        }
        this.isWall = true;
    }
}
