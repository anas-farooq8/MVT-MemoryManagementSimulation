package utils;

// Constants class that holds various static values for the game
public class Constants {
    // Constants defining tile sizes and scaling factor
    public final static int originalTileSize = 32;
    public final static int scale = 2; // 64 * 64

    // Derived constants based on tile size and scale
    public final static int tileSize = originalTileSize * scale;

    // Constants related to screen dimensions and layout
    public final static int maxScreenCol = 20;
    public final static int maxScreenRow = 12;

    public final static int SCREEN_WIDTH = tileSize * maxScreenCol; // 1280
    public final static int SCREEN_HEIGHT = tileSize * maxScreenRow; // 768

    // Method to format the elapsed time into a readable string (mm:ss:SSS)
    public static String formatTime(long elapsedTime) {
        long millis = elapsedTime % 1000;
        long seconds = (elapsedTime / 1000) % 60;
        long minutes = (elapsedTime / (1000 * 60)) % 60;
        return String.format("%02d:%02d:%03d", minutes, seconds, millis);
    }

}
