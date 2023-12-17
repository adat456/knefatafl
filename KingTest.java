package knefatafl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {
    static Player attacker;
    static Player defender;
    static Game game;

    @BeforeEach()
    void init() {
        Square.createBoard();
        attacker = new Player("Attackers", "Attackers");
        defender = new Player("Defenders", "Defenders");
        game = new Game(attacker, defender);
    }

    @Test
    void determineStatusSurroundedByAttackersShouldReturnAttackersWin() {
        King king = (King)defender.getPieceByID("D12");
        Piece A0 = attacker.getPieceByID("A0");
        Piece A1 = attacker.getPieceByID("A1");
        Piece A2 = attacker.getPieceByID("A2");
        Piece A3 = attacker.getPieceByID("A3");

        king.setCurrentSquare(Square.getSquare(new int[] {2, 2}));
        A0.setCurrentSquare(Square.getSquare(new int[] {1, 2}));
        A1.setCurrentSquare(Square.getSquare(new int[] {2, 1}));
        A2.setCurrentSquare(Square.getSquare(new int[] {3, 2}));
        A3.setCurrentSquare(Square.getSquare(new int[] {2, 3}));

        assertEquals("Attackers win.", king.determineStatus(attacker));
    }

    @Test
    void determineStatusSurroundedByAttackersAndHostileSquareShouldReturnAttackersWin() {
        // moving defenders away into arbitrary positions
        Piece D0 = defender.getPieceByID("D0");
        Piece D1 = defender.getPieceByID("D1");
        Piece D2 = defender.getPieceByID("D2");
        Piece D3 = defender.getPieceByID("D3");
        D0.setCurrentSquare(Square.getSquare(new int[] {1, 0}));
        D1.setCurrentSquare(Square.getSquare(new int[] {2, 0}));
        D2.setCurrentSquare(Square.getSquare(new int[] {0, 1}));
        D3.setCurrentSquare(Square.getSquare(new int[] {1, 1}));

        // moving king into position
        King king = (King)defender.getPieceByID("D12");
        king.setCurrentSquare(Square.getSquare(new int[] {5, 4}));

        // moving attackers into position
        Piece A0 = attacker.getPieceByID("A0");
        Piece A1 = attacker.getPieceByID("A1");
        Piece A2 = attacker.getPieceByID("A2");
        A0.setCurrentSquare(Square.getSquare(new int[] {5, 3}));
        A1.setCurrentSquare(Square.getSquare(new int[] {4, 4}));
        A2.setCurrentSquare(Square.getSquare(new int[] {6, 4}));

        assertEquals("Attackers win.", king.determineStatus(attacker));
    }

    @Test
    void determineStatusPartiallySurroundedByAttackersShouldNotReturnEmptyString() {
        // moving king into position
        King king = (King)defender.getPieceByID("D12");
        king.setCurrentSquare(Square.getSquare(new int[] {2, 2}));

        // moving attackers into position
        Piece A0 = attacker.getPieceByID("A0");
        Piece A1 = attacker.getPieceByID("A1");
        Piece A2 = attacker.getPieceByID("A2");
        A0.setCurrentSquare(Square.getSquare(new int[] {1, 2}));
        A1.setCurrentSquare(Square.getSquare(new int[] {2, 1}));
        A2.setCurrentSquare(Square.getSquare(new int[] {3, 2}));

        assertEquals("", king.determineStatus(attacker));
    }

    @Test
    void determineStatusOnRefugeSquareShouldReturnDefendersWin() {
        // moving king into position
        King king = (King)defender.getPieceByID("D12");
        king.setCurrentSquare(Square.getSquare(new int[] {0, 0}));

        assertEquals("Defenders win.", king.determineStatus(attacker));
    }
}