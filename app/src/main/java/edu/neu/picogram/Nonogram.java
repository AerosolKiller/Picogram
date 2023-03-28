package edu.neu.picogram;

public class Nonogram {
    private int width;
    private int height;
    private int[][] rowClues;
    private int[][] colClues;
    private boolean[][] solution;
    private int[][] currentGrid;

    public Nonogram(int width, int height, int[][] rowClues, int[][] colClues, boolean[][] solution) {
        // 初始化游戏，储存游戏的宽高，行列提示，解，以及当前游戏的状态
        this.width = width;
        this.height = height;
        this.rowClues = rowClues;
        this.colClues = colClues;
        this.solution = solution;
        this.currentGrid = new int[height][width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getRowClues(int row) {
        return this.rowClues[row];
    }

    public int[] getColClues(int col) {
        return this.colClues[col];
    }

    public boolean getSolution(int row, int col) {
        return this.solution[row][col];
    }

    public int getCurrentGrid(int row, int col) {
        return currentGrid[row][col];
    }

    public int[][] getRowClues() {
        return rowClues;
    }

    public int[][] getColClues() {
        return colClues;
    }

    public boolean[][] getSolution() {
        return solution;
    }

    public void setRowClues(int[][] rowClues) {
        this.rowClues = rowClues;
    }

    public void setColClues(int[][] colClues) {
        this.colClues = colClues;
    }

    public void setSolution(boolean[][] solution) {
        this.solution = solution;
    }

    public void toggleCell(int row, int col, boolean mode) {
        // 对于当前游戏，每个点有三种状态，0表示未填，1表示标记为真，2表示标记为假
        int previousState = currentGrid[row][col];
        // 根据mode，看是要标记为真还是标记为假
        int newState = mode ? 1 : 2;
        // 如果当前状态和新状态一样，那么就把当前状态置为0，代表取消标记
        // 否则就把当前状态置为新状态
        if (previousState == newState) {
            currentGrid[row][col] = 0;
        } else {
            currentGrid[row][col] = newState;
        }
    }

    public boolean isSolved() {
        // 检查当前游戏的状态和解是否一致
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (solution[row][col] != (currentGrid[row][col] == 1)) {
                    return false;
                }
            }
        }
        return true;
    }
}