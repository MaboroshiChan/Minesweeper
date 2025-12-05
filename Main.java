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

public class Main{

    public static void main(String[] args){

        Random rand = new Random();

        System.out.println("Welcome to Minesweeper!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select a mode: (beginner, intermediate, expert, or debug)");
        int rows = 20;
        int columns = 20;
        int mines = rand.nextInt(20) + 1; // Random number of mines between 1 and 20
        boolean debug = false;
        Minefield minefield = new Minefield(rows, columns, mines);

        /** 
        while(!minefield.gameOver()){
            int revealRow, revealCol;
            // user input position to reveal
            System.out.println("Enter row and column to reveal (e.g., '3 4'):");
            revealRow = scanner.nextInt();
            revealCol = scanner.nextInt();
            scanner.nextLine(); // consume the newline character
            if (revealRow < 0 || revealRow >= rows || revealCol < 0 || revealCol >= columns) {
                System.out.println("Invalid position. Please try again.");
                continue;
            }
            // reveal the cell
            minefield.revealZeroes(revealRow, revealCol);
            // print the current state of the minefield
            System.out.println(minefield.toString());
            // check if the cell is a mine

        }
       System.out.println("test println");
       */
    }
}
