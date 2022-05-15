import java.util.*;
import java.util.List;

public class Algorithm {


    /**
     * The method randomly generates a maze using DFS
     * @param width the width that the maze will generate with
     * @param height the height that the maze will generate with
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
                currentCell.toggleVisited();
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
        // toggles all the maze cell so that they are marked as not visited
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                mazeData[i][j].toggleVisited();
            }
        }
        return mazeData;
    }

    /**
     * The method checks if a maze represented by mazeData is solvable
     * @param mazeData a two-dimensional int that represents a maze
     * @return Checks a given maze for its solvability and returns a boolean
     */
    protected boolean mazeSolvability(MazeCell[][] mazeData) {
        return true;
    }

    /**
     * The method checks for the optimal solution for the maze represented by mazeData
     * @param mazeData a two-dimensional int that represents a maze
     * @return returns a two-dimensional int that represents a maze
     */
    protected MazeCell[][] optimalSolution(int[][] mazeData) {
        return null;
    }

    /**
     * The method checks for all the dead cells within the maze represented by mazeData and returns a percentage of them
     * @param mazeData a two-dimensional int that represents a maze
     * @return returns a percentage representing the dead cells in a maze
     */
    protected float showDeadCells(int[][] mazeData) {
        return 0;
    }
}
