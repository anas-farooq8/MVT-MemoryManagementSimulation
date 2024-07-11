package models;

public class PCB {
    private final int processId;
    private final int processSize;
    private final int timeInMemory;
    private int baseRegister;
    private int limitRegister;

    private int remainingTime;

    public PCB(int processId, int processSize, int timeInMemory) {
        this.processId = processId;
        this.processSize = processSize;
        this.timeInMemory = timeInMemory;
        this.remainingTime = timeInMemory;
    }

    public int getProcessId() {
        return processId;
    }
    public int getProcessSize() {
        return processSize;
    }
    public int getTimeInMemory() { return timeInMemory; }
    public int getBaseRegister() { return baseRegister; }
    public void setBaseRegister(int baseRegister) { this.baseRegister = baseRegister; }
    public int getLimitRegister() { return limitRegister; }
    public void setLimitRegister(int limitRegister) { this.limitRegister = limitRegister; }
    public int getRemainingTime() { return remainingTime; }
    public void setRemainingTime(int remainingTime) { this.remainingTime = remainingTime; }

}
