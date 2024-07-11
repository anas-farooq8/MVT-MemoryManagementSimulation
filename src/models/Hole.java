package models;

public class Hole {
    int base;
    int limit;
    int size;

    public Hole(int base, int limit) {
        this.base = base;
        this.limit = limit;
    }

    public int getBase() {
        return base;
    }
    public int getSize() { return limit - base; }

    public int getLimit() { return limit; }

    public void setBase(int base) {
        this.base = base;
    }
    public void setSize(int size) { this.size = size; }
}