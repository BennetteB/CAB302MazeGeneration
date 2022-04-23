import javax.swing.*;
import java.awt.*;

/**
 * Creates the Grid Panel with editable cells for Maze construction
 */
public class GridPanel extends JPanel {
    enum Position {HORIZONTAL,VERTICAl}
    private GridBagConstraints cst;
    private int mazeWidth;
    private int mazeHeight;

    public GridPanel() {
        super();
        setLayout(new GridBagLayout());
        cst = new GridBagConstraints();
    }

    /**
     * Creates an Empty Grid
     * @param width Number of cells wide
     * @param height Number of cells high
     */
    protected void CreateGrid(int width, int height) {

    }

    /**
     * Creates Empty JPanel Cell
     * @return JPanel
     */
    private JPanel createCell() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(10,10));
        return panel;
    }

    /**
     * Creates Vertical or Horizontal JButton Walls
     * @param position Vertical or Horizontal decides dimensions of button
     * @return JButton
     */
    private JButton createWall(Position position) {
        JButton btn = new JButton();
        switch (position) {
            case HORIZONTAL:
                btn.setPreferredSize(new Dimension(10,2));
                break;
            case VERTICAl:
                btn.setPreferredSize(new Dimension(2,10));
                break;
        }
        return btn;
    }
}
