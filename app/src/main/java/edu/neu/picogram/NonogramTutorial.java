package edu.neu.picogram;

public class NonogramTutorial extends Nonogram {
  private final String description;
  private final Boolean isGame;
  private int imageId;

  public NonogramTutorial(String name, String description, Boolean isGame, int imageId) {
    super();
    this.name = name;
    this.description = description;
    this.isGame = isGame;
    this.imageId = imageId;
  }

  public NonogramTutorial(
      String name,
      String description,
      Boolean isGame,
      int width,
      int height,
      int[][] rowClues,
      int[][] colClues,
      int[][] solution) {
    super(name, width, height, rowClues, colClues, solution);
    this.name = name;
    this.description = description;
    this.isGame = isGame;
    this.currentGrid = new int[height][width];
  }

  public NonogramTutorial(
      String name,
      String description,
      Boolean isGame,
      int width,
      int height,
      int[][] rowClues,
      int[][] colClues,
      int[][] solution,
      int[][] currentGrid) {
    this(name, description, isGame, width, height, rowClues, colClues, solution);
    this.currentGrid = currentGrid;
  }

  public String getDescription() {
    return description;
  }

  public Boolean getGame() {
    return isGame;
  }

  @Override
  public boolean isSolved() {
    // 检查当前游戏的状态和解是否一致
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        if (currentGrid[row][col] != solution[row][col]) {
          return false;
        }
      }
    }
    return true;
  }
}
