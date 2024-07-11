package Screens;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Class representing the start screen of the game
public class StartScreen extends JPanel implements ActionListener {
    private final JButton startSimulationButton;
    private final JButton exitButton;

    // Constructor to initialize the StartScreen object with a given parent window
    public StartScreen() {
        // Fields to store the parent JFrame, and buttons for various actions

        // SetTING layout, background color, and preferred size for the panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Vertical BoxLayout
        setBackground(Color.gray);
        setPreferredSize(new Dimension(640, 320));

        // Title label
        JLabel titleLabel = new JLabel("Multiple Partitions - Variable Regions", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Spacing
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 50)));

        // Buttons
        startSimulationButton = createButton("Start Simulation");
        exitButton = createButton("Exit");

        // Add buttons to the panel with spacing
        add(startSimulationButton);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(exitButton);
    }

    // Private helper method to create and configure buttons
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(200, 40)); // Set maximum size
        button.addActionListener(this);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand
        button.setFocusPainted(false);

        // Adding mouse listener to handle hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Bold outline on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBorder(UIManager.getBorder("Button.border")); // Default border when not hovered
            }
        });

        return button;
    }

    // ActionListener implementation to handle button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startSimulationButton) {
                Window.startSimulation();
            // Start the Simulation
        } else if (e.getSource() == exitButton) {
            // Exit the application when the exit button is clicked
            System.exit(0);
        }
    }
}
