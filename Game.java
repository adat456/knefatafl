package knefatafl;

public class Game {
    private int turnTimeLimit;
    private Player currentPlayer;
    private Player attacker;
    private Player defender;
    private Player victor;
    private String errorMessage;

    public Game(Player attacker, Player defender) {
        this.attacker = attacker;
        this.defender = defender;
    }

    public Game(Player attacker, Player defender, int turnTimeLimit) {
        this.attacker = attacker;
        this.currentPlayer = attacker;
        this.defender = defender;
        this.turnTimeLimit = turnTimeLimit;
    }

    public void displayErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        System.out.println(errorMessage);
    }

    public void switchTurn () {
        if (currentPlayer.getTeam().equals("Attackers")) {
            currentPlayer = defender;
        } else {
            currentPlayer = attacker;
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setVictor(Player victor) {
        this.victor = victor;
    }

    public Player getVictor() {
        return victor;
    }

    public void displayVictor() {

    }
}
