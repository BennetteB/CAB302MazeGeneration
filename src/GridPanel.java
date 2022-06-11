import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    private Color SOLUTIONCOLOR = Color.GREEN;
    enum Orientation {HORIZONTAL,VERTICAl}
    private final int CELLWIDTH = 10;
    private final int WALLSHORT = 2;
    private GridBagConstraints cst;
    private GridComponent[][] GridComponentArray;
    private MazeCell[][] GridMazeCellArray;
    private HashMap<java.util.List<Integer>,GridImage> GridImages = new HashMap<>();

    private int HEIGHT;
    private int WIDTH;
    private boolean showSolutionLine = false;
    protected void ShowSolutionLine(boolean bool) {showSolutionLine = bool;}

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
     * Return if State is IMAGEPLACE
     * @return Boolean representing if State is IMAGEPLACE
     */
    protected boolean IsPlaceImageState() {
        return State == GridState.IMAGEPLACE;
    }

    /**
     * Sets State to EDIT if true or NOEDIT if false
     * @param bool set to Edit if true or NOEDIT if false
     */
    protected void SetEditState(boolean bool) {
        if(bool) {
            State = GridState.EDIT;
            allowGridWallSelection(true);
            allowGridCellSelection(false);
        }else {
            State = GridState.NOEDIT;
            allowGridWallSelection(false);
            allowGridCellSelection(false);
        }

    }

    /**
     * Sets current ImagePane and State to IMAGEPLACE
     * @param pane ImagePace to be placed
     */
    protected void SetImagePlaceState(ImagePane pane) {
        imagePane = pane;
        State = GridState.IMAGEPLACE;
        allowGridWallSelection(false);
        allowGridCellSelection(true);
    }

    /**
     * Sets State to REMOVEIMAGE
     */
    protected void SetRemoveImageState() {
        State = GridState.REMOVEIMAGE;
        allowGridWallSelection(false);
        allowGridCellSelection(false);
    }

    /**
     * Returns the GridImages placed on the grid
     * @return HashMap with key of grid location and value of GridImage
     */
    protected HashMap<List<Integer>, GridImage> GetImageMap() {
        return GridImages;
    }

    /**
     * Applies MazeData to Grid
     * @param mazeData MazeData to be applied to Grid
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

    /**
     * Applied mazeData to Grid and places GridImages on Grid
     * @param mazeData MazeData to be applied to Grid
     * @param imgs Array of GridImages
     */
    protected void CreateMaze(MazeCell[][] mazeData, GridImage[] imgs) {
        CreateMaze(mazeData);
        for (GridImage img :imgs) {
            imagePane = new ImagePane(img.image,img.WIDTH, img.HEIGHT);
            PlaceImage((Cell)GridComponentArray[img.i][img.j]);
        }
    }

    /**
     * Changes Selection of wall
     * @param i row of wall
     * @param j column of wall
     * @param isSelected if wall is to be selected or not
     */
    private void changeWall(int i, int j, boolean isSelected) {
        GridComponentArray[i][j].getModel().setSelected(isSelected);
    }


    /**
     * Creates an Empty Grid.
     * @param width Number of cells wide
     * @param height Number of cells high
     */
    protected void CreateGrid(int width, int height) {
        SetEditState(true);
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

    /**
     * Shows solution line on grid
     * @param data MazeData which indicated which cells are part of solution
     */
    protected void ShowSolutionLine(MazeCell[][] data) {
        ShowSolutionLine(true);
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if(data[i][j].getSolutionCell()) {
                    int gridCoordinatei = (i*2) +1;
                    int gridCoordinatej = (j*2) +1;

                    GridComponentArray[gridCoordinatei][gridCoordinatej].setBackground(SOLUTIONCOLOR);
                }
            }
        }
        repaint();
    }


    /**
     * Allows selection of walls in grid
     * @param enable boolean true to enable false to disable
     */
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

    /**
     * Allows selection of cells in grid
     * @param enable boolean true to enable false to disable
     */
    private void allowGridCellSelection(boolean enable) {
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
                        x += CELLWIDTH;
                    }
                }
                else{
                    if(j % 2 == 0) {
                        // Vertical Wall
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

    /**
     * Call when state of supplied cell is changed
     * @param cell Cell whose state has changed
     */
    private void cellStateChange(Cell cell) {
        repaint();
        if(State == GridState.IMAGEPLACE) {
            ResetGridColors();
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
                        PlaceImage(cell);
                    }
                }

            }
        }
    }

    /**
     * Replaces cells with ImagePane image on grid
     * @param cell the top-right cell where Image panel starts
     */
    private void PlaceImage(Cell cell) {
        for (int j = cell.j; j < (((imagePane.getImageCellWidth() * 2) - 1) + cell.j); j++) {
            for (int k = 0; k < (imagePane.getImageCellHeight() * 2) - 1; k++) {
                if(GridComponentArray[cell.i + k][j].isWall) {
                    deselectWall((WallButton) GridComponentArray[cell.i + k][j]);
                }
                    remove(GridComponentArray[cell.i + k][j]);
                    GridComponentArray[cell.i + k][j].isDisabled = true;
                    if(j == cell.j) {
                        if(GridComponentArray[cell.i + k][cell.j - 1].isWall) {
                            selectWall((WallButton) GridComponentArray[cell.i + k][cell.j - 1]);
                        }
                        if(k == 0) {
                            if(GridComponentArray[cell.i - 1][j - 1].isWall) {
                                selectWall((WallButton) GridComponentArray[cell.i - 1][j - 1]);
                            }
                        }
                        if(k == (imagePane.getImageCellHeight() * 2) - 2) {
                            int ktemp = (imagePane.getImageCellHeight() * 2) - 1;
                            if(GridComponentArray[cell.i + ktemp][j - 1].isWall) {
                                selectWall((WallButton) GridComponentArray[cell.i + ktemp][j - 1]);
                            }
                        }
                    }
                    if(j == ((imagePane.getImageCellWidth() * 2) - 1) + cell.j - 1) {
                        int jtemp = ((imagePane.getImageCellWidth() * 2) - 1) + cell.j;
                        if(GridComponentArray[cell.i + k][jtemp].isWall) {
                            selectWall((WallButton) GridComponentArray[cell.i + k][jtemp]);
                        }
                    }
                    if(k == 0) {
                        if(GridComponentArray[cell.i - 1][j].isWall) {
                            selectWall((WallButton) GridComponentArray[cell.i - 1][j]);
                        }
                    }
                    if(k == (imagePane.getImageCellHeight() * 2) - 2) {
                        int ktemp = (imagePane.getImageCellHeight() * 2) - 1;
                        if(GridComponentArray[cell.i + ktemp][j].isWall) {
                            selectWall((WallButton) GridComponentArray[cell.i + ktemp][j]);
                        }
                    }
                //}
            }
        }
        cst.gridx = cell.x;
        cst.gridy = cell.y;
        cst.gridwidth = (imagePane.getImageCellWidth() * CELLWIDTH) + ((imagePane.getImageCellWidth() - 1) * WALLSHORT);
        cst.gridheight = (imagePane.getImageCellHeight() * CELLWIDTH) + ((imagePane.getImageCellHeight() - 1) * WALLSHORT);
        GridImage imgButton = new GridImage(cell.x, cell.y, cell.i, cell.j, imagePane.getImageCellWidth(), imagePane.getImageCellHeight(),imagePane.getOriginalImage());
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
        label.setPreferredSize(new Dimension(1000,1000));
        imgButton.add(label);
        imgButton.setBackground(GRIDCOLOR);
        imgButton.setPreferredSize(new Dimension(
                ((imagePane.getImageCellWidth() * CELLWIDTH) + ((imagePane.getImageCellWidth() - 1) * WALLSHORT)) * sizeMultiplier,
                ((imagePane.getImageCellHeight() * CELLWIDTH) + ((imagePane.getImageCellHeight() - 1) * WALLSHORT)) * sizeMultiplier
        ));
        GridImages.put(Arrays.asList(cell.i, cell.j),imgButton);
        add(GridImages.get(Arrays.asList(cell.i, cell.j)), cst);
        revalidate();
        repaint();
        SetEditState(false);
    }

    /**
     * Add grid component back to grid panel
     * @param i row of cell
     * @param j column of cell
     */
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

    /**
     * Called when state of supplied GridImage is changed
     * @param imgPane GridImage whose state has changed
     */
    private void imagePanelStateChange(GridImage imgPane) {
        if(State == GridState.REMOVEIMAGE) {
            if(imgPane.getModel().isSelected()) {
                imgPane.getModel().setSelected(false);
                remove(GridImages.get(Arrays.asList(imgPane.i,imgPane.j)));
                for (int i = imgPane.i; i < (((imgPane.HEIGHT * 2) - 1) + imgPane.i); i++) {
                    addGridComponentToPanel(i, imgPane.j);
                    GridComponentArray[i][imgPane.j].isDisabled = false;
                }
                for (int j = imgPane.j + 1; j < (((imgPane.WIDTH * 2) - 1) + imgPane.j); j++) {
                    for (int k = 0; k < (imgPane.HEIGHT * 2) - 1; k++) {
                            addGridComponentToPanel(imgPane.i + k, j);
                            GridComponentArray[imgPane.i + k][j].isDisabled = false;
                    }
                }
                GridImages.remove(Arrays.asList(imgPane.i,imgPane.j));
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

    /**
     * Unselects wall
     * @param btn wall to be unselected
     */
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

    /**
     * Selects wall
     * @param btn wall to be selected
     */
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

    /**
     * Resets GridComponents colors to their original color, exception if showSolutionLine is on
     */
    protected void ResetGridColors() {
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
                        int mazeCelli = (i - 1) / 2;
                        int mazeCellj = (j - 1) / 2;
                        if(showSolutionLine && GridMazeCellArray[mazeCellj][mazeCellj].getSolutionCell()) {
                            GridComponentArray[i][j].setBackground(SOLUTIONCOLOR);
                        }
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

    /**
     * Turns corresponding MazeCell wall on given GridComponentArray locations of WallButton
     * @param i row of wallbutton
     * @param j column of wallbutton
     * @param orien orientation of button
     * @param isOn is wall on or off
     */
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
     * @param i row of intersect
     * @param j column of intersect
     * @return return boolean if wall is adjacent to Selected wall or not
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

    /**
     * Returns the GridMazeCellArray
     * @return MazeCell[][] GridMazeCellArray
     */
    protected MazeCell[][] getGridMazeCellArray() {
        return GridMazeCellArray;
    }
}

