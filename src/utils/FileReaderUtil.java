package utils;

import models.PCB;

import java.io.*;
import java.util.*;

public class FileReaderUtil {
    public static Queue<PCB> readProcesses(String filePath) throws IOException {
        Queue<PCB> processesQueue = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                int processId = Integer.parseInt(parts[0]);
                int processSize = Integer.parseInt(parts[1]);
                int timeInMemory = Integer.parseInt(parts[2]);
                processesQueue.add(new PCB(processId, processSize, timeInMemory));
            }
        }
        return processesQueue;
    }
}
