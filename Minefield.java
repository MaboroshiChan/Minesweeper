
// Import Section
import java.util.Queue;
import java.util.Random;

public class Minefield {
    /**
     * Global Section
     */
    public static final String ANSI_YELLOW_BRIGHT = "\u001B[33;1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001b[47m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001b[45m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";

    /*
     * Class Variable Section
     * 
     */
    private Cell[][] field;

    private int rows;
    private int columns;

    private int mines;
    private int flags;
    private int revealedCells;
    private boolean debugMode;
    private Random rand = new Random();

    /*
     * Things to Note:
     * Please review ALL files given before attempting to write these functions.
     * Understand the Cell.java class to know what object our array contains and
     * what methods you can utilize
     * Understand the StackGen.java interface to know what type of stack you will be
     * working with and methods you can utilize
     * Understand the QGen.java interface to know what type of queue you will be
     * working with and methods you can utilize
     */

    /**
     * Minefield
     * 
     * Build a 2-d Cell array representing your minefield.
     * Constructor
     * 
     * @param rows    Number of rows.
     * @param columns Number of columns.
     * @param flags   Number of flags, should be equal to mines
     */
    public Minefield(int rows, int columns, int flags) {
        this.rows = rows;
        this.columns = columns;
        this.mines = flags;
        this.flags = flags;
        this.revealedCells = 0;
        this.debugMode = true;

        field = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                field[i][j] = new Cell(false, " ");
            }
        }

        createMines(-1, -1, this.mines);
        evaluateField();
        // If debug mode is enabled, print the minefield
        if (debugMode) {
            debug();    
        }
    }

    /**
     * evaluateField
     * 
     * 
     * @function:
     *            Evaluate entire array.
     *            When a mine is found check the surrounding adjacent tiles. If
     *            another mine is found during this check, increment adjacent cells
     *            status by 1.
     * 
     */
    public void evaluateField() {
        // Would this method be best called after createMines()?
        //
         for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (field[i][j].getStatus().equals("M")) {
                    // Increment adjacent cells
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            int newX = i + x;
                            int newY = j + y;
                            if (newX >= 0 && newX < rows && newY >= 0 && newY < columns) {
                                if (!field[newX][newY].getStatus().equals("M")) {
                                    String currentStatus = field[newX][newY].getStatus();
                                    int currentCount = currentStatus.equals(" ") ? 0 : Integer.parseInt(currentStatus);
                                    field[newX][newY].setStatus(Integer.toString(currentCount + 1));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * createMines
     * 
     * Randomly generate coordinates for possible mine locations.
     * If the coordinate has not already been generated and is not equal to the
     * starting cell set the cell to be a mine.
     * utilize rand.nextInt()
     * 
     * @param x     Start x, avoid placing on this square.
     * @param y     Start y, avoid placing on this square.
     * @param mines Number of mines to place.
     */
    public void createMines(int x, int y, int mines) {
        for (int i = 0; i < mines;) {
            int randX = rand.nextInt(rows);
            int randY = rand.nextInt(columns);
            if (!(randX == x && randY == y)
                    && !field[randX][randY].getStatus().equals("M") // avoid placing multiple mines in same spot
            ) {
                field[randX][randY].setStatus("M");
                i++;
            }
        }
    }

    /**
     * guess
     * 
     * Check if the guessed cell is inbounds (if not done in the Main class).
     * Either place a flag on the designated cell if the flag boolean is true or
     * clear it.
     * If the cell has a 0 call the revealZeroes() method or if the cell has a mine
     * end the game.
     * At the end reveal the cell to the user.
     * 
     * 
     * @param x    The x value the user entered.
     * @param y    The y value the user entered.
     * @param flag A boolean value that allows the user to place a flag on the
     *             corresponding square.
     * @return boolean Return false if guess did not hit mine or if flag was placed,
     *         true if mine found.
     */
    public boolean guess(int x, int y, boolean flag) {
        Cell cell = field[x][y];
        if (flag) {
            if (flags > 0 && !cell.getRevealed()) {
                cell.setStatus("F");
                flags--;
            }
            return false;
        } else {
            if (cell.getStatus().equals("M")) {
                cell.setRevealed(true);
                return true; // hit a mine
            } else if (cell.getStatus().equals("0")) {
                revealZeroes(x, y);
            } else {
                cell.setRevealed(true);
                revealedCells++;
            }
        }
        return false;
    }

    /**
     * gameOver
     * 
     * Ways a game of Minesweeper ends:
     * 1. player guesses a cell with a mine: game over -> player loses
     * 2. player has revealed the last cell without revealing any mines -> player
     * wins
     * 
     * @return boolean Return false if game is not over and squares have yet to be
     *         revealed, otherwise return true.
     */
    public boolean gameOver() {
        // Check if all cells are revealed or if a mine was hit
        if (revealedCells + mines == rows * columns) {
            System.out.println("Congratulations! You've cleared the minefield!");
            return true; // Player wins
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = field[i][j];
                if (cell.getRevealed() && cell.getStatus().equals("M")) {
                    System.out.println("Game Over! You hit a mine!");
                    return true; // Player loses
                }
            }
        }
        return false; // Game is still ongoing
    }

    /**
     * Reveal the cells that contain zeroes that surround the inputted cell.
     * Continue revealing 0-cells in every direction until no more 0-cells are found
     * in any direction.
     * Utilize a STACK to accomplish this.
     * 
     * I think a stack would be used here to implement a depth-first search (DFS)
     * approach, allowing us to explore as far as possible along each branch before
     * backtracking.
     *
     * This method should follow the psuedo-code given in the lab writeup.
     * Why might a **stack** be useful here rather than a **queue**?
     *
     * @param x The x value the user entered.
     * @param y The y value the user entered.
     */
    public void revealZeroes(int x, int y) {
        Stack1Gen<int[]> stack = new Stack1Gen<>();
        stack.push(new int[]{x,y});

        Boolean[][] visited = new Boolean[this.columns][this.rows];

        while (!stack.isEmpty()){
            int[] pos = stack.pop();
            int cx = pos[0];
            int cy = pos[1];
            Cell cell = field[cx][cy];
            // traverse its surrounding
            if(cx + 1 < this.rows && // unvisited
               !visited[cx+1][cy] && !field[cx+1][cy].getStatus().equals('M')
            ) {
                visited[cx+1][cy] = true;
                stack.push(new int[]{cx+1,cy});

            }
            if(cx - 1 >= 0 && !visited[cx -1][cy] && ! field[cx-1][cy].getStatus().equals('M')) {
                visited [cx-1][cy] = true;
                stack.push(new int[]{cx-1,cy});

            }
            if(cy + 1 < this.columns && !visited[cx][cy +1] && ! field[cx][cy-1].getStatus().equals('M')) {
                visited [cx][cy+1] = true;
                stack.push(new int[]{cx,cy+1});
            }
            if(cy - 1 >= 0 && !visited[cx][cy -1] && ! field[cx][cy-1].getStatus().equals('M')) {
                visited [cx][cy-1] = true;
                stack.push(new int[]{cx, cy-1});
            }
        }
    }

    /**
     * revealStartingArea
     *
     * On the starting move only reveal the neighboring cells of the initial cell
     * and continue revealing the surrounding concealed cells until a mine is found.
     * Utilize a QUEUE to accomplish this.
     * 
     * This method should follow the psuedo-code given in the lab writeup.
     * Why might a queue be useful for this function?
     * 
     * Looks like a breadth-first search (BFS) approach would be appropriate here,
     * allowing us to explore all neighbors at the present depth prior to moving on
     * to nodes at the next depth level.
     *
     * @param x The x value the user entered.
     * @param y The y value the user entered.
     */
    public void revealStartingArea(int x, int y) {
        Q1Gen<int[]> queue = new Q1Gen<>();
        queue.add(new int[]{x,y});

        Boolean[][] visited = new Boolean[this.columns][this.rows];

        while (!(queue.length() == 0)){
            int[] pos = queue.remove();
            int cx = pos[0];
            int cy = pos[1];
            Cell cell = field[cx][cy];
            // traverse its surrounding
            if(cx + 1 < this.rows && // unvisited
               !visited[cx+1][cy] && !field[cx+1][cy].getStatus().equals('M')
            ) {
                visited[cx+1][cy] = true;
                queue.add(new int[]{cx+1,cy});

            }
            if(cx - 1 >= 0 && !visited[cx -1][cy] && ! field[cx-1][cy].getStatus().equals('M')) {
                visited [cx-1][cy] = true;
                queue.add(new int[]{cx-1,cy});

            }
            if(cy + 1 < this.columns && !visited[cx][cy +1] && ! field[cx][cy-1].getStatus().equals('M')) {
                visited [cx][cy+1] = true;
                queue.add(new int[]{cx,cy+1});
            }
            if(cy - 1 >= 0 && !visited[cx][cy -1] && ! field[cx][cy-1].getStatus().equals('M')) {
                visited [cx][cy-1] = true;
                queue.add(new int[]{cx, cy-1});
            }
        }
    }

    /**
     * For both printing methods utilize the ANSI color codes provided!
     * 
     * 
     * 
     * 
     * 
     * debug
     *
     * @function This method should print the entire minefield, regardless if the
     *           user has guessed a square.
     *           This method should print out when debug mode has been selected. It
     *           is very similar to the toString method below.
     */
    public void debug() {
        // Print the minefield when evaluated the field
        // each cell should be assigned a value or a mine
        System.out.println(ANSI_YELLOW_BRIGHT + "Debug Mode: Minefield State" + ANSI_GREY_BACKGROUND);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = field[i][j];
                if (cell.getStatus().equals("M")) {
                    System.out.print(ANSI_RED + "[M]" + ANSI_GREY_BACKGROUND);
                } else {
                    String status = cell.getStatus();
                    if(status.equals(" ")){
                        System.out.print(ANSI_GREEN + "[ ]" + ANSI_GREY_BACKGROUND);
                    }
                    else {
                        System.out.print(ANSI_YELLOW + "[" + status + "]" + ANSI_GREY_BACKGROUND);
                    }
                }
            }
            System.out.println();
        }
    }

    /**
     * toString
     *
     * @return String The string that is returned only has the squares that has been
     *         revealed to the user or that the user has guessed.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = field[i][j];
                if (cell.getStatus().equals("M") && cell.getRevealed()) {
                    sb.append(ANSI_RED).append("[M]").append(ANSI_GREY_BACKGROUND);
                }
                else if (cell.getRevealed()) {
                    // append status with color coding
                    sb.append(ANSI_YELLOW);
                    sb.append("[").append(cell.getStatus()).append("]");
                    sb.append(ANSI_GREY_BACKGROUND);
                } else {
                    sb.append(ANSI_GREEN).append("[ ]").append(ANSI_GREY_BACKGROUND);
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
