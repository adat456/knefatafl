package knefatafl;

public class Game {
    private int turnTimeLimit;
    private Player currentPlayer;
    private Player attacker;
    private Player defender;
    private Player victor;

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

    public void switchTurn () {
        if (currentPlayer.getTeam().equals("Attackers")) {
            currentPlayer = defender;
        } else {
            currentPlayer = defender;
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
