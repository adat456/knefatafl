package knefatafl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

class EliminateTest {
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
    void eliminateOpponentsDefenderSandwichedByTwoAttackersIsEliminated() {
        Piece A0 = attacker.getPieceByID("A0");
        Piece A6 = attacker.getPieceByID("A6");
        Piece D0 = defender.getPieceByID("D0");

        A0.setCurrentSquare(Square.getSquare(new int[] {4, 3}));
        A6.setCurrentSquare(Square.getSquare(new int[] {6, 3}));
        Eliminate.eliminateOpponents(Square.getSquare(new int[] {6, 3}), defender);

        assertTrue(((Minion)D0).isEliminated());
    }

    @Test
    void eliminateOpponentsAttackerSandwichedByDefenderAndWallIsEliminated() {
        Piece A0 = attacker.getPieceByID("A0");
        Piece D0 = defender.getPieceByID("D0");

        D0.setCurrentSquare(Square.getSquare(new int[] {1, 3}));
        Eliminate.eliminateOpponents(Square.getSquare(new int[] {1, 3}), attacker);

        assertTrue(((Minion)A0).isEliminated());
    }

    @Test
    void eliminateOpponentsAttackerSandwichedByDefenderAndRefugeIsEliminated() {
        Piece A0 = attacker.getPieceByID("A0");
        Piece D0 = defender.getPieceByID("D0");

        A0.setCurrentSquare(Square.getSquare(new int[] {0, 1}));
        D0.setCurrentSquare(Square.getSquare(new int[] {0, 3}));
        D0.setCurrentSquare(Square.getSquare(new int[] {0, 2}));
        Eliminate.eliminateOpponents(Square.getSquare(new int[] {0, 2}), attacker);

        assertTrue(((Minion)A0).isEliminated());
    }
}