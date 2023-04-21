package edu.neu.picogram.gamedata;

import android.content.Context;
import android.util.Log;

import edu.neu.picogram.Nonogram;
import edu.neu.picogram.NonogramUtils;
import edu.neu.picogram.R;

import java.io.File;
import java.util.ArrayList;

public class LargeScaleGameConstants {

  public static final int[] largeScaleGameIds = {
    R.raw.nonogram, R.raw.nonogram2, R.raw.nonogram3, R.raw.nonogram4, R.raw.nonogram5,
  };

  public static ArrayList<Nonogram> getGames(Context context) {
    ArrayList<Nonogram> games = new ArrayList<>();
    File dir = new File(context.getExternalFilesDir(null), "nonogram");
    File[] files = dir.listFiles();
    if (files != null) {
      for (File file : files) {
        Log.d("UserNonogramConstants", "getGames: " + file.getName());
        Nonogram game = NonogramUtils.restoreGame(context, file);
        if (game != null) {
          games.add(game);
        }
      }
    }

    for (int largeScaleGameId : largeScaleGameIds) {
      Nonogram game = NonogramUtils.restoreGame(context, largeScaleGameId);
      if (game != null) {
        games.add(game);
      }
    }
    return games;
  }

  public static ArrayList<Nonogram> getGames(Context context, int index) {
    Nonogram nonogram = getGames(context).get(index);
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

  public static Nonogram getGame(Context context, int outerIndex, int innerIndex) {
    return getGames(context, outerIndex).get(innerIndex);
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
