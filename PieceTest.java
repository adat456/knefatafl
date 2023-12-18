package knefatafl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {
    static Player attacker;
    static Player defender;

    @BeforeEach
    void init() {
        Square.createBoard();
        attacker = new Player("Attackers", "Attackers");
        defender = new Player("Defenders", "Defenders");
    }

    // findAllValidSquares
    @Test
    void findALlValidSquaresForBorderAttackerThatCanMoveUpAndRight() {
        Minion A0 = (Minion)attacker.getPieceByID("A0");
        ArrayList<Square> validSquares = A0.findAllValidSquares();

        ArrayList<Square> expectedValidSquares = new ArrayList<>();
        expectedValidSquares.add(Square.getSquare(new int[] {0, 2}));
        expectedValidSquares.add(Square.getSquare(new int[] {0, 1}));
        expectedValidSquares.add(Square.getSquare(new int[] {1, 3}));
        expectedValidSquares.add(Square.getSquare(new int[] {2, 3}));
        expectedValidSquares.add(Square.getSquare(new int[] {3, 3}));
        expectedValidSquares.add(Square.getSquare(new int[] {4, 3}));

        assertEquals(expectedValidSquares, validSquares);
    }

    @Test
    void findALlValidSquaresForAttackerThatCanMoveAllFourDirectins() {
        Minion A0 = (Minion)attacker.getPieceByID("A0");
        A0.setCurrentSquare(Square.getSquare(new int[] {3, 3}));
        ArrayList<Square> validSquares = A0.findAllValidSquares();

        ArrayList<Square> expectedValidSquares = new ArrayList<>();
        expectedValidSquares.add(Square.getSquare(new int[] {3, 2}));
        expectedValidSquares.add(Square.getSquare(new int[] {3, 1}));
        expectedValidSquares.add(Square.getSquare(new int[] {3, 4}));
        expectedValidSquares.add(Square.getSquare(new int[] {2, 3}));
        expectedValidSquares.add(Square.getSquare(new int[] {1, 3}));
        expectedValidSquares.add(Square.getSquare(new int[] {0, 3}));
        expectedValidSquares.add(Square.getSquare(new int[] {4, 3}));

        assertEquals(expectedValidSquares, validSquares);
    }

    @Test
    void findAllValidSquaresForTrappedKing() {
        King king = (King)defender.getPieceByID("D12");
        ArrayList<Square> validSquares = king.findAllValidSquares();

        ArrayList<Square> expectedValidSquares = new ArrayList<>();

        assertEquals(expectedValidSquares, validSquares);
    }

    @Test
    void findAllValidSquaresForBorderKingThatCanMoveLeftRightAndDown() {
        // moving attacker into position (to reduce number of possible moves)
        Piece A0 = attacker.getPieceByID("A0");
        A0.setCurrentSquare(Square.getSquare(new int[] {1, 3}));

        King king = (King)defender.getPieceByID("D12");
        king.setCurrentSquare(Square.getSquare(new int[] {1, 0}));
        ArrayList<Square> validSquares = king.findAllValidSquares();

        ArrayList<Square> expectedValidSquares = new ArrayList<>();
        expectedValidSquares.add(Square.getSquare(new int[] {1, 1}));
        expectedValidSquares.add(Square.getSquare(new int[] {1, 2}));
        expectedValidSquares.add(Square.getSquare(new int[] {0, 0}));
        expectedValidSquares.add(Square.getSquare(new int[] {2, 0}));

        assertEquals(expectedValidSquares, validSquares);
    }


    // setCurrentSquare
    @Test
    void moveAttackerToSquareShouldToggleSquareOccupation() {
        Piece A0 = attacker.getPieceByID("A0");
        Square startingSquare = A0.getCurrentSquare();
        A0.setCurrentSquare(Square.getSquare(new int[] {1, 3}));
        Square endingSquare = A0.getCurrentSquare();

        assertTrue(!startingSquare.isOccupied() && endingSquare.isOccupied());
    }
}