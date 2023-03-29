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

  public void setGameName(String gameName) {
    this.gameName = gameName;
  }

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }
}
