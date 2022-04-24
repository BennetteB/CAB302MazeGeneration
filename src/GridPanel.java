import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import javax.swing.plaf.metal.MetalButtonUI;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Creates the Grid Panel with editable cells for Maze construction
 */
public class GridPanel extends JPanel {
    private int sizeMultiplier = 5;
    private Color GRIDCOLOR = Color.WHITE;
    private Color ROLLOVERCOLOR = Color.RED;
    private Color SELECTEDCOLOR = Color.BLACK;
    enum Orientation {HORIZONTAL,VERTICAl}
    private final int CELLWIDTH = 10;
    private final int WALLSHORT = 2;
    private GridBagConstraints cst;
    private Component[][] GridComponentArray;

    public GridPanel() {
        super();
        setLayout(new GridBagLayout());
        cst = new GridBagConstraints();
        cst.weightx = 0;
        cst.weighty = 0;
        cst.anchor = GridBagConstraints.CENTER;
        cst.fill = GridBagConstraints.NONE;
    }

    /**
     * Creates an Empty Grid
     * @param width Number of cells wide
     * @param height Number of cells high
     */
    protected void CreateGrid(int width, int height) {
        GridComponentArray = new Component[(height * 2) + 1][(width * 2) + 1];
        int x = 0;
        int y = 0;
        for(int i = 0; i < (height * 2) + 1; i++) {
            for(int j = 0; j < (width * 2) + 1; j++) {
                if(i % 2 == 0) {
                    if(j % 2 == 0) {
                        createIntersect(x,y,i,j);
                        x += 2;
                    }
                    else {
                        createWall(x,y,Orientation.HORIZONTAL,i,j);
                        x += 10;
                    }
                }
                else{
                    if(j % 2 == 0) {
                        createWall(x,y,Orientation.VERTICAl,i,j);
                        x += 2;
                    }
                    else {
                        createCell(x,y,i,j);
                        x += 10;
                    }
                }
            }
            if(i % 2 == 0) {
                y += 2;
            }
            else {
                y += 10;
            }
            x = 0;
        }
    }

    /**
     * Creates empty panel and adds it to GridComponentArray and  panel
     * @param x GridBagConstraints x position
     * @param y GridBagConstraints y position
     * @param i GridComponentArray x index
     * @param j GridComponentArray y index
     */
    private void createCell(int x, int y, int i, int j) {
        Cell cell = new Cell(x,y,CELLWIDTH,i,j);
        cell.setBackground(GRIDCOLOR);
        cell.setPreferredSize(new Dimension(CELLWIDTH * sizeMultiplier,CELLWIDTH * sizeMultiplier));
        GridComponentArray[i][j] = cell;
        GridBagConstraints cst = new GridBagConstraints();
        cst.gridx = x;
        cst.gridy = y;
        cst.gridwidth = CELLWIDTH;
        cst.gridheight = CELLWIDTH;
        add(GridComponentArray[i][j],cst);
    }

    /**
     * Creates small empty panel and adds it to GridComponentArray and panel
     * @param x GridBagConstraints x position
     * @param y GridBagConstraints y position
     * @param i GridComponentArray x index
     * @param j GridComponentArray y index
     */
    private void createIntersect(int x, int y, int i, int j) {
        Cell cell = new Cell(x,y,WALLSHORT,i,j);
        cell.setBackground(GRIDCOLOR);
        cell.setPreferredSize(new Dimension(WALLSHORT * sizeMultiplier,WALLSHORT * sizeMultiplier));
        GridComponentArray[i][j] = cell;
        GridBagConstraints cst = new GridBagConstraints();
        cst.gridx = x;
        cst.gridy = y;
        cst.gridwidth = WALLSHORT;
        cst.gridheight = WALLSHORT;
        add(GridComponentArray[i][j],cst);
    }

