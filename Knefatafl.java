package knefatafl;

import java.util.Scanner;

public class Knefatafl {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Starting new Knefatafl game...");

        // gathering input
        System.out.print("Attackers' name: ");
        String attackersName = input.nextLine();
        System.out.print("Defenders' name: ");
        String defendersName = input.nextLine();

        System.out.print("Move time limit (in seconds): ");
        int turnTimeLimit = input.nextInt();
        input.nextLine();

        Move.determineMoveLimit(input);

        // setting up Game, Board, Players, and Pieces
        Square.createBoard();
        Player attacker = new Player(attackersName, "Attackers");
        Player defender = new Player(defendersName, "Defenders");
        Game game = new Game(attacker, defender, turnTimeLimit);

        while ((Move.getTotalMoves() < Move.getMoveLimit()) && game.getVictor() == null) {
            Player currentPlayer = game.getCurrentPlayer();
            String pieceID = "";

            boolean validPiece = false;
            while (!validPiece) {
                System.out.print("Select piece: ");
                pieceID = input.next();
                input.nextLine();

                validPiece = currentPlayer.isPieceValid(pieceID.trim());
            }  

            boolean moveMade = false;
            while (!moveMade) {
                System.out.print("Move piece " + pieceID + " to square: ");
                int x = input.nextInt();
                int y = input.nextInt();
                int[] coords = {x, y};

                Piece piece = currentPlayer.getPiece(pieceID);
                Square startingSquare = piece.getCurrentSquare();
                Square endingSquare = Square.getSquare(coords);

                if (Move.isMoveValid(game, piece, startingSquare, endingSquare)) {
                    currentPlayer.addNewestMove(piece, startingSquare, endingSquare);
                    piece.setCurrentSquare(endingSquare);
                    moveMade = true;
                }
            }
            game.switchTurn();
        }

        input.close();
    }
}