import java.util.Arrays;
/**
 * A utility class for the fox hound program.
 * 
 * It contains helper functions to check the state of the game
 * board and validate board coordinates and figure positions.
 */
public class FoxHoundUtils {

    // ATTENTION: Changing the following given constants can 
    // negatively affect the outcome of the auto grading!

    /** Default dimension of the game board in case none is specified. */
    public static final int DEFAULT_DIM = 8;
    /** Minimum possible dimension of the game board. */
    public static final int MIN_DIM = 4;
    /** Maximum possible dimension of the game board. */
    public static final int MAX_DIM = 26;
    /** Default length of players array. */
    public static final int DEFAULT_LENGTH = 5;

    /** Symbol to represent a hound figure. */
    public static final char HOUND_FIELD = 'H';
    /** Symbol to represent the fox figure. */
    public static final char FOX_FIELD = 'F';

    // HINT Write your own constants here to improve code readability ...
    /** Number of letter A in ASCII */
    public static final int LETTER_A = 65;
    /** Number of letter B in ASCII */
    public static final int LETTER_B = 66;

    /**
     * Create an array of initial players' positions.
     *
     * @param dimension an int representing dimensions of the board
     * @return a String array with players initial positions
     * @throws IllegalArgumentException if the board dimensions are invalid
     */
    public static String[] initialisePositions(int dimension) {
        if (dimension < MIN_DIM || dimension > MAX_DIM) {
            throw new IllegalArgumentException("Invalid board dimension");
        }
        int numOfHounds = Math.toIntExact((long) Math.floor(dimension / 2.0));
        int arrayLength = numOfHounds + 1;
        String[] positions = new String[arrayLength];
        // Initialising positions of hounds starting with B1
        for (int j = 0; j < arrayLength - 1; j++) {
            positions[j] = (char)(2*j + LETTER_B) + Integer.toString(1);
        }
        // Initialising foxes position
        if (dimension % 2 == 0) {
            // If dimension is even fox should either on the right or left center field
            if ((dimension / 2) % 2 == 0) {
                positions[positions.length - 1] = (char)(LETTER_A + dimension/2) + Integer.toString(dimension);
            }
            else {
                positions[positions.length - 1] = (char) (LETTER_A + dimension / 2 - 1) + Integer.toString(dimension);
            }
        }
        else {
            // If dimension is odd, fox should stand either on the center bottom field, or on the one on the right to it
            int centerFieldNumber = (int) Math.ceil(dimension / 2.0);
            if (centerFieldNumber % 2 == 0) {
                positions[positions.length - 1] = (char)(LETTER_A + centerFieldNumber - 1) + Integer.toString(dimension);
            }
            else {
                positions[positions.length - 1] = (char)(LETTER_A + centerFieldNumber) + Integer.toString(dimension);
            }
        }
        return positions;
    }

    /**
     * Determine if the users' entered coordinates represent a valid move for given figure.
     *
     * @param dim an int representing dimensions of the board
     * @param players a String array with coordinates of all the players on board
     * @param figure a char representing a figure to move
     * @param origin a String for coordinate to move form
     * @param destination a String for coordinate to move to
     * @return a boolean for valid or invalid move
     * @throws IllegalArgumentException if the board dimensions are invalid, players array is null, or
     * improper character representing figure to move
     */
    public static boolean isValidMove(int dim, String[] players, char figure, String origin, String destination) {
        boolean value = false;
        if (!(players != null
                && (figure == 'F' || figure == 'H')
                && dim >= MIN_DIM && dim <= MAX_DIM)) {
            throw new IllegalArgumentException("Invalid input");
        }
        // Checking if destination position is free and continue
        if (! (contains(players, destination))) {
            // Current position and destination position as numbers
            int[] current_pos = posToNum(origin);
            int[] destination_pos = posToNum(destination);
            switch (figure) {
                case FOX_FIELD:
                    if (players[players.length - 1].equals(origin)) {
                        // If destination position is correct, coordinates vary no more than by one
                        if (Math.abs(current_pos[0] - destination_pos[0]) == 1 && Math.abs(current_pos[1] - destination_pos[1]) == 1) {
                            value = true;
                        }
                    }
                    break;
                case HOUND_FIELD:
                    if (contains(Arrays.copyOfRange(players, 0, players.length - 1), origin)) {
                        // Same but hounds can go only forward
                        if ((current_pos[1] - destination_pos[1]) == -1 && Math.abs(current_pos[0] - destination_pos[0]) == 1) {
                            value = true;
                        }
                    }
                    break;
            }
        }
        return value;
    }

