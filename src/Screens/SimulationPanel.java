package Screens;

import models.Hole;
import models.PCB;
import utils.Constants;
import utils.FileReaderUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// Class representing the Simulation panel where the simulation is displayed
public class SimulationPanel extends JPanel implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(SimulationPanel.class.getName());

    // Instance variables
    private Queue<PCB> readyQueue;
    private Queue<PCB> jobQueue;
    private final List<PCB> memory;
    private final List<Hole> holes;


    // Constants
    final int RAM_SIZE = 2048;
    final int OS_MEMORY = 512;

    // Text area for displaying completed processes
    private final JTextArea completedProcessesTextArea;

    // Compaction Count
    private int compactionCount = 0;

    // Constructor to initialize the simulation panel
    public SimulationPanel() {
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.setBackground(Color.GRAY);
        this.setDoubleBuffered(true);

        // File chooser to select process files
        JFileChooser fileChooser = new JFileChooser();
        // Set the current directory to the specified path
        fileChooser.setCurrentDirectory(new File("src/files"));

        // Choose the ready queue
        JOptionPane.showMessageDialog(null, "Please choose the Ready Queue file.");
        fileChooser.setDialogTitle("Select Ready Queue File");
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File readyFile = fileChooser.getSelectedFile();
            try {
                readyQueue = FileReaderUtil.readProcesses(readyFile.getAbsolutePath());
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to read processes from Ready Queue file.", e);
                System.exit(1);
            }
        }

        // Choose the job queue
        JOptionPane.showMessageDialog(null, "Please choose the Job Queue file.");
        fileChooser.setDialogTitle("Select Job Queue File");
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File jobFile = fileChooser.getSelectedFile();
            try {
                jobQueue = FileReaderUtil.readProcesses(jobFile.getAbsolutePath());
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to read processes from file.", e);
                System.exit(1);
            }
        }

        // Initialize memory and holes
        memory = new LinkedList<>();
        holes = new LinkedList<>();
        holes.add(new Hole(OS_MEMORY, RAM_SIZE - 1)); // Initially one big hole

        // Create the text area and scroll pane for completed processes
        completedProcessesTextArea = new JTextArea(5, 20);
        completedProcessesTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(completedProcessesTextArea);
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.SOUTH);

        // Panel for the Next Step button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton nextStepButton = new JButton("Next Step");
        nextStepButton.addActionListener(this);
        buttonPanel.add(nextStepButton);
        this.add(buttonPanel, BorderLayout.NORTH); // Add button to the bottom of the panel
    }

    // Method to paint the simulation panel
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw the title
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("Multiprogramming with Variable number of Tasks", (Constants.SCREEN_WIDTH / 2) - 250, 60);

        // Center the RAM representation
        int ramX = (Constants.SCREEN_WIDTH - 300) / 2;
        int ramY = ((Constants.SCREEN_HEIGHT - 500) / 2 + 80);

        // Draw the RAM boundaries
        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        g2.drawString("0", ramX - 20, ramY + 15);
        g2.drawString(Integer.toString(RAM_SIZE - 1), ramX - 30, ramY + 415);

        // Draw OS memory
        int y = ramY;
        g2.setColor(Color.GREEN);
        g2.fillRect(ramX, y, 300, OS_MEMORY * 400 / RAM_SIZE);
        g2.setColor(Color.BLACK);
        g2.drawRect(ramX, y, 300, OS_MEMORY * 400 / RAM_SIZE);
        g2.drawString("OS Base: 0", ramX + 5, y + 15);
        g2.drawString("Limit: " + (OS_MEMORY - 1), ramX + 238, y + 95); // Display OS limit
        y += OS_MEMORY * 400 / RAM_SIZE; // Increased height

        // Combine processes and holes into a single list for sorting
        List<Object> memoryBlocks = new ArrayList<>();
        memoryBlocks.addAll(memory);
        memoryBlocks.addAll(holes);

        // Sort by base address
        memoryBlocks.sort(Comparator.comparingInt(block -> {
            if (block instanceof PCB) {
                return ((PCB) block).getBaseRegister();
            } else {
                return ((Hole) block).getBase();
            }
        }));

        // Draw processes and holes in memory
        for (Object block : memoryBlocks) {
            if (block instanceof PCB process) {
                g2.setColor(Color.YELLOW);
                int processHeight = process.getProcessSize() * 400 / RAM_SIZE;
                g2.fillRect(ramX, y, 300, processHeight);
                g2.setColor(Color.BLACK);
                g2.drawRect(ramX, y, 300, processHeight);
                g2.drawString("P" + process.getProcessId() + " Base: " + process.getBaseRegister(), ramX + 5, y + 15);
                // display remaining time
                g2.drawString("Time: " + process.getRemainingTime(), ramX + 135, y + 15);
                y += processHeight;
                g2.drawString("Limit: " + process.getLimitRegister(), ramX + 238, y - 5);
            } else if (block instanceof Hole hole) {
                g2.setColor(Color.RED);
                int holeHeight = hole.getSize() * 400 / RAM_SIZE;
                g2.fillRect(ramX, y, 300, holeHeight);
                g2.setColor(Color.BLACK);
                g2.drawRect(ramX, y, 300, holeHeight);
                g2.drawString("Hole" + " Base: " + hole.getBase() + " Limit: " + hole.getLimit() + " Size:" + hole.getSize(), ramX + 305, y + 15);
                y += holeHeight;
            }
        }

        // Center the ready queue on the right
        int readyQueueX = Constants.SCREEN_WIDTH - 200;
        y = (Constants.SCREEN_HEIGHT - (readyQueue.size() * 20 + 40)) / 2;
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Ready Queue:", readyQueueX, y);
        y += 20;
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        for (PCB process : readyQueue) {
            g2.drawString("P" + process.getProcessId() + " Mem: " + process.getProcessSize() + " Time: " + process.getTimeInMemory(), readyQueueX, y);
            y += 20;
        }

        // Center the job queue on the left
        int jobQueueX = 50;
        y = (Constants.SCREEN_HEIGHT - (jobQueue.size() * 20 + 40)) / 2;
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Job Queue:", jobQueueX, y);
        y += 20;
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        for (PCB process : jobQueue) {
            g2.drawString("P" + process.getProcessId() + " Mem: " + process.getProcessSize() + " Time: " + process.getTimeInMemory(), jobQueueX, y);
            y += 20;
        }
    }

    // Method to Allocate processes from the ready queue to memory
    private int allocateProcesses() {
        // If there is no process in the memory and only the OS is present
        // Then convert all the holes to one big hole
        if (memory.isEmpty()) {
            if(holes.size() > 1) {
                holes.clear();
                holes.add(new Hole(OS_MEMORY, RAM_SIZE - 1));

                // Append completed process information to the text area
                String mergingHoles;
                // if the process can't be fit in the memory and there are more than one hole
                if(readyQueue.isEmpty() && jobQueue.isEmpty()) {
                    mergingHoles = "No process to allocate, Merging Holes.\n";
                }
                else {
                    mergingHoles = "Can't allocated Process, Merging Holes.\n";
                }

                SwingUtilities.invokeLater(() -> completedProcessesTextArea.append(mergingHoles));
            }
        }

        int allocationCount = 0;
        // Check if there are processes in the ready queue
        while (!readyQueue.isEmpty()) {
            PCB process = readyQueue.peek();

            // First fit algorithm
            // Find the first hole that is large enough to accommodate the process
            Hole suitableHole = null;
            for (Hole hole : holes) {
                if (hole.getSize() >= process.getProcessSize()) {
                    suitableHole = hole;
                    break;
                }
            }

            // If a suitable hole is found
            if (suitableHole != null) {
                // Allocate the process in the suitable hole
                process.setBaseRegister(suitableHole.getBase());
                process.setLimitRegister(suitableHole.getBase() + (process.getProcessSize() - 1));

                // Update the hole
                suitableHole.setBase(suitableHole.getBase() + process.getProcessSize());
                suitableHole.setSize(suitableHole.getSize() - (process.getProcessSize() - 1));
                if (suitableHole.getSize() == 0) {
                    holes.remove(suitableHole);
                }

                // Move the process from the ready queue to memory
                readyQueue.poll();
                memory.add(process);
                allocationCount++;

                // Append allocated process information to the text area
                String completedProcessInfo = String.format(
                        "P%d Allocated.\n",
                        process.getProcessId()
                );
                SwingUtilities.invokeLater(() -> completedProcessesTextArea.append(completedProcessInfo));


            } else {
                break; // No suitable hole found, break the loop
            }
        }
        return allocationCount;
    }

    // Method to create a hole in memory after a process is deallocated
    private void createHole(PCB process) {
        int base = process.getBaseRegister();
        int limit = process.getLimitRegister();
        holes.add(new Hole(base, limit));
    }

    // Method to execute processes and deallocate finished processes
    private void executeProcesses() {
        // Check if there are no processes in memory
        if(!memory.isEmpty()) {
            // Find the smallest remaining time among all processes in memory
            int smallestRemainingTime = memory.stream()
                    .mapToInt(PCB::getRemainingTime)
                    .min()
                    .orElse(Integer.MAX_VALUE);

            // List to store processes that will complete in this step
            List<PCB> completedProcesses = new ArrayList<>();

            // Identify all processes with the smallest remaining time
            for (Iterator<PCB> iterator = memory.iterator(); iterator.hasNext();) {
                PCB process = iterator.next();
                if (process.getRemainingTime() == smallestRemainingTime) {
                    // Process completes in this step
                    completedProcesses.add(process);
                    iterator.remove(); // Remove from memory
                }
            }

            // Handle completed processes
            for (PCB completedProcess : completedProcesses) {
                // Append completed process information to the text area
                String completedProcessInfo = String.format(
                        "P%d completed.\n",
                        completedProcess.getProcessId()
                );
                SwingUtilities.invokeLater(() -> completedProcessesTextArea.append(completedProcessInfo));

                // Create hole in memory for the completed process
                createHole(completedProcess);
            }

            // Reduce remaining time for all other processes in memory
            for (PCB process : memory) {
                int remainingTime = process.getRemainingTime() - smallestRemainingTime;
                process.setRemainingTime(remainingTime);
            }
        }
    }


    // Method to compact memory by shifting holes to one side.
    private void compactMemory() {
        // Sort processes based on base register
        memory.sort(Comparator.comparingInt(PCB::getBaseRegister));

        // Reallocate processes to create one big hole at the end
        int currentBase = OS_MEMORY; // Start after OS memory
        for (PCB process : memory) {
            process.setBaseRegister(currentBase);
            process.setLimitRegister(currentBase + (process.getProcessSize() - 1));
            currentBase += process.getProcessSize();
        }

        // Create one big hole
        holes.clear();
        holes.add(new Hole(currentBase, RAM_SIZE - 1));

        // Append completed process information to the text area
        String compactionPerformed = "Compaction Performed.\n";
        SwingUtilities.invokeLater(() -> completedProcessesTextArea.append(compactionPerformed));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Check for completion after each step
        checkForCompletion();

        // Repaint the panel after each step
        repaint();

        // Allocate processes from the ready queue to memory
        if(allocateProcesses() > 0)
            return;

        // Pick a random job from the job queue and move it to the ready queue
        pickNextJob();

        // Before executing if the process can be moved to the memory (allocated)
        if(allocateProcesses() > 0)
            return;

        // Simulate the execution of processes and deallocate finished processes
        executeProcesses();

        // Check if compaction is needed
        if (holes.size() > 3) {
            captureScreenshot("before_compaction" + compactionCount);
            compactMemory();
            // Capture memory state after compaction
            captureScreenshot("after_compaction" + compactionCount);
            compactionCount++;
        }
        
    }

    // Method to pick the next job from the job queue that can fit into memory
    private void pickNextJob() {
        Iterator<PCB> iterator = jobQueue.iterator();
        while (iterator.hasNext()) {
            PCB job = iterator.next();

            // Check if the job can fit in any hole
            for (Hole hole : holes) {
                if (hole.getSize() >= job.getProcessSize()) {
                    readyQueue.add(job);
                    iterator.remove();
                    // Append the picked job information to the text area
                    String pickedJob = String.format(
                            "P%d Picked from Job Queue.\n",
                            job.getProcessId()
                    );

                    SwingUtilities.invokeLater(() -> completedProcessesTextArea.append(pickedJob));
                    return; // Only pick one job at a time
                }
            }
        }
    }


    // Method to check if the simulation is complete
    private void checkForCompletion() {
        if (jobQueue.isEmpty() && readyQueue.isEmpty() && memory.isEmpty()) {
            // If there is no process in the memory and only the OS is present
            // Then convert all the holes to one big hole
            holes.clear();
            holes.add(new Hole(OS_MEMORY, RAM_SIZE - 1));

            // Stop the simulation
            stopSimulation();

            // Show the completion alert box
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    this,
                    "Simulation is Completed",
                    "Simulation Completed",
                    JOptionPane.INFORMATION_MESSAGE
            ));
        }
    }

    // Method to stop the simulation
    private void stopSimulation() {
        // If there is no process in the memory and only the OS is present
        // Then convert all the holes to one big hole
        holes.clear();
        holes.add(new Hole(OS_MEMORY, RAM_SIZE - 1));
    }

    private void captureScreenshot(String filename) {
        String directory = "output";
        // Create directory if it does not exist
        Path path = Paths.get(directory);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to create output directory.", e);
                return; // Exit if directory creation failed
            }
        }

        // Append the file extension and save the image
        String filepath = directory + File.separator + filename + ".png";
        BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        this.paint(g2);
        g2.dispose();

        try {
            ImageIO.write(image, "png", new File(filepath));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save screenshot.", e);
        }
    }
}