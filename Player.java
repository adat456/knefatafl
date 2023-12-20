package knefatafl;

import java.util.ArrayList;
import java.util.Arrays;

public class Player {
    private String name;
    private String team;
    private ArrayList<Move> allMoves = new ArrayList<>();
    private ArrayList<Piece> allPieces = new ArrayList<>();

    public Player(String name, String team) {
        this.name = name;
        this.team = team;
        createPieces(team);
    } 

    private void createPieces(String team) {
        if (team.equals("Attackers")) {
            int[][] attackerCoords = {{0, 3}, {0, 4}, {0, 5}, {0, 6}, {0, 7}, {1, 5}, {10, 3}, {10, 4}, {10, 5}, {10, 6}, {10, 7}, {9, 5}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0}, {5, 1}, {3, 10}, {4, 10}, {5, 10}, {6, 10}, {7, 10}, {5, 9}};
            for (int i = 0; i < 24; i++) {
                allPieces.add(new Minion(String.format("%s%d", "A", i), Square.getSquare(attackerCoords[i])));
                Square.getSquare(attackerCoords[i]).toggleOccupied();
            }
        } else if (team.equals("Defenders")) {
            int[][] defenderCoords = {{5, 3}, {4, 4}, {5, 4}, {6, 4}, {3, 5}, {4, 5}, {6, 5}, {7, 5}, {4, 6}, {5, 6}, {6, 6}, {5, 7}};
            for (int i = 0; i < 12; i++) {
                allPieces.add(new Minion(String.format("%s%d", "D", i), Square.getSquare(defenderCoords[i])));
                Square.getSquare(defenderCoords[i]).toggleOccupied();
            }
            allPieces.add(new King(Square.getSquare(new int[] {5, 5})));
        }
    }

    public boolean isPieceValid(String pieceID) {
        String pieceStart = pieceID.substring(0, 1);
        int pieceIndex = Integer.parseInt(pieceID.substring(1));
        if (!pieceStart.equals(team.substring(0, 1)) || pieceIndex > allPieces.size() - 1 || pieceIndex < 0) {
            System.out.println("This piece is not part of this player's team.");
            return false;
        }

        Piece piece = allPieces.get(pieceIndex);
        if (piece instanceof Minion) {
            if (((Minion)piece).isEliminated()) {
                System.out.println("This piece has been eliminated.");
                return false;
            }
        } 

        return true;
    }

    public Piece getPieceByID(String pieceID) {
        int pieceIndex = Integer.parseInt(pieceID.substring(1));
        return allPieces.get(pieceIndex);
    }

    public ArrayList<Piece> getPieceBySquare(Square square) {
        ArrayList<Piece> targetPiece = new ArrayList<>(1);
        for (Piece currentPiece : allPieces) {
            if (currentPiece.getCurrentSquare().equals(square)) {
                targetPiece.add(currentPiece);
            }
        }
        return targetPiece;
    }

    public boolean anyValidSquaresRemaining() {
        boolean validSquaresRemaining = false;

        for (Piece piece : allPieces) {
            if (piece instanceof Minion) {
                if (!((Minion)piece).isEliminated() && !piece.findAllValidSquares().isEmpty()) validSquaresRemaining = true;
            } else if (piece instanceof King) {
                if (!piece.findAllValidSquares().isEmpty()) {
                    validSquaresRemaining = true;
                }
            }

            if (validSquaresRemaining) break;
        }

        return validSquaresRemaining;
    }

    // attempts to find repetitive sequences up to 5 moves long
    public boolean isPossibleMoveStartOfLastThreeMoveSequences(Move possibleMove) {
        // minimum number of moves in a repetitive sequence is 2; 2 * 3 = 6, so if the total number of moves is 5 or fewer, the sixth/possible move will finish the third repetition, which is fine
        if (allMoves.size() <= 5) return false;

        Move potentialStartOfSequence = null;
        int potentialLengthOfSequence = 0;

        // looking for a match between the possible move and the start of a sequence (so it starts with the second to most recent move)
        for (int i = 2; i <= 6; i++) {
            if (possibleMove.equals(allMoves.get(allMoves.size() - i))) {
                potentialStartOfSequence = allMoves.get(allMoves.size() - i);
                potentialLengthOfSequence = i;
                break;
            }
        }

        if (potentialLengthOfSequence == 0 || potentialStartOfSequence == null) return false;
        // safeguards against false sequences
        if (potentialLengthOfSequence * 3 > allMoves.size()) return false;

        boolean allThreeSequencesAreTheSame = true;
        // moves forward from the first/starting move of each repetition to ensure that subsequent moves are equal as well
        // counter is initialized to 0 because the possible move has only been verified to equal the first/starting move of the third repetition (not the second and third)
        // e.g., in a list of six moves (with the potential move being the seventh)
            // the first loop would compare move 0, move 2, and possible move (move 4 has already been compared)
            // the second loop would compare move 1, move 3, and move 5
        for (int j = 0; j < potentialLengthOfSequence; j++) {
            if (j == 0) {
                if (
                     !possibleMove.equals(allMoves.get(allMoves.size() - (potentialLengthOfSequence * 2))) ||
                     !possibleMove.equals(allMoves.get(allMoves.size() - (potentialLengthOfSequence * 3)))
                ) {
                    allThreeSequencesAreTheSame = false;
                    break;
                }
            } else {
                if (
                    !allMoves.get(allMoves.size() - (potentialLengthOfSequence) + j).equals(allMoves.get(allMoves.size() - (potentialLengthOfSequence * 2) + j)) ||
                    !allMoves.get(allMoves.size() - (potentialLengthOfSequence) + j).equals(allMoves.get(allMoves.size() - (potentialLengthOfSequence * 3) + j))
                ) {
                    allThreeSequencesAreTheSame = false;
                    break;
                }
            }

        }
        return allThreeSequencesAreTheSame;
    }

    public void addNewestMove(Move newMove) { allMoves.add(newMove); }
    public String getTeam() { return team; }
    public String getName() { return name; }
}
