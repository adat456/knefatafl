package knefatafl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SquareTest {
    @BeforeAll
    static void initAll() {
        Square.createBoard();
    }

    @Test
    void getSquare00ShouldReturnCoords00() {
        var square = Square.getSquare(new int[] {0, 0});
        assertArrayEquals(new int[] {0, 0}, square.getCoords());
    }

    @Test
    void getSquare1010ShouldReturnCoords1010() {
        var square = Square.getSquare(new int[] {10, 10});
        assertArrayEquals(new int[] {10, 10}, square.getCoords());
    }

    @Test
    void getSquareCoordsBelowBoundsShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    Square.getSquare(new int[] {-1, -1});
                });
    }

    @Test
    void getSquareCoordsAboveBoundsShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    Square.getSquare(new int[] {11, 11});
                });
    }

    @Test
    void toggleOccupiedSquare00ShouldBeOccupied() {
        var square = Square.getSquare(new int[] {0, 0});
        square.toggleOccupied();
        assertTrue(square.isOccupied());
    }
}