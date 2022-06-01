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
    enum GridState {EDIT, NOEDIT, IMAGEPLACE, REMOVEIMAGE}
    private GridState State;
    private int sizeMultiplier = 5;
    private Color GRIDCOLOR = Color.WHITE;
    private Color ROLLOVERCOLOR = Color.RED;
    private Color SELECTEDCOLOR = Color.BLACK;
    enum Orientation {HORIZONTAL,VERTICAl}
    private final int CELLWIDTH = 10;
    private final int WALLSHORT = 2;
    private GridBagConstraints cst;
    private GridComponent[][] GridComponentArray;
    private MazeCell[][] GridMazeCellArray;

    private int HEIGHT;
    private int WIDTH;

    private ImagePane imagePane;

    protected GridPanel() {
        super();
        setLayout(new GridBagLayout());
        cst = new GridBagConstraints();
        cst.weightx = 0;
        cst.weighty = 0;
        cst.anchor = GridBagConstraints.CENTER;
        cst.fill = GridBagConstraints.NONE;
    }

    /**
     * Applies MazeData to Grid
     * @param mazeData
     */
    protected void CreateMaze(MazeCell[][] mazeData) {
        for (int i = 0; i < mazeData.length; i++) {
            for (int j = 0; j < mazeData[0].length; j++) {
                MazeCell mazeCell = mazeData[i][j];
                int gridCoordinatei = (i*2) +1;
                int gridCoordinatej = (j*2) +1;

                changeWall(gridCoordinatei - 1, gridCoordinatej, mazeCell.getWallUp());
                changeWall(gridCoordinatei, gridCoordinatej + 1, mazeCell.getWallRight());
                changeWall(gridCoordinatei + 1, gridCoordinatej, mazeCell.getWallDown());
                changeWall(gridCoordinatei, gridCoordinatej - 1, mazeCell.getWallLeft());
            }
        }
    }

    private void changeWall(int i, int j, boolean isSelected) {
        ((WallButton) GridComponentArray[i][j]).getModel().setSelected(isSelected);
    }

    private void emptyMaze() {
        for (int i = 0; i < GridComponentArray.length; i++) {
            for (int j = 0; j < GridComponentArray[0].length; j++){
                if(i % 2 == 0 && j % 2 != 0) {
                    ((WallButton) GridComponentArray[i][j]).getModel().setSelected(false);
                }
                else if(i % 2 != 0 && j % 2 == 0) {
                    ((WallButton) GridComponentArray[i][j]).getModel().setSelected(false);
                }
            }
        }
    }

    /**
     * Creates an Empty Grid.
     * @param width Number of cells wide
     * @param height Number of cells high
     */
    protected void CreateGrid(int width, int height) {
        State = GridState.EDIT;
        WIDTH = width;
        HEIGHT = height;
        GridComponentArray = new GridComponent[(height * 2) + 1][(width * 2) + 1];
        Maze maze = new Maze(height, width, false);
        GridMazeCellArray = maze.getMaze();
        //GridMazeCellArray = new MazeCell[height][width];
        int x = 0;
        int y = 0;
        for(int i = 0; i < (HEIGHT * 2) + 1; i++) {
            for(int j = 0; j < (WIDTH * 2) + 1; j++) {
                if(i % 2 == 0) {
                    if(j % 2 == 0) {
                        createIntersect(x,y,i,j);
                        x += WALLSHORT;
                    }
                    else {
                        createWall(x,y,Orientation.HORIZONTAL,i,j);
                        x += CELLWIDTH;
                    }
                }
                else{
                    if(j % 2 == 0) {
                        createWall(x,y,Orientation.VERTICAl,i,j);
                        x += WALLSHORT;
                    }
                    else {
                        createCell(x,y,i,j);
                        x += CELLWIDTH;
                        //GridMazeCellArray[(i - 1) / 2][(j - 1) / 2] = new MazeCell(false, false, false, false);
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

    protected void ToggleEdit() {
        if(State != GridState.NOEDIT) {
            State = GridState.NOEDIT;
            allowGridbuttonSelection(false);
        }
        else {
            State = GridState.EDIT;
            allowGridbuttonSelection(true);
        }
    }

    protected void SetEdit(boolean bool) {
        if(bool) {
            State = GridState.EDIT;
            allowGridbuttonSelection(true);
        }else {
            State = GridState.NOEDIT;
            allowGridbuttonSelection(false);
        }

    }

    private void allowGridWallSelection(boolean enable) {
        int x = 0;
        int y = 0;
        for(int i = 0; i < (HEIGHT * 2) + 1; i++) {
            for(int j = 0; j < (WIDTH * 2) + 1; j++) {
                if(i % 2 == 0) {
                    if(j % 2 == 0) {
                        // Intersect
                        x += WALLSHORT;
                    }
                    else {
                        // Horizontal Wall
                        GridComponentArray[i][j].getModel().setEnabled(enable);
                        x += CELLWIDTH;
                    }
                }
                else{
                    if(j % 2 == 0) {
                        // Vertical Wall
                        GridComponentArray[i][j].getModel().setEnabled(enable);
                        x += WALLSHORT;
                    }
                    else {
                        // Cell
                        x += CELLWIDTH;
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
    private void allowGridbuttonSelection(boolean enable) {
        int x = 0;
        int y = 0;
        for(int i = 0; i < (HEIGHT * 2) + 1; i++) {
            for(int j = 0; j < (WIDTH * 2) + 1; j++) {
                if(i % 2 == 0) {
                    if(j % 2 == 0) {
                        // Intersect
                        x += WALLSHORT;
                    }
                    else {
                        // Horizontal Wall
                        GridComponentArray[i][j].getModel().setEnabled(enable);
                        x += CELLWIDTH;
                    }
                }
                else{
                    if(j % 2 == 0) {
                        // Vertical Wall
                        GridComponentArray[i][j].getModel().setEnabled(enable);
                        x += WALLSHORT;
                    }
                    else {
                        // Cell
                        GridComponentArray[i][j].getModel().setEnabled(enable);
                        x += CELLWIDTH;
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

    protected void ImagePlaceState(ImagePane pane) {
        imagePane = pane;
        State = GridState.IMAGEPLACE;
        allowGridWallSelection(false);
    }

    /**
     * Creates empty panel and adds it to GridComponentArray and panel
     * @param x GridBagConstraints x position
     * @param y GridBagConstraints y position
     * @param i GridComponentArray x index
     * @param j GridComponentArray y index
     */
    private void createCell(int x, int y, int i, int j) {
        Cell cell = new Cell(x,y,CELLWIDTH,i,j);
        cell.setBackground(GRIDCOLOR);
        cell.setBorderPainted(false);
        cell.setBackground(GRIDCOLOR);
        cell.setRolloverEnabled(true);
        cell.setPreferredSize(new Dimension(CELLWIDTH * sizeMultiplier,CELLWIDTH * sizeMultiplier));
        cell.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return GRIDCOLOR;
            }
        });
        cell.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                repaint();
                if(State == GridState.IMAGEPLACE) {
                    ResetGridColors();
                    if(cell.getModel().isSelected()) {
                        cell.getModel().setSelected(false);
                    }
                    if (cell.getModel().isRollover()) {
                        if ((((imagePane.getImageCellWidth() * 2) - 1) + cell.j) > GridComponentArray[0].length - 1 ||
                                (((imagePane.getImageCellHeight() * 2) - 1) + cell.i) > GridComponentArray.length - 1) {
                        } else {
                            boolean DisabledComps = false;
                            for(int i = cell.i; i < (((imagePane.getImageCellHeight() * 2) ) + cell.i); i++) {
                                if (GridComponentArray[i][cell.j].isDisabled) {DisabledComps = true; break;}
                            }
                            for(int j = cell.j; j < (((imagePane.getImageCellWidth() * 2) - 1) + cell.j); j++) {
                                for(int k = 0; k < (imagePane.getImageCellHeight()*2) -1; k++) {
                                    if(GridComponentArray[cell.i + k][j].isDisabled) {DisabledComps = true; break;}
                                }
                            }
                            if(DisabledComps) {}
                            else {
                                for (int i = cell.i; i < (((imagePane.getImageCellHeight() * 2) - 1) + cell.i); i++) {
                                    GridComponentArray[i][cell.j].setBackground(ROLLOVERCOLOR);
                                }
                                for (int j = cell.j; j < (((imagePane.getImageCellWidth() * 2) - 1) + cell.j); j++) {
                                    for (int k = 0; k < (imagePane.getImageCellHeight() * 2) - 1; k++) {
                                        GridComponentArray[cell.i + k][j].setBackground(ROLLOVERCOLOR);
                                    }
                                }
                                repaint();
                            }
                        }
                    }
                    if(cell.getModel().isPressed()) {
                        if ((((imagePane.getImageCellWidth() * 2) - 1) + cell.j) > GridComponentArray[0].length - 1 ||
                                (((imagePane.getImageCellHeight() * 2) - 1) + cell.i) > GridComponentArray.length - 1) {
                        } else {
                            boolean DisabledComps = false;
                            for(int i = cell.i; i < (((imagePane.getImageCellHeight() * 2) ) + cell.i); i++) {
                                if (GridComponentArray[i][cell.j].isDisabled) {DisabledComps = true; break;}
                            }
                            for(int j = cell.j; j < (((imagePane.getImageCellWidth() * 2) - 1) + cell.j); j++) {
                                for(int k = 0; k < (imagePane.getImageCellHeight()*2) -1; k++) {
                                    if(GridComponentArray[cell.i + k][j].isDisabled) {DisabledComps = true; break;}
                                }
                            }
                            if(DisabledComps) {}
                            else {
                                cst.gridx = cell.x;
                                cst.gridy = cell.y;
                                cst.gridwidth = (imagePane.getImageCellWidth() * CELLWIDTH) + ((imagePane.getImageCellWidth() - 1) * WALLSHORT);
                                cst.gridheight = (imagePane.getImageCellHeight() * CELLWIDTH) + ((imagePane.getImageCellHeight() - 1) * WALLSHORT);
                                for (int i = cell.i; i < (((imagePane.getImageCellHeight() * 2) - 1) + cell.i); i++) {
                                    remove(GridComponentArray[i][cell.j]);
                                    GridComponentArray[i][cell.j].isDisabled = true;
                                    if(GridComponentArray[i][cell.j].isCell) {
                                        GridMazeCellArray[(i - 1) / 2][(cell.j - 1) / 2].setDisabled(true);
                                    }
                                }
                                for (int j = cell.j + 1; j < (((imagePane.getImageCellWidth() * 2) - 1) + cell.j); j++) {
                                    for (int k = 0; k < (imagePane.getImageCellHeight() * 2) - 1; k++) {
                                        if (k == 0) {
                                            if (j < (((imagePane.getImageCellWidth() * 2) - 1) + cell.j) - 1) {
                                                remove(GridComponentArray[cell.i + k][j]);
                                                GridComponentArray[cell.i + k][j].isDisabled = true;
                                                if(GridComponentArray[cell.i + k][j].isCell) {
                                                    GridMazeCellArray[(cell.i + k - 1) / 2][(j - 1) / 2].setDisabled(true);
                                                }
                                            }
                                        } else {
                                            remove(GridComponentArray[cell.i + k][j]);
                                            GridComponentArray[cell.i + k][j].isDisabled = true;
                                            if(GridComponentArray[cell.i + k][j].isCell) {
                                                GridMazeCellArray[(cell.i + k - 1) / 2][(j - 1) / 2].setDisabled(true);
                                            }
                                        }
                                    }
                                }
                                JPanel panel = new JPanel();
                                JLabel label = new JLabel(imagePane.getResizedImage());
                                panel.add(label);
                                panel.setBackground(GRIDCOLOR);
                                panel.setPreferredSize(new Dimension(
                                        ((imagePane.getImageCellWidth() * CELLWIDTH) + ((imagePane.getImageCellWidth() - 1) * WALLSHORT)) * sizeMultiplier,
                                        ((imagePane.getImageCellHeight() * CELLWIDTH) + ((imagePane.getImageCellHeight() - 1) * WALLSHORT)) * sizeMultiplier
                                ));
                                add(panel, cst);
                                revalidate();
                                repaint();
                                SetEdit(true);
                                System.out.println(State);
                            }
                        }

                    }
                }
                else {
                    if(cell.getModel().isSelected()) {
                        cell.getModel().setSelected(false);
                    }
                }
            }
        });
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
        Intersect isc = new Intersect(x,y,WALLSHORT,i,j);
        isc.setBackground(GRIDCOLOR);
        isc.setBackground(GRIDCOLOR);
        isc.setBorderPainted(false);
        isc.setBackground(GRIDCOLOR);
        isc.setRolloverEnabled(false);
        isc.setEnabled(false);
        isc.setPreferredSize(new Dimension(WALLSHORT * sizeMultiplier,WALLSHORT * sizeMultiplier));
        isc.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return GRIDCOLOR;
            }
        });
        isc.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(isc.getModel().isSelected()){
                    isc.setSelected(false);
                }
            }
        });
        GridComponentArray[i][j] = isc;
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
                if(State == GridState.EDIT) {
                    if (btn.getModel().isRollover()) {
                        btn.setBackground(ROLLOVERCOLOR);
                    } else {
                        btn.setBackground(GRIDCOLOR);
                    }
                    if (btn.getModel().isSelected()) {

                        if (btn.orientation == Orientation.HORIZONTAL) {
                            GridComponentArray[btn.i][btn.j - 1].setBackground(SELECTEDCOLOR);
                            GridComponentArray[btn.i][btn.j + 1].setBackground(SELECTEDCOLOR);
                            MazeCellWallOn(btn.i, btn.j, Orientation.HORIZONTAL, true);

                        } else {
                            GridComponentArray[btn.i - 1][btn.j].setBackground(SELECTEDCOLOR);
                            GridComponentArray[btn.i + 1][btn.j].setBackground(SELECTEDCOLOR);
                            MazeCellWallOn(btn.i, btn.j, Orientation.VERTICAl, true);
                        }
                    } else {
                        if (btn.orientation == Orientation.HORIZONTAL) {
                            if (!isAdjacentToWall(btn.i, btn.j - 1)) {
                                GridComponentArray[btn.i][btn.j - 1].setBackground(GRIDCOLOR);
                            }
                            if (!isAdjacentToWall(btn.i, btn.j + 1)) {
                                GridComponentArray[btn.i][btn.j + 1].setBackground(GRIDCOLOR);
                            }
                            MazeCellWallOn(btn.i, btn.j, Orientation.HORIZONTAL, false);
                        } else {
                            if (!isAdjacentToWall(btn.i - 1, btn.j)) {
                                GridComponentArray[btn.i - 1][btn.j].setBackground(GRIDCOLOR);
                            }
                            if (!isAdjacentToWall(btn.i + 1, btn.j)) {
                                GridComponentArray[btn.i + 1][btn.j].setBackground(GRIDCOLOR);
                            }
                            MazeCellWallOn(btn.i, btn.j, Orientation.VERTICAl, false);
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

    private void ResetGridColors() {
        int x = 0;
        int y = 0;
        for(int i = 0; i < (HEIGHT * 2) + 1; i++) {
            for(int j = 0; j < (WIDTH * 2) + 1; j++) {
                if(i % 2 == 0) {
                    if(j % 2 == 0) {
                        // Intersect
                        if(GridComponentArray[i][j].getBackground() == ROLLOVERCOLOR) {
                            GridComponentArray[i][j].setBackground(GRIDCOLOR);
                        }
                        x += WALLSHORT;
                    }
                    else {
                        // Horizontal Wall
                        GridComponentArray[i][j].setBackground(GRIDCOLOR);
                        x += CELLWIDTH;
                    }
                }
                else{
                    if(j % 2 == 0) {
                        // Vertical Wall
                        GridComponentArray[i][j].setBackground(GRIDCOLOR);
                        x += WALLSHORT;
                    }
                    else {
                        // Cell
                        GridComponentArray[i][j].setBackground(GRIDCOLOR);
                        x += CELLWIDTH;
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
        repaint();
    }
    private void MazeCellWallOn(int i, int j, Orientation orien, boolean isOn) {
        int x = 0;
        int y = 0;
        if(orien == Orientation.HORIZONTAL) {
            if(i == 0) {
                x = (j - 1) / 2;
                if(GridMazeCellArray[i][x].getWallUp() != isOn) {
                    GridMazeCellArray[i][x].toggleWallUp();
                }
            }
            else if (i == GridComponentArray.length - 1) {
                x = (j - 1) / 2;
                y = (i / 2) - 1;
                if(GridMazeCellArray[y][x].getWallDown() != isOn) {
                    GridMazeCellArray[y][x].toggleWallDown();
                }
            }
            else {
                x = (j - 1) / 2;
                y = (i  / 2)  - 1;
                if(GridMazeCellArray[y][x].getWallDown() != isOn) {
                    GridMazeCellArray[y][x].toggleWallDown();
                }
                if(GridMazeCellArray[y + 1][x].getWallUp() != isOn) {
                    GridMazeCellArray[y + 1][x].toggleWallUp();
                }
            }
        }
        else {
            if(j == 0) {
                y = (i - 1) / 2;
                if(GridMazeCellArray[y][j].getWallLeft() != isOn) {
                    GridMazeCellArray[y][j].toggleWallLeft();
                }
            }
            else if(j == GridComponentArray[0].length - 1) {
                x = (j / 2) - 1;
                y = (i - 1) / 2;
                if(GridMazeCellArray[y][x].getWallRight() != isOn) {
                    GridMazeCellArray[y][x].toggleWallRight();
                }
            }
            else {
                x = (j / 2) - 1;
                y = (i - 1) / 2;
                if(GridMazeCellArray[y][x].getWallRight() != isOn) {
                    GridMazeCellArray[y][x].toggleWallRight();
                }
                if(GridMazeCellArray[y][x + 1].getWallLeft() != isOn) {
                    GridMazeCellArray[y][x + 1].toggleWallLeft();
                }
            }
        }

    }

    /**
     * Checks if Intersect at provided coordinates has a selected wall next to it
     * @param i
     * @param j
     * @return
     */
    private boolean isAdjacentToWall(int i, int j) {
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

    public MazeCell[][] getGridMazeCellArray() {
        return GridMazeCellArray;
    }
}

