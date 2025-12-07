
//Import Section
import java.util.Random;
import java.util.Scanner;

/*
 * Provided in this class is the neccessary code to get started with your game's implementation
 * You will find a while loop that should take your minefield's gameOver() method as its conditional
 * Then you will prompt the user with input and manipulate the data as before in project 2
 * 
 * Things to Note:
 * 1. Think back to Project 1 when we asked our user to give a shape. In this project we will be asking the user to provide a mode. Then create a minefield accordingly
 * 2. You must implement a way to check if we are playing in debug mode or not.
 * 3. When working inside your while loop think about what happens each turn. We get input, user our methods, check their return values. repeat.
 * 4. Once while loop is complete figure out how to determine if the user won or lost. Print appropriate statement.
 */

public class Main {

    public static void main(String[] args) {

        Random rand = new Random();

        System.out.println("Welcome to Minesweeper!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select a mode: (beginner, intermediate, expert, or debug)");
        int rows = 10;
        int columns = 10;
        int mines = 5; // Random number of mines between 1 and 20
        boolean debug = false;
        Minefield minefield = new Minefield(rows, columns, mines);

        //minefield.revealStartingArea(rows / 2, columns / 2);

        // 主游戏循环
        while (!minefield.gameOver()) {

            System.out.println("Enter your move:");
            System.out.println("Format: 'row column' to reveal a cell");
            System.out.println("Format: 'row column flag' to place/remove a flag");

            String input = scanner.nextLine();
            String[] parts = input.split(" ");

            if (parts.length < 2) {
                System.out.println("Invalid input. Please enter at least row and column.");
                continue;
            }

            try {
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                boolean isFlag = (parts.length >= 3 && parts[2].equalsIgnoreCase("flag"));

                if (row < 0 || row >= rows || col < 0 || col >= columns) {
                    System.out.println("Invalid position. Please try again.");
                    continue;
                }

                boolean hitMine = minefield.guess(row, col, isFlag);

                if (hitMine && !isFlag) {
                    System.out.println("BOOM! You hit a mine!");
                    if (debug) {
                        minefield.debug();
                    } else {
                        System.out.println(minefield.toString());
                    }
                    break;
                }

                if (!isFlag) {
                    System.out.println("ready to reveal");
                    minefield.revealZeroes(row, col);
                }

                System.out.println("Current minefield:");
                if (debug) {
                    minefield.debug();
                } else {
                    System.out.println(minefield.toString());
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid number format.");
            }
        }

        scanner.close();
    }
}