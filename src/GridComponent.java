import javax.swing.*;

/**
 * Class to be inherited by Intersect, Cell, WallButton
 */
public class GridComponent extends JToggleButton {
    protected boolean isDisabled = false;
    protected boolean isCell = false;
    protected boolean isWall = false;
    protected boolean isIntersect = false;
    protected int x;
    protected int y;
    protected int i;
    protected int j;
}
