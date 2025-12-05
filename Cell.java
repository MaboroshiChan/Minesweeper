/**
 * Cell.java
 * This class represents a single cell in the minefield.
 * It contains information about whether the cell is revealed and its status (e.g., mine, number of adjacent mines).
 */

public class Cell {
    private boolean revealed;
    private String status;
    public Cell(boolean revealed, String status) {
        this.revealed = revealed;
        this.status = status;
    }

    public boolean getRevealed() {
        return revealed;
    }

    public void setRevealed(boolean r) { revealed = r; }


    public String getStatus() {
        return status;
    }

    public void setStatus(String c) {
        status = c;
    }

}
