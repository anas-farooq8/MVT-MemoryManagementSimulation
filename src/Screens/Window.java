package Screens;

import javax.swing.*;

// Class representing the main window of the game
public class Window {

    // Private fields to store the window title and a static JFrame instance
    private final String title;
    private static JFrame window;

    // Constructor to initialize a Window object with a given title
    public Window(String title) {
        this.title = title;
    }

    // Method to initialize the game window
    public void init() {
        // Create a new JFrame with the specified title
        window = new JFrame(title);

        // Set default close operation, make window not resizable
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // Create and add the start screen to the window
        StartScreen startScreen = new StartScreen();
        window.add(startScreen);

        // Pack the components, set window location to the center, and make it visible
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    // Method to start the game with a given player name
    public static void startSimulation() {
        // Remove all components from the window
        window.getContentPane().removeAll();

        // Create and add the game panel to the window
        SimulationPanel simulationPanel = new SimulationPanel();
        window.add(simulationPanel);

        // Pack the components, set window location to the center, and make it visible
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Revalidate the window and start the game thread in the game panel
        window.revalidate();
    }
}
