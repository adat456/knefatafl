package knefatafl;

import java.util.ArrayList;

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
            }
        } else if (team.equals("Defenders")) {
            int[][] defenderCoords = {{5, 3}, {4, 4}, {5, 4}, {6, 4}, {3, 5}, {4, 5}, {6, 5}, {7, 5}, {4, 6}, {5, 6}, {6, 6}, {5, 7}};
            for (int i = 0; i < 12; i++) {
                allPieces.add(new Minion(String.format("%s%d", "D", i), Square.getSquare(defenderCoords[i])));
            }
            allPieces.add(new King(Square.getSquare(new int[] {5, 5})));
        }
    }

    public boolean isPieceValid(String pieceID) {
        String pieceStart = pieceID.substring(0, 1);
        int pieceIndex = Integer.parseInt(pieceID.substring(1));
        if (!pieceStart.equals(team.substring(0, 1)) || pieceIndex > allPieces.size() || pieceIndex < 0) {
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

    public Piece getPiece(String pieceID) {
        int pieceIndex = Integer.parseInt(pieceID.substring(1));
        return allPieces.get(pieceIndex);
    }

    public void attemptMove(Piece piece, int[] coords) throws IllegalArgumentException {
        try {
            Square currentSquare = piece.getCurrentSquare();
            Square endingSquare = Square.getSquare(coords);

            Move.validate(piece, currentSquare, endingSquare);
            addNewestMove(piece, currentSquare, endingSquare);
            piece.setCurrentSquare(endingSquare);
        } catch (IllegalArgumentException ex) {
            throw ex;
        }
    }

    private void addNewestMove(Piece piece, Square currentSquare, Square endingSquare) {
        Move newMove = new Move(piece, currentSquare, endingSquare);
        allMoves.add(newMove);
    }

    public String getTeam() {
        return team;
    }
}