    /**
     * Creates WallButton and adds it to GridComponentArray and panel, also assigns changeListener to Wallbutton for wall selection functionality
     * @param x GridBagConstraints x position
     * @param y GridBagConstraints y position
     * @param orien The Orientation of the WallButton
     * @param i GridComponentArray row
     * @param j GridComponentArray column
     */
    private void createWall(int x, int y, Orientation orien, int i, int j) {
        int width;
        int height;
        if(orien == Orientation.HORIZONTAL) {
            width = CELLWIDTH;
            height = WALLSHORT;
        }else {
            width = WALLSHORT;
            height = CELLWIDTH;
        }
        WallButton btn = new WallButton(x,y,width,height,i,j);
        btn.setBorderPainted(false);
        btn.setBackground(GRIDCOLOR);
        btn.setRolloverEnabled(true);
        btn.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return SELECTEDCOLOR;
            }
        });
        btn.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(btn.getModel().isRollover()) {
                    btn.setBackground(ROLLOVERCOLOR);
                }
                else{
                    btn.setBackground(GRIDCOLOR);
                }
                if(btn.getModel().isSelected()) {

                    if(btn.orientation == Orientation.HORIZONTAL) {
                        GridComponentArray[btn.i][btn.j-1].setBackground(SELECTEDCOLOR);
                        GridComponentArray[btn.i][btn.j+1].setBackground(SELECTEDCOLOR);
                    }
                    else {
                        GridComponentArray[btn.i-1][btn.j].setBackground(SELECTEDCOLOR);
                        GridComponentArray[btn.i+1][btn.j].setBackground(SELECTEDCOLOR);
                    }
                }
                else {
                    if(btn.orientation == Orientation.HORIZONTAL) {
                        if(!isAdjacentToWall(btn.i,btn.j - 1)) {
                            GridComponentArray[btn.i][btn.j - 1].setBackground(GRIDCOLOR);
                        }
                        if(!isAdjacentToWall(btn.i,btn.j + 1)) {
                            GridComponentArray[btn.i][btn.j + 1].setBackground(GRIDCOLOR);
                        }
                    }
                    else {
                        if(!isAdjacentToWall(btn.i - 1,btn.j)) {
                            GridComponentArray[btn.i - 1][btn.j].setBackground(GRIDCOLOR);
                        }
                        if(!isAdjacentToWall(btn.i + 1,btn.j)) {
                            GridComponentArray[btn.i + 1][btn.j].setBackground(GRIDCOLOR);
                        }
                    }
                }

            }
        });
        btn.setPreferredSize(new Dimension(width * sizeMultiplier, height * sizeMultiplier));
        GridComponentArray[i][j] = btn;
        cst.gridx = x;
        cst.gridy = y;
        cst.gridwidth = width;
        cst.gridheight = height;
        add(GridComponentArray[i][j],cst);
    }

    /**
     * Checks if Intersect at provided coordinates has a selected wall next to it
     * @param i
     * @param j
     * @return
     */
    private boolean isAdjacentToWall(int i, int j) {
        int size =  GridComponentArray[0].length;
        if(i == 0) {
            if (j == 0) {
                return ((WallButton) GridComponentArray[i][j+1]).getModel().isSelected() ||
                        ((WallButton) GridComponentArray[i + 1][j]).getModel().isSelected();
            } else if (j == GridComponentArray[0].length - 1) {
                return ((WallButton) GridComponentArray[i][j-1]).getModel().isSelected() ||
                        ((WallButton) GridComponentArray[i + 1][j]).getModel().isSelected();
            } else {
                return ((WallButton) GridComponentArray[i][j-1]).getModel().isSelected() ||
                        ((WallButton) GridComponentArray[i][j+1]).getModel().isSelected() ||
                        ((WallButton) GridComponentArray[i+1][j]).getModel().isSelected();
            }
        }else if(i == GridComponentArray.length - 1) {
            if (j == 0) {
                return ((WallButton) GridComponentArray[i][j+1]).getModel().isSelected() ||
                        ((WallButton) GridComponentArray[i-1][j]).getModel().isSelected();
            } else if (j == GridComponentArray[0].length - 1) {
                return ((WallButton) GridComponentArray[i][j-1]).getModel().isSelected() ||
                        ((WallButton) GridComponentArray[i-1][j]).getModel().isSelected();
            } else {
                return ((WallButton) GridComponentArray[i][j-1]).getModel().isSelected() ||
                        ((WallButton) GridComponentArray[i][j+1]).getModel().isSelected() ||
                        ((WallButton) GridComponentArray[i-1][j]).getModel().isSelected();
            }
        }else if(j == 0) {
            return ((WallButton) GridComponentArray[i][j+1]).getModel().isSelected() ||
                    ((WallButton) GridComponentArray[i-1][j]).getModel().isSelected() ||
                    ((WallButton) GridComponentArray[i+1][j]).getModel().isSelected();
        }else if(j == GridComponentArray[0].length - 1) {
            return ((WallButton) GridComponentArray[i][j-1]).getModel().isSelected() ||
                    ((WallButton) GridComponentArray[i-1][j]).getModel().isSelected() ||
                    ((WallButton) GridComponentArray[i+1][j]).getModel().isSelected();
        }
        return ((WallButton) GridComponentArray[i][j-1]).getModel().isSelected() ||
                ((WallButton) GridComponentArray[i][j+1]).getModel().isSelected() ||
                ((WallButton) GridComponentArray[i-1][j]).getModel().isSelected() ||
                ((WallButton) GridComponentArray[i+1][j]).getModel().isSelected();
    }
}

