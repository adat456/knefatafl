package knefatafl;

import java.util.Arrays;

public class Square {
    private int[] coords;
    private boolean occupied = false;
    private boolean refuge;
    private boolean hostile;
    private static Square[][] board = new Square[11][11];

    public Square(int[] coords) {
        this.coords = coords;
    }

    public Square(int[] coords, boolean refuge, boolean hostile) {
        this.coords = coords;
        this.refuge = refuge;
        this.hostile = hostile;
    }

    public static void createBoard() {
        Square[][] newBoard = new Square[11][11];

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if ((i == 0 || i == 10) && (j == 0 || j == 10)) {
                    newBoard[i][j] = new Square(new int[] {i, j}, true, false);
                } else if (i == 5 && j == 5) {
                    newBoard[i][j] = new Square(new int[] {i, j}, false, true);
                } else {
                    newBoard[i][j] = new Square(new int[] {i, j}, false, false);
                }
            }
        }
        board = newBoard;
    }

    public static int[] changeCoords(int[] coords, String axis, String operation) {
        int[] newCoords = new int[2];
        if (axis.equals("x")) {
            if (operation.equals("+")) {
                newCoords[0] = coords[0] + 1;
                newCoords[1] = coords[1];
            } else if (operation.equals("-")) {
                newCoords[0] = coords[0] - 1;
                newCoords[1] = coords[1];
            }
        } else if (axis.equals("y")) {
            if (operation.equals("+")) {
                newCoords[0] = coords[0];
                newCoords[1] = coords[1] + 1;
            } else if (operation.equals("-")) {
                newCoords[0] = coords[0];
                newCoords[1] = coords[1] - 1;
            }
        }
        return newCoords;
    }

    public static boolean squareExists(int[] coords) {
        int x = coords[0];
        int y = coords[1];
        return !(x < 0 || x > 10 || y < 0 || y > 10);
    }

    public static Square getSquare(int[] coords) throws IllegalArgumentException {
        int x = coords[0];
        int y = coords[1];

        if (x < 0 || x > 10 || y < 0 || y > 10) 
            throw new IllegalArgumentException("This square falls outside the bounds of the board.");
        
        return board[x][y];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return Arrays.equals(coords, square.getCoords());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coords);
    }

    public void toggleOccupied() {
        occupied = !occupied;
    }
    public int[] getCoords() {
        return coords;
    }
    public boolean isOccupied() {
        return occupied;
    }
    public boolean isRefuge() {
        return refuge;
    }
    public boolean isHostile() {
        return hostile;
    }
}
