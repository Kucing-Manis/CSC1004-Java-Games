package org.example.game.animation;

public class SpriteData {
    // Name - Row - Width - Height - Time Second - Start - End
    private String name;
    private int row, width, height, start, end;
    private double timeSec;

    public SpriteData(String name, int row, int width, int height, int start, int end, double timeSec) {
        this.name = name;
        this.row = row;
        this.width = width;
        this.height = height;
        this.start = start;
        this.end = end;
        this.timeSec = timeSec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public double getTimeSec() {
        return timeSec;
    }

    public void setTimeSec(double timeSec) {
        this.timeSec = timeSec;
    }
}
