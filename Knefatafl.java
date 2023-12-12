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
                System.out.print("First move: ");
                int x = input.nextInt();
                int y = input.nextInt();
                int[] coords = {x, y};

                try {
                    currentPlayer.attemptMove(currentPlayer.getPiece(pieceID), coords);
                    moveMade = true;
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            game.switchTurn();
        }

        input.close();
    }
}