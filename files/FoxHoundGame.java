import java.io.IOException;
import java.util.Scanner;
import java.nio.file.Path;

/** 
 * The Main class of the fox hound program.
 * 
 * It contains the main game loop where main menu interactions
 * are processed and handler functions are called.
  */
public class FoxHoundGame {

    /** 
     * This scanner can be used by the program to read from
     * the standard input. 
     * 
     * Every scanner should be closed after its use, however, if you do
     * that for StdIn, it will close the underlying input stream as well
     * which makes it difficult to read from StdIn again later during the
     * program.
     * 
     * Therefore, it is advisable to create only one Scanner for StdIn 
     * over the course of a program and only close it when the program
     * exits. Additionally, it reduces complexity. 
     */
    private static final Scanner STDIN_SCAN = new Scanner(System.in);

    /**
     * Swap between fox and hounds to determine the next
     * figure to move.
     * 
     * @param currentTurn last figure to be moved
     * @return next figure to be moved
     */
    private static char swapPlayers(char currentTurn) {
        if (currentTurn == FoxHoundUtils.FOX_FIELD) {
            return FoxHoundUtils.HOUND_FIELD;
        } else {
            return FoxHoundUtils.FOX_FIELD;
        }
    }

    /**
     * The main loop of the game. Interactions with the main
     * menu are interpreted and executed here.
     * 
     * @param dim the dimension of the game board
     * @param players current position of all figures on the board in board coordinates
     */
    private static void gameLoop(int dim, String[] players) {

        // start each game with the Fox
        char turn = FoxHoundUtils.FOX_FIELD;
        boolean exit = false;
        while(!exit) {
            System.out.println("\n#################################");
            FoxHoundUI.displayBoard(players, dim);

            int choice = FoxHoundUI.mainMenuQuery(turn, STDIN_SCAN);
            
            // handle menu choice
            switch(choice) {
                case FoxHoundUI.MENU_MOVE:
                    // Ask for coordinates
                    boolean true_move = false;
                    while (!true_move) {
                        String[] coords = FoxHoundUI.positionQuery(dim, STDIN_SCAN);
                        if (FoxHoundUtils.isValidMove(dim, players, turn, coords[0], coords[1])) {
                            if (turn == FoxHoundUtils.FOX_FIELD) {
                                players[players.length - 1] = coords[1];
                            }
                            else if (turn == FoxHoundUtils.HOUND_FIELD) {
                                for (int i = 0; i < players.length; i++) {
                                    if (players[i].equals(coords[0])) {
                                        players[i] = coords[1];
                                        break;
                                    }
                                }
                            }
                            true_move = true;
                        }
                        else {System.err.println("ERROR: The move is invalid");}
                    }
                    if (FoxHoundUtils.isFoxWin(players[players.length - 1])) {
                        System.out.println("The Fox wins!");
                        exit = true;
                    }
                    else if (FoxHoundUtils.isHoundWin(players, dim)) {
                        System.out.println("The Hounds win!");
                        exit = true;
                    }
                    turn = swapPlayers(turn);
                    break;
                case FoxHoundUI.MENU_SAVE:
                    Path pathS = FoxHoundUI.fileQuery(STDIN_SCAN);
                    boolean ifSaved = false;
                    ifSaved = FoxHoundIO.saveGame(players, turn, pathS);
                    if (!ifSaved) {
                        System.err.println("ERROR: Saving file failed.");
                    }
                    break;
                case FoxHoundUI.MENU_LOAD:
                    Path pathL = FoxHoundUI.fileQuery(STDIN_SCAN);
                    char nextMove = FoxHoundIO.loadGame(players, pathL);
                    if (nextMove == '#') {
                        System.err.println("ERROR: Loading from file failed.");
                    }
                    else {turn = nextMove;}
                    break;
                case FoxHoundUI.MENU_EXIT:
                    exit = true;
                    break;
                default:
                    System.err.println("ERROR: invalid menu choice: " + choice);
            }
        }
    }

    /**
     * Entry method for the Fox and Hound game. 
     * 
     * The dimensions of the game board can be passed in as
     * optional command line argument.
     * 
     * If no argument is passed, a default dimension of 
     * {@value FoxHoundUtils#DEFAULT_DIM} is used. 
     * 
     * Dimensions must be between {@value FoxHoundUtils#MIN_DIM} and 
     * {@value FoxHoundUtils#MAX_DIM}.
     * 
     * @param args contain the command line arguments where the first can be
     * board dimensions.
     */
    public static void main(String[] args) {
        int dimension;
        if (args != null) {
            if (Integer.parseInt(args[0]) > FoxHoundUtils.MAX_DIM || Integer.parseInt(args[0]) < FoxHoundUtils.MIN_DIM) {
                System.out.println("Sorry, board dimensions should be from 4 to 26. Using default dimensions instead");
                dimension = FoxHoundUtils.DEFAULT_DIM;
            }
            else {
                dimension = Integer.parseInt(args[0]);
            }
        }
        else {
            dimension = FoxHoundUtils.DEFAULT_DIM;
        }

        String[] players = FoxHoundUtils.initialisePositions(dimension);
        gameLoop(dimension, players);
        // Close the scanner reading the standard input stream       
        STDIN_SCAN.close();
    }
}
