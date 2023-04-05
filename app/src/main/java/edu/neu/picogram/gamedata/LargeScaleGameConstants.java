package edu.neu.picogram.gamedata;

import android.content.Context;
import edu.neu.picogram.Nonogram;
import edu.neu.picogram.NonogramUtils;
import java.util.ArrayList;

public class LargeScaleGameConstants {
  public static ArrayList<Nonogram> getGames(Context context, int resourceId) {
    Nonogram nonogram = NonogramUtils.restoreGame(context, resourceId);
    if (nonogram == null) {
      return null;
    }
    String name = nonogram.getName();
    ArrayList<Nonogram> games = new ArrayList<>();
    int[][] solution = nonogram.getSolution();
    int[][][] subMatrices = splitMatrix(solution, 10, 10);
    for (int i = 0; i < subMatrices.length; i++) {
      int[][] subMatrix = subMatrices[i];
      Nonogram game = new Nonogram(name + "Puzzle " + (i + 1), 10, 10, null, null, null, subMatrix);
      NonogramUtils.updateGame(game);
      game.setCurrentGrid(new int[10][10]);
      games.add(game);
    }

    return games;
  }

  public static Nonogram getGame(Context context, int resourceId, int index) {
    return getGames(context, resourceId).get(index);
  }

  private static int[][][] splitMatrix(int[][] inputMatrix, int numRows, int numCols) {
    int rowSize = inputMatrix.length / numRows;
    int colSize = inputMatrix[0].length / numCols;
    int[][][] outputMatrices = new int[rowSize * colSize][numRows][numCols];

    for (int r = 0; r < rowSize; r++) {
      for (int c = 0; c < colSize; c++) {
        int[][] subMatrix = new int[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
          for (int j = 0; j < numCols; j++) {
            subMatrix[i][j] = inputMatrix[r * numRows + i][c * numCols + j];
          }
        }
        outputMatrices[r * colSize + c] = subMatrix;
      }
    }

    return outputMatrices;
  }
}
