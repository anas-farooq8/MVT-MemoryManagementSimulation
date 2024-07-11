# MVT - Memory Management Simulation

This project simulates the "Multiple Partitions - Variable Regions" (MVT) memory management technique using Java.

## Project Description

The simulation manages a memory of size 2 GB (2048 MB), with 512 MB allocated for the operating system (OS). Processes are read from two text files, `ready.txt` and `job.txt`, representing the ready queue and the job queue, respectively. Each line in these files contains:

- Process number
- Process size
- Time in memory

Processes from the ready queue are allocated to memory in the order given in the file. When a job finishes execution, a new job is brought from the job queue using the **First-Come, First-Served (FCFS)** scheduling algorithm with skipping. The job is fit into the memory based on the variable partitioning technique named as **First Fit**. 

## Features

- **Memory Allocation:** First fit algorithm is used for memory allocation.
- **Compaction:** Memory compaction is performed when the number of holes exceeds three.
- **Graphical Representation:** The program displays detailed diagrams of memory contents, showing allocated partitions, free holes, and the contents of the ready and job queues at each step.
- **Process Execution:** The simulation shows which job finishes execution and which job is brought into memory to replace it.
- **Memory State Before and After Compaction:** Diagrams show memory partitions before and after compaction.

## Usage

### Requirements

- Java Development Kit (JDK)
- JavaFX or Swing for the user interface

### Running the Program

1. Ensure you have the required Java environment set up.
2. Place the `ready.txt` and `job.txt` files in the `src/files` directory.
3. Compile and run the program.

### Files

- `ready.txt`: Contains the initial processes in the ready queue.
- `job.txt`: Contains the processes in the job queue.

Each line in these files should have the following format:

### Example
```text
ready.txt:
1    100    10
2    200    15

job.txt:
3    150    20
4    300    25
```

## Demo
https://github.com/anas-farooq8/MVT-MemoryManagementSimulation/assets/150327092/afb5adbb-f289-46fd-bc88-265c9eeffffa

