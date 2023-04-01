package edu.neu.picogram;

public class GameItem {
  private String gameName;
  private Integer level;

  public GameItem(String gameName, Integer level) {
    this.gameName = gameName;
    this.level = level;
  }

  public String getGameName() {
    return gameName;
  }

  public Integer getLevel() {
    return level;
  }
}
