package knefatafl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    // sandwich elimination
    @Test
    void defenderSandwichedByTwoAttackersShouldBeEliminated() {
        Piece A0 = attacker.getPieceByID("A0");
        Piece A6 = attacker.getPieceByID("A6");
        Piece D0 = defender.getPieceByID("D0");

        A0.setCurrentSquare(Square.getSquare(new int[] {4, 3}));
        A6.setCurrentSquare(Square.getSquare(new int[] {6, 3}));
        Eliminate.eliminateOpponents(Square.getSquare(new int[] {6, 3}), attacker, defender);

        assertTrue(((Minion)D0).isEliminated());
    }

    @Test
    void attackerSandwichedByDefenderAndWallShouldNotBeEliminated() {
        Piece A0 = attacker.getPieceByID("A0");
        Piece D0 = defender.getPieceByID("D0");

        D0.setCurrentSquare(Square.getSquare(new int[] {1, 3}));
        Eliminate.eliminateOpponents(Square.getSquare(new int[] {1, 3}), defender, attacker);

        assertFalse(((Minion)A0).isEliminated());
    }

    @Test
    void attackerSandwichedByDefenderAndRefugeShouldBeEliminated() {
        Piece A0 = attacker.getPieceByID("A0");
        Piece D0 = defender.getPieceByID("D0");

        A0.setCurrentSquare(Square.getSquare(new int[] {0, 1}));
        D0.setCurrentSquare(Square.getSquare(new int[] {0, 2}));
        Eliminate.eliminateOpponents(Square.getSquare(new int[] {0, 2}), defender, attacker);

        assertTrue(((Minion)A0).isEliminated());
    }

    @Test
    void twoDefendersSandwichedByAttackersAndRefugeShouldBeEliminated() {
        Piece A0 = attacker.getPieceByID("A0");
        Piece D0 = defender.getPieceByID("D0");
        Piece D1 = defender.getPieceByID("D1");

        A0.setCurrentSquare(Square.getSquare(new int[] {1, 2}));
        D0.setCurrentSquare(Square.getSquare(new int[] {0, 3}));
        D1.setCurrentSquare(Square.getSquare(new int[] {0, 1}));
        A0.setCurrentSquare(Square.getSquare(new int[] {0, 2}));
        Eliminate.eliminateOpponents(Square.getSquare(new int[] {0, 2}), attacker, defender);

        assertTrue(((Minion)D0).isEliminated() && ((Minion)D1).isEliminated());
    }

    // shield wall elimination
    @Test
    void onlyOneFlankDefenderAndIntactFrontDefendersShouldNotEliminateAttackers() {
        // getting potential eliminees
        Minion A0 = (Minion)attacker.getPieceByID("A0");
        Minion A1 = (Minion)attacker.getPieceByID("A1");
        Minion A2 = (Minion)attacker.getPieceByID("A2");

        // moving pieces out of the way
        Piece A3 = attacker.getPieceByID("A3");
        Piece A4 = attacker.getPieceByID("A4");
        Piece A5 = attacker.getPieceByID("A5");
        A3.setCurrentSquare(Square.getSquare(new int[] {1, 0}));
        A4.setCurrentSquare(Square.getSquare(new int[] {2, 0}));
        A5.setCurrentSquare(Square.getSquare(new int[] {1, 1}));

        // setting up the faulty shield wall
        Piece D0 = defender.getPieceByID("D0");
        Piece D1 = defender.getPieceByID("D1");
        Piece D2 = defender.getPieceByID("D2");
        Piece D3 = defender.getPieceByID("D3");
        D0.setCurrentSquare(Square.getSquare(new int[] {0, 2}));
        D1.setCurrentSquare(Square.getSquare(new int[] {1, 3}));
        D2.setCurrentSquare(Square.getSquare(new int[] {1, 4}));
        D3.setCurrentSquare(Square.getSquare(new int[] {1, 5}));

        Eliminate.eliminateOpponents(Square.getSquare(new int[] {0, 2}), defender, attacker);
        assertTrue(!A0.isEliminated() && !A1.isEliminated() && !A2.isEliminated());
    }

    @Test
    void bothFlankDefendersAndFrontWallWithHoleShouldNotEliminateAttackers() {
        // getting potential eliminees
        Minion A0 = (Minion)attacker.getPieceByID("A0");
        Minion A1 = (Minion)attacker.getPieceByID("A1");
        Minion A2 = (Minion)attacker.getPieceByID("A2");

        // moving pieces out of the way
        Piece A3 = attacker.getPieceByID("A3");
        Piece A4 = attacker.getPieceByID("A4");
        Piece A5 = attacker.getPieceByID("A5");
        A3.setCurrentSquare(Square.getSquare(new int[] {1, 0}));
        A4.setCurrentSquare(Square.getSquare(new int[] {2, 0}));
        A5.setCurrentSquare(Square.getSquare(new int[] {1, 1}));

        // setting up the faulty shield wall
        Piece D0 = defender.getPieceByID("D0");
        Piece D1 = defender.getPieceByID("D1");
        Piece D2 = defender.getPieceByID("D2");
        Piece D3 = defender.getPieceByID("D3");
        D0.setCurrentSquare(Square.getSquare(new int[] {0, 2}));
        D1.setCurrentSquare(Square.getSquare(new int[] {1, 3}));
        D2.setCurrentSquare(Square.getSquare(new int[] {1, 4}));
        D3.setCurrentSquare(Square.getSquare(new int[] {0, 6}));

        Eliminate.eliminateOpponents(Square.getSquare(new int[] {0, 2}), defender, attacker);
        assertTrue(!A0.isEliminated() && !A1.isEliminated() && !A2.isEliminated());
    }

    @Test
    void shieldWallNotFlushToBoardEndShouldNotEliminateAttackers() {
        // moving pieces out of the way
        Piece A3 = attacker.getPieceByID("A3");
        Piece A4 = attacker.getPieceByID("A4");
        Piece A5 = attacker.getPieceByID("A5");
        A3.setCurrentSquare(Square.getSquare(new int[] {1, 0}));
        A4.setCurrentSquare(Square.getSquare(new int[] {2, 0}));
        A5.setCurrentSquare(Square.getSquare(new int[] {1, 1}));

        // getting and moving potential eliminees
        Minion A0 = (Minion)attacker.getPieceByID("A0");
        Minion A1 = (Minion)attacker.getPieceByID("A1");
        Minion A2 = (Minion)attacker.getPieceByID("A2");
        A0.setCurrentSquare(Square.getSquare(new int[] {1, 3}));
        A1.setCurrentSquare(Square.getSquare(new int[] {1, 4}));
        A2.setCurrentSquare(Square.getSquare(new int[] {1, 5}));

        // setting up the non-flush, intact shield wall
        Piece D0 = defender.getPieceByID("D0");
        Piece D1 = defender.getPieceByID("D1");
        Piece D2 = defender.getPieceByID("D2");
        Piece D3 = defender.getPieceByID("D3");
        Piece D4 = defender.getPieceByID("D4");
        D0.setCurrentSquare(Square.getSquare(new int[] {1, 2}));
        D1.setCurrentSquare(Square.getSquare(new int[] {2, 3}));
        D2.setCurrentSquare(Square.getSquare(new int[] {2, 4}));
        D3.setCurrentSquare(Square.getSquare(new int[] {2, 5}));
        D4.setCurrentSquare(Square.getSquare(new int[] {1, 6}));

        Eliminate.eliminateOpponents(Square.getSquare(new int[] {0, 6}), defender, attacker);
        assertTrue(!A0.isEliminated() && !A1.isEliminated() && !A2.isEliminated());
    }

    @Test
    void bothFlankDefendersAndIntactFrontDefendersShouldEliminateAttackers() {
        // getting potential eliminees
        Minion A0 = (Minion)attacker.getPieceByID("A0");
        Minion A1 = (Minion)attacker.getPieceByID("A1");
        Minion A2 = (Minion)attacker.getPieceByID("A2");

        // moving pieces out of the way
        Piece A3 = attacker.getPieceByID("A3");
        Piece A4 = attacker.getPieceByID("A4");
        Piece A5 = attacker.getPieceByID("A5");
        A3.setCurrentSquare(Square.getSquare(new int[] {1, 0}));
        A4.setCurrentSquare(Square.getSquare(new int[] {2, 0}));
        A5.setCurrentSquare(Square.getSquare(new int[] {1, 1}));

        // setting up the intact shield wall
        Piece D0 = defender.getPieceByID("D0");
        Piece D1 = defender.getPieceByID("D1");
        Piece D2 = defender.getPieceByID("D2");
        Piece D3 = defender.getPieceByID("D3");
        Piece D4 = defender.getPieceByID("D4");
        D0.setCurrentSquare(Square.getSquare(new int[] {0, 2}));
        D1.setCurrentSquare(Square.getSquare(new int[] {1, 3}));
        D2.setCurrentSquare(Square.getSquare(new int[] {1, 4}));
        D3.setCurrentSquare(Square.getSquare(new int[] {1, 5}));
        D4.setCurrentSquare(Square.getSquare(new int[] {0, 6}));

        Eliminate.eliminateOpponents(Square.getSquare(new int[] {0, 6}), defender, attacker);
        assertTrue(A0.isEliminated() && A1.isEliminated() && A2.isEliminated());
    }

    @Test
    void bothFlankDefendersAndIntactFrontDefendersButNonFlankingFinishingMoveShouldNotEliminateAttackers() {
        // getting potential eliminees
        Minion A0 = (Minion)attacker.getPieceByID("A0");
        Minion A1 = (Minion)attacker.getPieceByID("A1");
        Minion A2 = (Minion)attacker.getPieceByID("A2");

        // moving pieces out of the way
        Piece A3 = attacker.getPieceByID("A3");
        Piece A4 = attacker.getPieceByID("A4");
        Piece A5 = attacker.getPieceByID("A5");
        A3.setCurrentSquare(Square.getSquare(new int[] {1, 0}));
        A4.setCurrentSquare(Square.getSquare(new int[] {2, 0}));
        A5.setCurrentSquare(Square.getSquare(new int[] {1, 1}));

        // setting up the intact shield wall
        Piece D0 = defender.getPieceByID("D0");
        Piece D1 = defender.getPieceByID("D1");
        Piece D2 = defender.getPieceByID("D2");
        Piece D3 = defender.getPieceByID("D3");
        Piece D4 = defender.getPieceByID("D4");
        D0.setCurrentSquare(Square.getSquare(new int[] {0, 2}));
        D1.setCurrentSquare(Square.getSquare(new int[] {1, 3}));
        D2.setCurrentSquare(Square.getSquare(new int[] {1, 4}));
        D3.setCurrentSquare(Square.getSquare(new int[] {1, 5}));
        D4.setCurrentSquare(Square.getSquare(new int[] {0, 6}));

        Eliminate.eliminateOpponents(Square.getSquare(new int[] {1, 4}), defender, attacker);
        assertTrue(!A0.isEliminated() && !A1.isEliminated() && !A2.isEliminated());
    }

    @Test
    void oneFlankDefenderOneRefugeSquareAndIntactFrontDefendersShouldEliminateAttackers() {
        // getting and moving eliminees
        Minion A0 = (Minion)attacker.getPieceByID("A0");
        Minion A1 = (Minion)attacker.getPieceByID("A1");
        Minion A2 = (Minion)attacker.getPieceByID("A2");
        A1.setCurrentSquare(Square.getSquare(new int[] {0, 2}));
        A2.setCurrentSquare(Square.getSquare(new int[] {0, 1}));

        // setting up the intact shield wall
        Piece D0 = defender.getPieceByID("D0");
        Piece D1 = defender.getPieceByID("D1");
        Piece D2 = defender.getPieceByID("D2");
        Piece D3 = defender.getPieceByID("D3");
        D0.setCurrentSquare(Square.getSquare(new int[] {1, 1}));
        D1.setCurrentSquare(Square.getSquare(new int[] {1, 2}));
        D2.setCurrentSquare(Square.getSquare(new int[] {1, 3}));
        D3.setCurrentSquare(Square.getSquare(new int[] {0, 4}));

        Eliminate.eliminateOpponents(Square.getSquare(new int[] {0, 4}), defender, attacker);
        assertTrue(A0.isEliminated() && A1.isEliminated() && A2.isEliminated());
    }

    @Test
    void twoIntactShieldWallsShouldEliminateTwoGroupsOfAttackers() {
        // getting and moving eliminees
        Minion A0 = (Minion)attacker.getPieceByID("A0");
        Minion A1 = (Minion)attacker.getPieceByID("A1");
        Minion A2 = (Minion)attacker.getPieceByID("A2");
        A1.setCurrentSquare(Square.getSquare(new int[] {0, 2}));
        A2.setCurrentSquare(Square.getSquare(new int[] {0, 1}));
        Minion A3 = (Minion)attacker.getPieceByID("A3");
        Minion A4 = (Minion)attacker.getPieceByID("A4");
        Minion A5 = (Minion)attacker.getPieceByID("A5");
        A5.setCurrentSquare(Square.getSquare(new int[] {0, 5}));

        // setting up two intact shield walls
        Piece D0 = defender.getPieceByID("D0");
        Piece D1 = defender.getPieceByID("D1");
        Piece D2 = defender.getPieceByID("D2");
        Piece D3 = defender.getPieceByID("D3");
        D0.setCurrentSquare(Square.getSquare(new int[] {1, 1}));
        D1.setCurrentSquare(Square.getSquare(new int[] {1, 2}));
        D2.setCurrentSquare(Square.getSquare(new int[] {1, 3}));
        D3.setCurrentSquare(Square.getSquare(new int[] {0, 4}));
        Piece D5 = defender.getPieceByID("D5");
        Piece D6 = defender.getPieceByID("D6");
        Piece D7 = defender.getPieceByID("D7");
        Piece D8 = defender.getPieceByID("D8");
        D5.setCurrentSquare(Square.getSquare(new int[] {1, 5}));
        D6.setCurrentSquare(Square.getSquare(new int[] {1, 6}));
        D7.setCurrentSquare(Square.getSquare(new int[] {1, 7}));
        D8.setCurrentSquare(Square.getSquare(new int[] {0, 8}));

        Eliminate.eliminateOpponents(Square.getSquare(new int[] {0, 4}), defender, attacker);
        assertTrue(A0.isEliminated() && A1.isEliminated() && A2.isEliminated() && A3.isEliminated() && A4.isEliminated() && A5.isEliminated());
    }
}