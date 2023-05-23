package cz.cvut.fel.pjv.arimaa;

import cz.cvut.fel.pjv.arimaa.model.Board;
import cz.cvut.fel.pjv.arimaa.model.PlayerColor;
import cz.cvut.fel.pjv.arimaa.model.figures.Elephant;
import cz.cvut.fel.pjv.arimaa.model.figures.Figure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTests {

    Board board;

    // Optional: Set up any necessary objects or configurations before each test
    @BeforeEach
    public void setUp() {
        board = new Board(false, false);

        for (int i = 0; i < 8; i++) {
            board.getGoldPlayer().placeFigure(1,i);
        }
        for (int i = 0; i < 8; i++) {
            board.getGoldPlayer().placeFigure(0,i);
        }

        board.changeCurrentPlayer();

        for (int i = 0; i < 8; i++) {
            board.getSilverPlayer().placeFigure(6,i);
        }
        for (int i = 0; i < 8; i++) {
            board.getSilverPlayer().placeFigure(7,i);
        }

        board.changeCurrentPlayer();
    }

    // Test case 1
    @Test
    public void moveMethodTest() {
        // Arrange (Set up the test case)
        Figure elephant = new Elephant(PlayerColor.GOLD, board, 2, 0);

        // Act (Perform the action)
        board.getBoard()[1][0].move(2,0);

        // Assert (Verify the result)
        // Use methods from the `Assertions` class for assertions
        assertEquals(board.getBoard()[2][0], elephant);
    }

    // Test case 2
    @Test
    public void canBePushedTest1() {
        // Arrange (Set up the test case)
        boolean expectedResult = true;
        boolean actualResult;

        // Act (Perform the action)
        board.getBoard()[1][7].move(2,7);
        board.getBoard()[2][7].move(3,7);
        board.getBoard()[3][7].move(4,7);
        board.getBoard()[4][7].move(4,6);
        board.changeCurrentPlayer();
        board.changeCurrentPlayer();
        board.getBoard()[4][6].move(4,5);
        board.getBoard()[4][5].move(4,4);
        board.getBoard()[4][4].move(5,4);
        actualResult = board.getBoard()[5][4].canBePushed();

        // Assert (Verify the result)
        // Use methods from the `Assertions` class for assertions
        assertEquals(expectedResult, actualResult);
    }

    // Test case 3
    @Test
    public void canBePushedTest2() {
        // Arrange (Set up the test case)
        boolean expectedResult = false;
        boolean actualResult;

        // Act (Perform the action)
        board.getBoard()[1][7].move(2,7);
        board.getBoard()[2][7].move(3,7);
        actualResult = board.getBoard()[3][7].canBePushed();

        // Assert (Verify the result)
        // Use methods from the `Assertions` class for assertions
        assertEquals(expectedResult, actualResult);
    }

    // Test case 4
    @Test
    public void getAdjacentFriendlyFiguresTest() {
        // Arrange (Set up the test case)
        List<Figure> expectedResult = new ArrayList<>();
        List<Figure> actualResult;
        expectedResult.add(board.getBoard()[0][1]);
        expectedResult.add(board.getBoard()[1][0]);
        expectedResult.add(board.getBoard()[1][2]);

        // Act (Perform the action)
        actualResult = board.getBoard()[1][1].getAdjacentFriendlyFigures();

        // Assert (Verify the result)
        // Use methods from the `Assertions` class for assertions
        assertEquals(expectedResult, actualResult);
    }

    // Test case 5
    @Test
    public void getAdjacentEnemyFiguresTest() {
        // Arrange (Set up the test case)
        List<Figure> expectedResult = new ArrayList<>();
        List<Figure> actualResult;
        expectedResult.add(board.getBoard()[6][1]);

        // Act (Perform the action)
        board.getBoard()[1][1].move(2,1);
        board.getBoard()[2][1].move(3,1);
        board.getBoard()[3][1].move(4,1);
        board.getBoard()[4][1].move(5,1);
        actualResult = board.getBoard()[5][1].getAdjacentEnemyFigures();

        // Assert (Verify the result)
        // Use methods from the `Assertions` class for assertions
        assertEquals(expectedResult, actualResult);
    }
}
