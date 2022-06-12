import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MazeTest {
    MazeDataSource dataSource;
    MazeDetails details;

    @Before
    public void setUp() {
        dataSource = new MazeDataSource(new MainGUI());
        details = new MazeDetails("bunny", "rabbit", "maze data", "image data", 4, 4, "maze image", "maze solution");
        details.mazeImage = "maze image";
        details.mazeSolution = "maze solution";
    }

    @Test
    public void saveMaze() throws MazeException {
        dataSource.saveNewMaze(details);
        assertEquals("bunny", dataSource.getSelectedMaze(details.id).author);
    }

    @Test
    public void updateMaze() throws MazeException {
        details.author = "bird";
        dataSource.updateMaze(details);
        assertEquals("bird", dataSource.getSelectedMaze(details.id).author);
    }

    @Test
    public void deleteMaze() throws MazeException{
        dataSource.deleteMaze(new int[] {details.id});
        assertEquals(null, dataSource.getSelectedMaze(details.id).author);
    }

}