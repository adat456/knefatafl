package knefatafl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    static Player attacker;
    static Player defender;

    @BeforeEach
    void init() {
        Square.createBoard();
        attacker = new Player("Attackers", "Attackers");
        defender = new Player("Defenders", "Defenders");
    }

    @Test
    void defenderPieceIDIsNotAValidAttackerPieceID() {
        assertFalse(attacker.isPieceValid("D12"));
    }

    @Test
    void incorrectLetterOfPieceIDIsNotAValidAttackerOrDefenderPieceID() {
        assertTrue(!attacker.isPieceValid("S0") && !defender.isPieceValid("S0"));
    }

    @Test
    void outOfBoundsPieceIDIsNotAValidAttackerPieceID() {
        assertFalse(attacker.isPieceValid("A24"));
    }

    @Test
    void negativePieceIDIsNotAValidDefenderPieceID() {
        assertFalse(defender.isPieceValid("D-1"));
    }

    @Test
    void attackerPieceIDIsAValidAttackerPieceID() {
        assertTrue(attacker.isPieceValid("A0"));
    }

    @Test
    void kingPieceIDIsAValidDefenderPieceID() {
        assertTrue(defender.isPieceValid("D12"));
    }

    // anyValidSquaresRemaining
    @Test
    void validSquaresRemainingForBothPlayersAtStartOfGame() {
        assertTrue(attacker.anyValidSquaresRemaining() && defender.anyValidSquaresRemaining());
    }

    @Test
    void noValidSquaresRemainingForSurroundedKingAndDefender() {
        // eliminate all defenders but one
        for (int i = 0; i < 11; i++) {
            String pieceID = String.format("D%d", i);
            ((Minion)defender.getPieceByID(pieceID)).eliminate();
        }
        // move remaining defender next to the king
        Piece D11 = defender.getPieceByID("D11");
        D11.setCurrentSquare(Square.getSquare(new int[] {6, 5}));

        Piece A0 = attacker.getPieceByID("A0");
        Piece A1 = attacker.getPieceByID("A1");
        Piece A2 = attacker.getPieceByID("A2");
        Piece A3 = attacker.getPieceByID("A3");
        Piece A4 = attacker.getPieceByID("A4");
        Piece A5 = attacker.getPieceByID("A5");
        A0.setCurrentSquare(Square.getSquare(new int[] {5, 4}));
        A1.setCurrentSquare(Square.getSquare(new int[] {6, 4}));
        A2.setCurrentSquare(Square.getSquare(new int[] {7, 5}));
        A3.setCurrentSquare(Square.getSquare(new int[] {6, 6}));
        A4.setCurrentSquare(Square.getSquare(new int[] {5, 6}));
        A5.setCurrentSquare(Square.getSquare(new int[] {4, 5}));

        assertFalse(defender.anyValidSquaresRemaining());
    }

    @Test
    void noValidSquaresRemainingForSurroundedKing() {
        // eliminate all defenders
        for (int i = 0; i < 12; i++) {
            String pieceID = String.format("D%d", i);
            ((Minion)defender.getPieceByID(pieceID)).eliminate();
        }

        Piece A0 = attacker.getPieceByID("A0");
        Piece A1 = attacker.getPieceByID("A1");
        Piece A2 = attacker.getPieceByID("A2");
        Piece A3 = attacker.getPieceByID("A3");
        A0.setCurrentSquare(Square.getSquare(new int[] {5, 4}));
        A1.setCurrentSquare(Square.getSquare(new int[] {5, 6}));
        A2.setCurrentSquare(Square.getSquare(new int[] {4, 5}));
        A3.setCurrentSquare(Square.getSquare(new int[] {6, 5}));

        assertFalse(defender.anyValidSquaresRemaining());
    }

    @Test
    void validSquareRemainingForPartiallySurroundedKing() {
        for (int i = 0; i < 12; i++) {
            String pieceID = String.format("D%d", i);
            ((Minion)defender.getPieceByID(pieceID)).eliminate();
        }

        Piece A0 = attacker.getPieceByID("A0");
        Piece A1 = attacker.getPieceByID("A1");
        Piece A2 = attacker.getPieceByID("A2");
        A0.setCurrentSquare(Square.getSquare(new int[] {5, 4}));
        A1.setCurrentSquare(Square.getSquare(new int[] {5, 6}));
        A2.setCurrentSquare(Square.getSquare(new int[] {4, 5}));

        assertTrue(defender.anyValidSquaresRemaining());
    }

    // isPossibleMoveStartOfLastThreeMoves
    @Test
    void sixthPossibleMoveCannotBeComparedAsThereAreNotEnoughMovesLogged() {
        Piece A0 = attacker.getPieceByID("A0");

        Move move1 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move2 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 3}));
        Move move3 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move4 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 3}));
        Move move5 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        attacker.addNewestMove(move1);
        attacker.addNewestMove(move2);
        attacker.addNewestMove(move3);
        attacker.addNewestMove(move4);
        attacker.addNewestMove(move5);

        Move move6 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 3}));
        assertFalse(attacker.isPossibleMoveStartOfLastThreeMoveSequences(move6));

    }

    @Test
    void seventhPossibleMoveIsAFourthRepetitionOfATwoMoveSequence() {
        Piece A0 = attacker.getPieceByID("A0");

        Move move1 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move2 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 3}));
        Move move3 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move4 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 3}));
        Move move5 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move6 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 3}));
        attacker.addNewestMove(move1);
        attacker.addNewestMove(move2);
        attacker.addNewestMove(move3);
        attacker.addNewestMove(move4);
        attacker.addNewestMove(move5);
        attacker.addNewestMove(move6);

        Move move7 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        assertTrue(attacker.isPossibleMoveStartOfLastThreeMoveSequences(move7));
    }

    @Test
    void seventhPossibleMoveIsAFourthRepetitionOfATwoMoveSequenceWithExtraMovesInBeginning() {
        Piece A0 = attacker.getPieceByID("A0");

        Move move00 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {3, 3}));
        Move move0 = new Move(A0, Square.getSquare(new int[] {3, 3}), Square.getSquare(new int[] {0, 3}));
        Move move1 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move2 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 3}));
        Move move3 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move4 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 3}));
        Move move5 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move6 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 3}));
        attacker.addNewestMove(move00);
        attacker.addNewestMove(move0);
        attacker.addNewestMove(move1);
        attacker.addNewestMove(move2);
        attacker.addNewestMove(move3);
        attacker.addNewestMove(move4);
        attacker.addNewestMove(move5);
        attacker.addNewestMove(move6);

        Move move7 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        assertTrue(attacker.isPossibleMoveStartOfLastThreeMoveSequences(move7));
    }

    @Test
    void seventhPossibleMoveIsNotAFourthRepetitionOfATwoMoveSequenceBecauseItIsDifferentFromTheFirstMoveOfTheSequence() {
        Piece A0 = attacker.getPieceByID("A0");

        Move move1 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move2 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 3}));
        Move move3 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move4 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 3}));
        Move move5 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move6 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 3}));
        attacker.addNewestMove(move1);
        attacker.addNewestMove(move2);
        attacker.addNewestMove(move3);
        attacker.addNewestMove(move4);
        attacker.addNewestMove(move5);
        attacker.addNewestMove(move6);

        Move move7 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 2}));
        assertFalse(attacker.isPossibleMoveStartOfLastThreeMoveSequences(move7));
    }

    @Test
    void seventhPossibleMoveIsNotAFourthRepetitionOfAFalseTwoMoveSequenceBecauseFirstMoveOfFirstSequenceIsDifferent() {
        Piece A0 = attacker.getPieceByID("A0");

        Move move1 = new Move(A0, Square.getSquare(new int[] {0, 2}), Square.getSquare(new int[] {0, 1}));
        Move move2 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 3}));
        Move move3 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move4 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 3}));
        Move move5 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move6 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 3}));
        attacker.addNewestMove(move1);
        attacker.addNewestMove(move2);
        attacker.addNewestMove(move3);
        attacker.addNewestMove(move4);
        attacker.addNewestMove(move5);
        attacker.addNewestMove(move6);

        Move move7 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        assertFalse(attacker.isPossibleMoveStartOfLastThreeMoveSequences(move7));
    }

    @Test
    void tenthPossibleMoveIsAFourthRepetitionOfAThreeMoveSequence() {
        Piece A0 = attacker.getPieceByID("A0");

        Move move1 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move2 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 2}));
        Move move3 = new Move(A0, Square.getSquare(new int[] {0, 2}), Square.getSquare(new int[] {0, 3}));
        Move move4 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move5 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 2}));
        Move move6 = new Move(A0, Square.getSquare(new int[] {0, 2}), Square.getSquare(new int[] {0, 3}));
        Move move7 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move8 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 2}));
        Move move9 = new Move(A0, Square.getSquare(new int[] {0, 2}), Square.getSquare(new int[] {0, 3}));
        attacker.addNewestMove(move1);
        attacker.addNewestMove(move2);
        attacker.addNewestMove(move3);
        attacker.addNewestMove(move4);
        attacker.addNewestMove(move5);
        attacker.addNewestMove(move6);
        attacker.addNewestMove(move7);
        attacker.addNewestMove(move8);
        attacker.addNewestMove(move9);

        Move move10 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        assertTrue(attacker.isPossibleMoveStartOfLastThreeMoveSequences(move10));
    }

    @Test
    void tenthPossibleMoveIsNotAFourthRepetitionOfAThreeMoveSequenceBecauseItIsDifferentFromFirstMoveOfSequence() {
        Piece A0 = attacker.getPieceByID("A0");

        Move move1 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move2 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 2}));
        Move move3 = new Move(A0, Square.getSquare(new int[] {0, 2}), Square.getSquare(new int[] {0, 3}));
        Move move4 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move5 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 2}));
        Move move6 = new Move(A0, Square.getSquare(new int[] {0, 2}), Square.getSquare(new int[] {0, 3}));
        Move move7 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move8 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 2}));
        Move move9 = new Move(A0, Square.getSquare(new int[] {0, 2}), Square.getSquare(new int[] {0, 3}));
        attacker.addNewestMove(move1);
        attacker.addNewestMove(move2);
        attacker.addNewestMove(move3);
        attacker.addNewestMove(move4);
        attacker.addNewestMove(move5);
        attacker.addNewestMove(move6);
        attacker.addNewestMove(move7);
        attacker.addNewestMove(move8);
        attacker.addNewestMove(move9);

        Move move10 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 2}));
        assertFalse(attacker.isPossibleMoveStartOfLastThreeMoveSequences(move10));
    }

    @Test
    void tenthPossibleMoveIsNotAFourthRepetitionOfThreeMoveSequenceBecauseThereAreOnlyTwoRepetitionsFollowedByDifferentSequence() {
        Piece A0 = attacker.getPieceByID("A0");

        Move move1 = new Move(A0, Square.getSquare(new int[]{0, 3}), Square.getSquare(new int[]{0, 1}));
        Move move2 = new Move(A0, Square.getSquare(new int[]{0, 1}), Square.getSquare(new int[]{0, 2}));
        Move move3 = new Move(A0, Square.getSquare(new int[]{0, 2}), Square.getSquare(new int[]{0, 3}));
        Move move4 = new Move(A0, Square.getSquare(new int[]{0, 3}), Square.getSquare(new int[]{0, 1}));
        Move move5 = new Move(A0, Square.getSquare(new int[]{0, 1}), Square.getSquare(new int[]{0, 2}));
        Move move6 = new Move(A0, Square.getSquare(new int[]{0, 2}), Square.getSquare(new int[]{0, 3}));
        Move move7 = new Move(A0, Square.getSquare(new int[]{0, 3}), Square.getSquare(new int[]{0, 2}));
        Move move8 = new Move(A0, Square.getSquare(new int[]{0, 2}), Square.getSquare(new int[]{0, 1}));
        Move move9 = new Move(A0, Square.getSquare(new int[]{0, 1}), Square.getSquare(new int[]{0, 3}));
        attacker.addNewestMove(move1);
        attacker.addNewestMove(move2);
        attacker.addNewestMove(move3);
        attacker.addNewestMove(move4);
        attacker.addNewestMove(move5);
        attacker.addNewestMove(move6);
        attacker.addNewestMove(move7);
        attacker.addNewestMove(move8);
        attacker.addNewestMove(move9);

        Move move10 = new Move(A0, Square.getSquare(new int[]{0, 3}), Square.getSquare(new int[]{0, 1}));
        assertFalse(attacker.isPossibleMoveStartOfLastThreeMoveSequences(move10));
    }

    @Test
    void tenthPossibleMoveIsNotAFourthRepetitionOfThreeMoveSequenceBecauseThereAreOnlyTwoRepetitionsInterruptedByDifferentSequence() {
        Piece A0 = attacker.getPieceByID("A0");

        Move move1 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move2 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 2}));
        Move move3 = new Move(A0, Square.getSquare(new int[] {0, 2}), Square.getSquare(new int[] {0, 3}));
        Move move4 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 2}));
        Move move5 = new Move(A0, Square.getSquare(new int[] {0, 2}), Square.getSquare(new int[] {0, 1}));
        Move move6 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 3}));
        Move move7 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        Move move8 = new Move(A0, Square.getSquare(new int[] {0, 1}), Square.getSquare(new int[] {0, 2}));
        Move move9 = new Move(A0, Square.getSquare(new int[] {0, 2}), Square.getSquare(new int[] {0, 3}));
        attacker.addNewestMove(move1);
        attacker.addNewestMove(move2);
        attacker.addNewestMove(move3);
        attacker.addNewestMove(move4);
        attacker.addNewestMove(move5);
        attacker.addNewestMove(move6);
        attacker.addNewestMove(move7);
        attacker.addNewestMove(move8);
        attacker.addNewestMove(move9);

        Move move10 = new Move(A0, Square.getSquare(new int[] {0, 3}), Square.getSquare(new int[] {0, 1}));
        assertFalse(attacker.isPossibleMoveStartOfLastThreeMoveSequences(move10));
    }
}