package edu.neu.picogram;

import java.util.List;

public class SerializableNonogram {
    private String name;
    private int width;
    private int height;
    private String creator;
    private String createTime;
    private int likedNum;

    private String rowClues;
    private String colClues;
    private String solution;
    private String currentGrid;

    public SerializableNonogram(String name,
                                int width,
                                int height,
                                String rowClues,
                                String colClues,
                                String solution,
                                String creator,
                                int likedNum,
                                String createTime
                                ) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.rowClues = rowClues;
        this.colClues = colClues;
        this.solution = solution;
        this.createTime = createTime;
        this.creator = creator;
        this.likedNum = likedNum;
        this.currentGrid = currentGrid;
    }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public int getLikedNum() {
    return likedNum;
  }

  public void setLikedNum(int likedNum) {
    this.likedNum = likedNum;
  }

  public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRowClues() {
        return rowClues;
    }

    public void setRowClues(String rowClues) {
        this.rowClues = rowClues;
    }

    public String getColClues() {
        return colClues;
    }

    public void setColClues(String colClues) {
        this.colClues = colClues;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getCurrentGrid() {
        return currentGrid;
    }

    public void setCurrentGrid(String currentGrid) {
        this.currentGrid = currentGrid;
    }
}