    /**
     * Determine if the coordinate is valid.
     *
     * @param dim an int representing dimensions of the board
     * @param coord an int array with coordinate converted to two integers
     * @return a boolean for valid or invalid coordinate
     */
    public static boolean isValidCoord(int dim, int[] coord) {
        boolean value = false;
        if (coord[0] <= (LETTER_A + dim - 1) && coord[1] <= dim){
            value = true;
        }
        return value;
    }

    /**
     * Determine if array contains specific element.
     *
     * @param array an array of any type
     * @param elem an element of such type that could be in array
     * @return a boolean whether an array contains an element or not
     */
    public static <T> boolean contains(T[] array, T elem) {
        boolean value = false;
        for (T a : array) {
            if (a.equals(elem)) {
                value = true;
                break;
            }
        }
        return value;
    }

    /**
     * Convert coordinate to two integers.
     *
     * @param position a coordinate String
     * @return an int array with two integers representing a letter and a number in coordinate
     */
    public static int[] posToNum(String position) {
        if (position.length() != 2 && position.length() != 3 ) {
            throw new IllegalArgumentException("Ivalid coordinate");
        }
        int[] coordPair = new int[2];
        coordPair[0] = position.charAt(0);
        coordPair[1] = Integer.parseInt(position.substring(1, position.length()));
        return coordPair;
    }

    /**
     * Print letters to console.
     *
     * @param letterArray a char alphabet array
     * @param dim an int for board dimensions
     */
    public static void letterPrinting(char[] letterArray, int dim){
        if (dim < 10) {
            System.out.print("  ");
        }
        else System.out.print("   ");

        for(int i = 0; i < dim; i++){
            System.out.print(letterArray[i]);
        }
        if (dim < 10) {
            System.out.println("  ");
        }
        else System.out.println("   ");
    }

    /**
     * Determine if fox wins the game.
     *
     * @param foxPos a coordinate String of fox
     * @return a boolean
     */
    public static boolean isFoxWin(String foxPos) {
        int[] foxCoords = posToNum(foxPos);
        boolean value = false;
        if (foxCoords[1] == 1) {
            value = true;
        }
        return value;
    }

    /**
     * Determine if hounds win the game.
     *
     * @param players a String array for positions of all the players
     * @param dim an integer for board dimensions
     * @return a boolean
     */
    public static boolean isHoundWin(String[] players, int dim) {
        if (dim < MIN_DIM || dim > MAX_DIM) {
            throw new IllegalArgumentException("Invalid board dimensions");
        }
        boolean value = false;
        int[] foxCoord = posToNum(players[players.length - 1]);
        // Counter to count how many fields are there for fox to go to
        int counter = 0;
        for (int i = -1; i <= 1; i+=2) {
            for (int j = -1; j <= 1; j+=2) {
                int[] potentialCoord = {foxCoord[0] + i, foxCoord[1] + j};
                String potentialCoordAsString = (char)potentialCoord[0] + Integer.toString(potentialCoord[1]);
                if (isValidCoord(dim, potentialCoord)
                        && !contains(Arrays.copyOfRange(players, 0, players.length - 1), potentialCoordAsString)){
                    counter++;
                }
            }
        }
        if (counter == 0) {
            value = true;
        }
        return value;
    }
}
