import java.util.*;
import java.util.List;

/** Class containing algorithms used in managing a maze */
public class Algorithm {

    /**
     * Generates a maze using the Depth First Search algorithm
     * @param width the width that the maze will be generated with
     * @param height the height that the maze will be generated with
     * @return returns a two-dimensional MazeCell that represents a maze
     */
    protected MazeCell[][] generateMaze(int width, int height) {
        // Creates a blank maze with all the walls set to true
        Maze maze = new Maze(height, width, true);
        MazeCell[][] mazeData = maze.getMaze();
        // Picks a random index to start at when generating the maze
        int[] index = new int[2];
        index[0] = new Random().nextInt(height);
        index[1] = new Random().nextInt(width);

        // Initializes the mazeStack which will track the state of the maze and the directionStack which tracks the
        // previous direction
        Stack<MazeCell> mazeStack = new Stack<>();
        Stack<String> directionStack = new Stack<>();
        mazeStack.push(mazeData[index[0]][index[1]]);
        directionStack.push(null);

        String[] directions = {
                "N",
                "E",
                "S",
                "W"
        };
        MazeCell currentCell;
        String previousDirection;
        while(!mazeStack.empty()) {
            // Sets the current cell to the top of the stack
            currentCell = mazeStack.pop();
            // Sets the previous direction to the top of the stack
            previousDirection = directionStack.pop();

            // Randomises the array of directions
            List<String> directionList = Arrays.asList(directions);
            Collections.shuffle(directionList);
            directionList.toArray(directions);

            // Adds the cells in all directions of the current cell to the maze stack
            // Adds the direction used to get to the cell to the top of the direction stack
            for (String direction : directions) {
                if (Objects.equals(direction, "N") && currentCell.getCellUp() != null) {
                    if (!currentCell.getCellUp().getVisited()) {
                        mazeStack.push(currentCell.getCellUp());
                        directionStack.push("N");
                    }
                } else if (Objects.equals(direction, "E") && currentCell.getCellRight() != null) {
                    if (!currentCell.getCellRight().getVisited()) {
                        mazeStack.push(currentCell.getCellRight());
                        directionStack.push("E");
                    }
                } else if (Objects.equals(direction, "S") && currentCell.getCellDown() != null) {
                    if (!currentCell.getCellDown().getVisited()) {
                        mazeStack.push(currentCell.getCellDown());
                        directionStack.push("S");
                    }
                } else if (Objects.equals(direction, "W") && currentCell.getCellLeft() != null) {
                    if (!currentCell.getCellLeft().getVisited()) {
                        mazeStack.push(currentCell.getCellLeft());
                        directionStack.push("W");
                    }
                }
            }

            // Removes the wall between the current cell and the previous cell if the current cell has not been visited
            if (!currentCell.getVisited()) {
                currentCell.setVisited(true);
                if (previousDirection != null) {
                    switch (previousDirection) {
                        case "N" -> {
                            currentCell.getCellDown().toggleWallUp();
                            currentCell.toggleWallDown();
                        }
                        case "E" -> {
                            currentCell.getCellLeft().toggleWallRight();
                            currentCell.toggleWallLeft();
                        }
                        case "S" -> {
                            currentCell.getCellUp().toggleWallDown();
                            currentCell.toggleWallUp();
                        }
                        case "W" -> {
                            currentCell.getCellRight().toggleWallLeft();
                            currentCell.toggleWallRight();
                        }
                    }
                }
            }
        }
        mazeData[0][0].toggleWallLeft();
        mazeData[height-1][width-1].toggleWallRight();
        // toggles all the maze cell so that they are marked as not visited
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                mazeData[i][j].setVisited(false);
            }
        }
        return mazeData;
    }

    /**
     * Checks the solvability of a maze. A maze is defined as solvable if it has two open ends that link up to each
     * other
     * @param mazeData a two-dimensional MazeCell that represents the maze
     * @return Returns true if the given maze is solvable and false otherwise
     */
    protected MazeCell[][] mazeSolvability(MazeCell[][] mazeData) {
        MazeCell[] indexes = findOpenCells(mazeData);
        if (indexes == null) {
            return null;
        }
        MazeCell startIndex = indexes[0];
        MazeCell endIndex = indexes[1];

        Queue<MazeCell> queue = new LinkedList<>();
        queue.add(startIndex);
        startIndex.setVisited(true);

        while(!queue.isEmpty()) {
            MazeCell current = queue.remove();

            if (current.equals(endIndex)) {
                for (MazeCell[] mazeDatum : mazeData) {
                    for (int j = 0; j < mazeData[0].length; j++) {
                        mazeDatum[j].setVisited(false);
                    }
                }
                return mazeData;
            }

            if (current.getCellUp() != null && !current.getCellUp().getVisited() && !current.getWallUp()) {
                queue.add(current.getCellUp());
                current.getCellUp().setVisited(true);
                current.getCellUp().setParent(current);
            }
            if (current.getCellRight() != null && !current.getCellRight().getVisited()
                    && !current.getWallRight()) {
                queue.add(current.getCellRight());
                current.getCellRight().setVisited(true);
                current.getCellRight().setParent(current);
            }
            if (current.getCellDown() != null && !current.getCellDown().getVisited()
                    && !current.getWallDown()) {
                queue.add(current.getCellDown());
                current.getCellDown().setVisited(true);
                current.getCellDown().setParent(current);
            }
            if (current.getCellLeft() != null && !current.getCellLeft().getVisited()
                    && !current.getWallLeft()) {
                queue.add(current.getCellLeft());
                current.getCellLeft().setVisited(true);
                current.getCellLeft().setParent(current);
            }
        }
        for (MazeCell[] mazeDatum : mazeData) {
            for (int j = 0; j < mazeData[0].length; j++) {
                mazeDatum[j].setVisited(false);
            }
        }
        return null;
    }

    /**
     * Checks all the outside cells on a maze for openings. Returns two MazeCells if only two of the outside cells are
     * open to the outside.
     * @param mazeData a two-dimensional MazeCell that represents the maze
     * @return a MazeCell array containing two openings if they were found, and null if there are more than or less than
     * two openings
     */
    protected MazeCell[] findOpenCells(MazeCell[][] mazeData) {
        MazeCell[] openEnds = new MazeCell[2];
        int openCellCount = 0;
        int mazeHeight = mazeData.length;
        int mazeWidth = mazeData[0].length;
        for (int i = 0; i < mazeWidth; i++) {     //top and bottom cells search
            if (!mazeData[0][i].getWallUp()) {
                openCellCount++;
                if (openCellCount > 2) {
                    return null;
                }
                openEnds[openCellCount-1] = mazeData[0][i];
            }
            if (!mazeData[mazeHeight-1][i].getWallDown()) {
                openCellCount++;
                if (openCellCount > 2) {
                    return null;
                }
                openEnds[openCellCount-1] = mazeData[mazeHeight-1][i];
            }
        }
        for (MazeCell[] mazeDatum : mazeData) {  //right and left cells searched
            if (!mazeDatum[0].getWallLeft()) {
                openCellCount++;
                if (openCellCount > 2) {
                    return null;
                }
                openEnds[openCellCount - 1] = mazeDatum[0];
            }
            if (!mazeDatum[mazeWidth - 1].getWallRight()) {
                openCellCount++;
                if (openCellCount > 2) {
                    return null;
                }
                openEnds[openCellCount - 1] = mazeDatum[mazeWidth - 1];
            }
        }
        if (openCellCount == 2) {
            return openEnds;
        } else {
            return null;
        }
    }

    /**
     * Checks a maze for its optimal solution and returns it. Assumes that a maze is solvable when checking for its
     * optimal solution.
     * @param mazeData a two-dimensional MazeCell that represents the maze
     * @return a two-dimensional MazeCell that has been modified to show the optimal solution
     */
    protected MazeCell[][] optimalSolution(MazeCell[][] mazeData) {
        for (MazeCell[] mazeDatum : mazeData) {
            for (int j = 0; j < mazeData[0].length; j++) {
                mazeDatum[j].setSolutionCell(false);
            }
        }
        MazeCell[] indexes = findOpenCells(mazeData);
        MazeCell startIndex = indexes[0];
        MazeCell current = indexes[1];
        while (current != startIndex) {
            current.setSolutionCell(true);
            current = current.getParent();
        }
        startIndex.setSolutionCell(true);

        for (MazeCell[] mazeDatum : mazeData) {
            for (int j = 0; j < mazeData[0].length; j++) {
                mazeDatum[j].setParent(null);
            }
        }
        return mazeData;
    }

    /**
     * Checks a maze for its optimal solution and returns the percentage of cells used to find the optimal solution.
     * Assumes that a maze is solvable when checking for its optimal solution
     * @param mazeData a two-dimensional MazeCell that represents the maze
     * @return the percentage of cells in a maze used to find the optimal solution
     */
    protected float showOptimalCells(MazeCell[][] mazeData) {
        float totalCells = mazeData[0].length * mazeData.length;
        float optimalCells = 0;
        MazeCell[] indexes = findOpenCells(mazeData);
        MazeCell startIndex = indexes[0];
        MazeCell current = indexes[1];
        while (current != startIndex) {
            optimalCells++;
            current = current.getParent();
        }
        optimalCells++;

        for (MazeCell[] mazeDatum : mazeData) {
            for (int j = 0; j < mazeData[0].length; j++) {
                mazeDatum[j].setParent(null);
            }
        }
        return (optimalCells / totalCells) * 100;
    }

    /**
     * Checks a maze for all its dead cells and returns the percentage of cells in the maze that are dead cells. A dead
     * cell is defined as a cell with 3 walls.
     * @param mazeData a two-dimensional MazeCell that represents the maze
     * @return returns a percentage representing the dead cells in a maze
     */
    protected float showDeadCells(MazeCell[][] mazeData) {
        MazeCell currentCell;
        float totalCells = mazeData[0].length * mazeData.length;
        float deadCells = 0;
        for (int i = 0; i < mazeData[0].length; i++) {
            for (int j = 0; j < mazeData.length; j++) {
                int numWalls = 0;
                currentCell = mazeData[i][j];
                if (currentCell.getWallUp()) {
                    numWalls++;
                }
                if (currentCell.getWallRight()) {
                    numWalls++;
                }
                if (currentCell.getWallDown()) {
                    numWalls++;
                }
                if (currentCell.getWallLeft()) {
                    numWalls++;
                }
                if (numWalls == 3) {
                    deadCells++;
                }
            }
        }
        return (deadCells / totalCells) * 100;
    }

}
