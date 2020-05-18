import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A utility class for the fox hound program.
 * 
 * It contains helper functions for all user interface related
 * functionality such as printing menus and displaying the game board.
 */
public class FoxHoundUI<fileName> {

    /** Number of main menu entries. */
    private static final int MENU_ENTRIES = 4;
    /** Main menu display string. */
    private static final String MAIN_MENU =
        "\n1. Move\n2. Save\n3. Load\n4. Exit\n\nEnter 1 - 4:";

    /** Menu entry to select a move action. */
    public static final int MENU_MOVE = 1;
    /** Menu entry to save the program. */
    public static final int MENU_SAVE = 2;
    /** Menu entry to load the program. */
    public static final int MENU_LOAD = 3;
    /** Menu entry to terminate the program. */
    public static final int MENU_EXIT = 4;

    /**
     * Print current board with players.
     *
     * @param players array of the positions of all figures
     * @param dimension an int for dimensions of the board
     * @throws IllegalArgumentException if the players array is null
     */
    public static void displayBoard(String[] players, int dimension) {
        if (dimension < FoxHoundUtils.MIN_DIM || dimension > FoxHoundUtils.MAX_DIM) {
            throw new IllegalArgumentException("Players array is null");
        }
        if (players == null) {
            throw new NullPointerException("Players array is null");
        }
        for (String str: players) {
            if (!FoxHoundUtils.isValidCoord(dimension, FoxHoundUtils.posToNum(str))){
                throw new IllegalArgumentException("Invalid coord");
            }
        }
        // Making an array of all the possible letter positions (just an alphabet of uppercases)
        // using unicode
        char[] letter_pos = new char[26];
        for(int i = 0; i < 26; i++){
            letter_pos[i] = (char)(65 + i);
        }

        FoxHoundUtils.letterPrinting(letter_pos, dimension);
        System.out.println();

        // Variable 'player' to keep track of the player we need to paste on the board
        int player = 1;

        for (int i = 1; i <= dimension; i++) {
            if (dimension > 9 && i < 10) {
                System.out.print(0);
            }
            System.out.print(i + " ");
            for (int j = 1; j <= dimension; j++) {
                if (FoxHoundUtils.contains(players, ("" + letter_pos[j - 1] + i))) {
                    if (players[players.length - 1].equals("" + letter_pos[j - 1] + i)) {
                        System.out.print("F");
                    }
                    else {System.out.print("H");}
                    if (player != players.length) {
                        player++;
                    }
                }
                else {
                    System.out.print(".");
                }
            }
            if (dimension > 9 && i < 10) {
                System.out.println(" " + 0 + i);
            }
            else {
                System.out.println(" " + i);
            }
        }
        System.out.println();
        FoxHoundUtils.letterPrinting(letter_pos, dimension);
    }

    /**
     * Print the main menu and query the user for an entry selection.
     * 
     * @param figureToMove the figure type that has the next move
     * @param stdin a Scanner object to read user input from
     * @return a number representing the menu entry selected by the user
     * @throws IllegalArgumentException if the given figure type is invalid
     * @throws NullPointerException if the given Scanner is null
     */
    public static int mainMenuQuery(char figureToMove, Scanner stdin) {
        Objects.requireNonNull(stdin, "Given Scanner must not be null");
        if (figureToMove != FoxHoundUtils.FOX_FIELD 
         && figureToMove != FoxHoundUtils.HOUND_FIELD) {
            throw new IllegalArgumentException("Given figure field invalid: " + figureToMove);
        }

        String nextFigure = 
            figureToMove == FoxHoundUtils.FOX_FIELD ? "Fox" : "Hounds";

        int input = -1;
        while (input == -1) {
            System.out.println(nextFigure + " to move");
            System.out.println(MAIN_MENU);

            boolean validInput = false;
            if (stdin.hasNextInt()) {
                input = stdin.nextInt();
                validInput = input > 0 && input <= MENU_ENTRIES;
            }

            if (!validInput) {
                System.out.println("Please enter valid number.");
                input = -1; // reset input variable
            }

            stdin.nextLine(); // throw away the rest of the line
        }

        return input;
    }

    /**
     * Ask user for coordinates.
     *
     * @param dim an int for dimensions of the board
     * @param stdin a Scanner object to read users input from
     * @return a String array with two coordinates
     */
    public static String[] positionQuery(int dim, Scanner stdin) {
        Objects.requireNonNull(stdin, "Given Scanner must not be null");
        if (dim < FoxHoundUtils.MIN_DIM || dim > FoxHoundUtils.MAX_DIM) {
            throw new IllegalArgumentException("Invalid dimension");
        }
        String[] coords = new String[2];

        boolean validPair = false;
        while (!validPair) {
            System.out.println("Provide origin and destination coordinates.");
            System.out.println("Enter two positions between A1-" + (char)(FoxHoundUtils.LETTER_A + dim - 1) + dim + ":");
            if (stdin.hasNext()) {
                String input = stdin.nextLine();
                String[] potentialCoord = input.trim().split("\\s+");
                // Checking if entered input is coordinates
                if ((potentialCoord.length == 2)
                        && FoxHoundUtils.isValidCoord(dim, FoxHoundUtils.posToNum(potentialCoord[0]))
                        && FoxHoundUtils.isValidCoord(dim, FoxHoundUtils.posToNum(potentialCoord[1]))) {
                    for (int i = 0; i < potentialCoord.length; i++) {
                        coords[i] = potentialCoord[i];
                    }
                    validPair = true;
                }
                else {
                    System.out.println();
                    System.err.println("ERROR: Please enter valid coordinate pair separated by space.");
                }
            }
        }
        return coords;
    }

    /**
     * Ask user for file path to save to or load from.
     *
     * @param stdin a Scanner object to read users input from
     * @return a Path object with entered path
     */
    public static Path fileQuery(Scanner stdin) {
        System.out.println("Enter file path:");
        String filename = stdin.nextLine();
        // Determine if entered file name has either / or \ in it
        Pattern pattern = Pattern.compile("[\\\\/]");
        Matcher matcher = pattern.matcher(filename);

        Path path;
        if (matcher.find()) {
            path = Paths.get(filename);
        }
        else {
            String pathStr = "C:\\Users\\Irina\\Documents\\Uni\\OOP\\Coursework\\inf1b-cw1\\Game\\" + filename;
            path = Paths.get(pathStr);
        }

        return path;
    }
}







