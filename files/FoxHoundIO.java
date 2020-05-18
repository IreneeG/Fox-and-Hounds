import java.io.IOException;
import java.nio.file.Path;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * A utility class for the fox hound program.
 * 
 * It contains helper functions for all file input / output
 * related operations such as saving and loading a game.
 */
public class FoxHoundIO {

    /**
     * Save the game to a file.
     *
     * @param players array of the positions of all figures
     * @param path a Path object to save a file to
     * @param nextMove a character representing the figure to move next
     * @return a boolean value if the saving was successful
     * @throws IllegalArgumentException if the board dimensions are not default
     * @throws NullPointerException if the given Path is null
     */
    public static boolean saveGame(String[] players, char nextMove, Path path) {
        if (path == null) {
            throw new NullPointerException("The path is null");
        }
        if (players.length != FoxHoundUtils.DEFAULT_LENGTH) {
            throw new IllegalArgumentException("Saving can be done only for default board dimension");
        }
        else {
            for (int i = 0; i < FoxHoundUtils.DEFAULT_LENGTH; i++) {
                int[] coord = FoxHoundUtils.posToNum(players[i]);
                if (!FoxHoundUtils.isValidCoord(FoxHoundUtils.DEFAULT_DIM, coord)) {
                    throw new IllegalArgumentException("Saving can be done only for default board dimension");
                }
            }
        }
        return ifWroteToFile(players, nextMove, path);
    }

    /**
     * Load the game from a file.
     *
     * @param players array of the positions of all figures
     * @param path a Path object to save a file to
     * @return a character representing figure to move next
     * @throws IllegalArgumentException if the board dimensions are not default
     * @throws NullPointerException if the given Path is null
     */
    public static char loadGame(String[] players, Path path) {
        if (path == null) {
            throw new NullPointerException("The path is null");
        }
        if (players.length != 5) {
            throw new IllegalArgumentException("Loading works only for default board dimensions (8x8");
        }
        if (players == null) {
            throw new NullPointerException("Players array is not initialised");
        }
        for (String str: players) {
            if (str == null) {
                throw new  NullPointerException("Coordinate is null");
            }
            else if (!FoxHoundUtils.isValidCoord(8, FoxHoundUtils.posToNum(str))) {
                throw new  IllegalArgumentException("Illegal coordinate");
            }
        }
        char returnChar = '#';
        String data = "";
        try {
            File fileToScan = path.toFile();
            if (!fileToScan.createNewFile()) {
                Scanner fileScan = new Scanner(fileToScan);
                data = (fileScan.nextLine()).trim();
                if (data.matches("[FH](\\s[A-H][1-8]){5}")) {
                    String[] playersArray = (data.substring(2, data.length())).split(" ");
                    returnChar = data.charAt(0);
                    // Update players array
                    for (int i = 0; i < FoxHoundUtils.DEFAULT_LENGTH; i++) {
                        players[i] = playersArray[i];
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return returnChar;
    }

    /**
     * Determine if writing to a file was successful.
     *
     * @param players array of the positions of all figures
     * @param nextMove a character representing the figure to move next
     * @param path a Path object to save a file to
     * @return boolean value for successful or unsuccessful writing
     */
    public static boolean ifWroteToFile(String[] players, char nextMove, Path path) {
        boolean value = false;
        try {
            File fileToWrite = path.toFile();
            if (fileToWrite.createNewFile()) {
                try {
                    FileWriter fileWriter = new FileWriter(fileToWrite);
                    fileWriter.write(nextMove);
                    fileWriter.write(" ");
                    for (String p: players) {
                        fileWriter.write(p + " ");
                    }
                    value = true;
                    fileWriter.close();
                } catch (IOException e){
                    System.err.println("Couldn`t write to file.");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Couldn`t create new file.");
            e.printStackTrace();
        }
        return value;
    }

}
