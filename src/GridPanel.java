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
import java.util.Arrays;
import java.util.HashMap;

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
    private HashMap<java.util.List<Integer>,GridImage> GridImages = new HashMap<>();

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
    protected void ImagePlaceState(ImagePane pane) {
        imagePane = pane;
        State = GridState.IMAGEPLACE;
        allowGridWallSelection(false);
    }
    protected void SetRemoveImage() {
        State = GridState.REMOVEIMAGE;
        allowGridWallSelection(false);
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
                cellStateChange(cell);
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

    private void cellStateChange(Cell cell) {
        repaint();
        if(State == GridState.IMAGEPLACE) {
            ResetGridColors();
            if (cell.getModel().isRollover()) {
                System.out.println(cell.isDisabled);
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
                            if(GridComponentArray[i][cell.j].isIntersect &&
                                    GridComponentArray[i][cell.j].getBackground() == SELECTEDCOLOR){continue;}
                            GridComponentArray[i][cell.j].setBackground(ROLLOVERCOLOR);
                        }
                        for (int j = cell.j; j < (((imagePane.getImageCellWidth() * 2) - 1) + cell.j); j++) {
                            for (int k = 0; k < (imagePane.getImageCellHeight() * 2) - 1; k++) {

                                if(GridComponentArray[cell.i + k][j].isIntersect &&
                                        GridComponentArray[cell.i + k][j].getBackground() == SELECTEDCOLOR) {continue;}
                                GridComponentArray[cell.i + k][j].setBackground(ROLLOVERCOLOR);
                            }
                        }
                    }
                }
            }
            if(cell.getModel().isSelected()) {
                cell.getModel().setSelected(false);
                System.out.println("Selected");
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
                            if(GridComponentArray[i][cell.j].isWall) {
                                deselectWall((WallButton)GridComponentArray[i][cell.j]);
                            }
                            remove(GridComponentArray[i][cell.j]);
                            GridComponentArray[i][cell.j].isDisabled = true;
                        }
                        for (int j = cell.j + 1; j < (((imagePane.getImageCellWidth() * 2) - 1) + cell.j); j++) {
                            for (int k = 0; k < (imagePane.getImageCellHeight() * 2) - 1; k++) {
                                /*if (k == 0) {
                                    if (j < (((imagePane.getImageCellWidth() * 2) - 1) + cell.j) - 1) {
                                        remove(GridComponentArray[cell.i + k][j]);
                                        GridComponentArray[cell.i + k][j].isDisabled = true;
                                    }
                                } else { */
                                if(GridComponentArray[cell.i + k][j].isWall) {
                                    deselectWall((WallButton) GridComponentArray[cell.i + k][j]);
                                }
                                    remove(GridComponentArray[cell.i + k][j]);
                                    GridComponentArray[cell.i + k][j].isDisabled = true;
                                //}
                            }
                        }
                        cst.gridx = cell.x;
                        cst.gridy = cell.y;
                        cst.gridwidth = (imagePane.getImageCellWidth() * CELLWIDTH) + ((imagePane.getImageCellWidth() - 1) * WALLSHORT);
                        cst.gridheight = (imagePane.getImageCellHeight() * CELLWIDTH) + ((imagePane.getImageCellHeight() - 1) * WALLSHORT);
                        GridImage imgButton = new GridImage(cell.x,cell.y,cell.i, cell.j, imagePane.getImageCellWidth(), imagePane.getImageCellHeight());
                        imgButton.setBorderPainted(false);
                        imgButton.setBackground(GRIDCOLOR);
                        imgButton.setUI(new MetalToggleButtonUI() {
                            @Override
                            protected Color getSelectColor() {
                                return GRIDCOLOR;
                            }
                        });
                        imgButton.addChangeListener(new ChangeListener() {
                            @Override
                            public void stateChanged(ChangeEvent e) {
                                imagePanelStateChange(imgButton);
                            }
                        });
                        JLabel label = new JLabel(imagePane.getResizedImage());
                        imgButton.add(label);
                        imgButton.setBackground(GRIDCOLOR);
                        imgButton.setPreferredSize(new Dimension(
                                ((imagePane.getImageCellWidth() * CELLWIDTH) + ((imagePane.getImageCellWidth() - 1) * WALLSHORT)) * sizeMultiplier,
                                ((imagePane.getImageCellHeight() * CELLWIDTH) + ((imagePane.getImageCellHeight() - 1) * WALLSHORT)) * sizeMultiplier
                        ));
                        GridImages.put(Arrays.asList(cell.i,cell.j),imgButton);
                        add(GridImages.get(Arrays.asList(cell.i,cell.j)), cst);
                        revalidate();
                        repaint();
                        SetEdit(true);
                        //SetRemoveImage();
                        System.out.println(State);
                    }
                }

            }
        }
    }

    private void addGridComponentToPanel(int i, int j) {
        GridComponent temp = GridComponentArray[i][j];
        cst.gridx = temp.x;
        cst.gridy = temp.y;
        if(temp.isCell) {
            cst.gridheight = CELLWIDTH;
            cst.gridwidth = CELLWIDTH;
        }
        else if(temp.isWall) {
            if(((WallButton)temp).orientation == Orientation.HORIZONTAL) {
                cst.gridheight = WALLSHORT;
                cst.gridwidth = CELLWIDTH;
            }
            else {
                cst.gridheight = CELLWIDTH;
                cst.gridwidth = WALLSHORT;
            }
        }
        else {
            cst.gridheight = WALLSHORT;
            cst.gridwidth = WALLSHORT;
        }
        add(GridComponentArray[i][j], cst);
    }
    private void imagePanelStateChange(GridImage imgPane) {
        if(State == GridState.REMOVEIMAGE) {
            if(imgPane.getModel().isPressed()) {
                remove(GridImages.get(Arrays.asList(imgPane.i,imgPane.j)));
                for (int i = imgPane.i; i < (((imagePane.getImageCellHeight() * 2) - 1) + imgPane.i); i++) {
                    addGridComponentToPanel(i, imgPane.j);
                    GridComponentArray[i][imgPane.j].isDisabled = false;
                }
                for (int j = imgPane.j + 1; j < (((imagePane.getImageCellWidth() * 2) - 1) + imgPane.j); j++) {
                    for (int k = 0; k < (imagePane.getImageCellHeight() * 2) - 1; k++) {
                        /*if (k == 0) {
                            if (j < (((imagePane.getImageCellWidth() * 2) - 1) + imgPane.j) - 1) {
                                addGridComponentToPanel(imgPane.i + k, j);
                                GridComponentArray[imgPane.i + k][j].isDisabled = false;
                                if(GridComponentArray[imgPane.i + k][j].isCell) {
                                    GridMazeCellArray[(imgPane.i + k - 1) / 2][(j - 1) / 2].setDisabled(false);
                                }
                            }
                        } else { */
                            addGridComponentToPanel(imgPane.i + k, j);
                            GridComponentArray[imgPane.i + k][j].isDisabled = false;
                        //}
                    }
                }
                revalidate();
                repaint();
            }
        }
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

                        selectWall(btn);
                    } else {
                        deselectWall(btn);
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

    private void deselectWall(WallButton btn) {
        btn.setSelected(false);
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

    private void selectWall(WallButton btn) {
        btn.setSelected(true);
        if (btn.orientation == Orientation.HORIZONTAL) {
            GridComponentArray[btn.i][btn.j - 1].setBackground(SELECTEDCOLOR);
            GridComponentArray[btn.i][btn.j + 1].setBackground(SELECTEDCOLOR);
            MazeCellWallOn(btn.i, btn.j, Orientation.HORIZONTAL, true);

        } else {
            GridComponentArray[btn.i - 1][btn.j].setBackground(SELECTEDCOLOR);
            GridComponentArray[btn.i + 1][btn.j].setBackground(SELECTEDCOLOR);
            MazeCellWallOn(btn.i, btn.j, Orientation.VERTICAl, true);
        }
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
                return  GridComponentArray[i][j+1].getModel().isSelected() ||
                         GridComponentArray[i + 1][j].getModel().isSelected();


            } else if (j == GridComponentArray[0].length - 1) {
                return  GridComponentArray[i][j-1].getModel().isSelected() ||
                         GridComponentArray[i + 1][j].getModel().isSelected();
            } else {
                return  GridComponentArray[i][j-1].getModel().isSelected() ||
                         GridComponentArray[i][j+1].getModel().isSelected() ||
                         GridComponentArray[i+1][j].getModel().isSelected();
            }
        }else if(i == GridComponentArray.length - 1) {
            if (j == 0) {
                return GridComponentArray[i][j+1].getModel().isSelected() ||
                        GridComponentArray[i-1][j].getModel().isSelected();
            } else if (j == GridComponentArray[0].length - 1) {
                return GridComponentArray[i][j-1].getModel().isSelected() ||
                        GridComponentArray[i-1][j].getModel().isSelected();
            } else {
                return GridComponentArray[i][j-1].getModel().isSelected() ||
                        GridComponentArray[i][j+1].getModel().isSelected() ||
                        GridComponentArray[i-1][j].getModel().isSelected();
            }
        }else if(j == 0) {
            return GridComponentArray[i][j+1].getModel().isSelected() ||
                    GridComponentArray[i-1][j].getModel().isSelected() ||
                    GridComponentArray[i+1][j].getModel().isSelected();
        }else if(j == GridComponentArray[0].length - 1) {
            return GridComponentArray[i][j-1].getModel().isSelected() ||
                    GridComponentArray[i-1][j].getModel().isSelected() ||
                    GridComponentArray[i+1][j].getModel().isSelected();
        }
        return GridComponentArray[i][j-1].getModel().isSelected() ||
                GridComponentArray[i][j+1].getModel().isSelected() ||
                GridComponentArray[i-1][j].getModel().isSelected() ||
                GridComponentArray[i+1][j].getModel().isSelected();
    }

    public MazeCell[][] getGridMazeCellArray() {
        return GridMazeCellArray;
    }
}

