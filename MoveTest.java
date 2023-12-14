package knefatafl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {
    static Player attacker;
    static Player defender;
    static Game game;

    @BeforeAll
    static void initAll() {
        Square.createBoard();
        attacker = new Player("Attackers", "Attackers");
        defender = new Player("Defenders", "Defenders");
        game = new Game(attacker, defender);
    }

    // getMoveLimit
    @Test
    void getMoveLimitNoShouldStoreIntegerMaxValue() {
        Scanner scanner = new Scanner("n");
        Move.determineMoveLimit(scanner);
        assertEquals(Integer.MAX_VALUE, Move.getMoveLimit());
    }

    @Test
    void getMoveLimitShouldStore100() {
        String s = "y 100 200";
        Scanner scanner = new Scanner(s);
        Move.determineMoveLimit(scanner);
        assertEquals(100, Move.getMoveLimit());
    }

    // isMoveValid
    @Test
    void isMoveValidSameStartingAndEndingSquaresShouldReturnFalse() {
        Piece piece = attacker.getPieceByID("A0");
        Square startingSquare = piece.getCurrentSquare();
        Square endingSquare = piece.getCurrentSquare();

        assertFalse(Move.isMoveValid(game, piece, startingSquare, endingSquare));
    }

    @Test
    void isMoveValidDiagonalMovesShouldReturnFalse() {
        Piece piece = attacker.getPieceByID("A0");
        Square startingSquare = piece.getCurrentSquare();
        Square endingSquare = Square.getSquare(new int[] {1, 2});

        assertFalse(Move.isMoveValid(game, piece, startingSquare, endingSquare));
    }

    @Test
    void isMoveValidKingMovingToRefugeShouldReturnTrue() {
        King king = new King(Square.getSquare(new int[] {1, 0}));
        Square startingSquare = king.getCurrentSquare();
        Square endingSquare = Square.getSquare(new int[] {0, 0});

        assertTrue(Move.isMoveValid(game, king, startingSquare, endingSquare));
    }

    @Test
    void isMoveValidMinionMovingToRefugeShouldReturnFalse() {
        Piece piece = attacker.getPieceByID("A0");
        Square startingSquare = piece.getCurrentSquare();
        Square endingSquare = Square.getSquare(new int[] {0, 0});

        assertFalse(Move.isMoveValid(game, piece, startingSquare, endingSquare));
    }

    @Test
    void isMoveValidUnoccupiedEndingSquareShouldReturnTrue() {
        Piece piece = attacker.getPieceByID("A0");
        Square startingSquare = piece.getCurrentSquare();
        Square endingSquare = Square.getSquare(new int[] {4, 3});

        assertTrue(Move.isMoveValid(game, piece, startingSquare, endingSquare));
    }

    @Test
    void isMoveValidOccupiedEndingSquareShouldReturnFalse() {
        Piece piece = attacker.getPieceByID("A0");
        Square startingSquare = piece.getCurrentSquare();
        Square endingSquare = Square.getSquare(new int[] {5, 3});

        assertFalse(Move.isMoveValid(game, piece, startingSquare, endingSquare));
    }

    @Test
    void isMoveValidNoPiecesInBetweenShouldReturnTrue() {
        Piece piece = attacker.getPieceByID("A0");
        Square startingSquare = piece.getCurrentSquare();
        Square endingSquare = Square.getSquare(new int[] {3, 3});

        assertTrue(Move.isMoveValid(game, piece, startingSquare, endingSquare));
    }

    @Test
    void isMoveValidPiecesInBetweenShouldReturnFalse() {
        Piece piece = attacker.getPieceByID("A0");
        Square startingSquare = piece.getCurrentSquare();
        Square endingSquare = Square.getSquare(new int[] {7, 3});

        assertFalse(Move.isMoveValid(game, piece, startingSquare, endingSquare));
    }
}