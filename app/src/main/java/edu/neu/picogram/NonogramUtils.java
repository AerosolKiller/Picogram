package edu.neu.picogram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class NonogramUtils {

  public static void saveGame(String name, int[][] rowClues, int[][] colClues, int[][] solution) {
    // 把当前游戏的状态转换为json数组，然后转换为json字符串
    // 当前只是在Logcat中打印出来，后续会把json字符串保存为文件
    JSONArray jsonArray = new JSONArray(Arrays.asList(rowClues));
    JSONArray jsonArray1 = new JSONArray(Arrays.asList(colClues));
    JSONArray jsonArray2 = new JSONArray(Arrays.asList(solution));
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("name", name);
      jsonObject.put("rowClues", jsonArray);
      jsonObject.put("colClues", jsonArray1);
      jsonObject.put("solution", jsonArray2);
      String json = jsonObject.toString();
      Log.d("json", json);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 读取关卡json文件，更新solution，rowClues，colClues这几个变量.
   * 方法本质是打开文件，把文件内部先读到byte数组，再转成String，最后通过json解析，转化为json对象.
   * 在json对象中，通过key，找到对应的json数组，再把json数组转成int数组，或者boolean数组.
   *
   * @param fileID res 内raw 文件夹中的文件，实际上有个int类型的id， 通过id，可以找到对应的文件.
   */
  public static Nonogram restoreGame(Context context, int fileID) {
    int[][] rowClues;
    int[][] colClues;
    int[][] solution;
    try {
      InputStream inputStream = context.getResources().openRawResource(fileID);
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int length;
      while ((length = inputStream.read(buffer)) != -1) {
        result.write(buffer, 0, length);
      }
      inputStream.close();
      String jsonString = result.toString("UTF-8");
      JSONObject jsonObject = new JSONObject(jsonString);
      JSONArray jsonArray = jsonObject.getJSONArray("rowClues");
      JSONArray jsonArray1 = jsonObject.getJSONArray("colClues");
      JSONArray jsonArray2 = jsonObject.getJSONArray("solution");
      String name = jsonObject.getString("name");
      rowClues = new int[jsonArray.length()][];
      colClues = new int[jsonArray1.length()][];
      solution = new int[jsonArray2.length()][];
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONArray jsonArray3 = jsonArray.getJSONArray(i);
        int[] ints = new int[jsonArray3.length()];
        for (int j = 0; j < jsonArray3.length(); j++) {
          ints[j] = jsonArray3.getInt(j);
        }
        rowClues[i] = ints;
      }
      for (int i = 0; i < jsonArray1.length(); i++) {
        JSONArray jsonArray3 = jsonArray1.getJSONArray(i);
        int[] ints = new int[jsonArray3.length()];
        for (int j = 0; j < jsonArray3.length(); j++) {
          ints[j] = jsonArray3.getInt(j);
        }
        colClues[i] = ints;
      }
      for (int i = 0; i < jsonArray2.length(); i++) {
        JSONArray jsonArray3 = jsonArray2.getJSONArray(i);
        int[] ints = new int[jsonArray3.length()];
        for (int j = 0; j < jsonArray3.length(); j++) {
          ints[j] = jsonArray3.getInt(j);
        }
        solution[i] = ints;
      }
      return new Nonogram(name, colClues.length, rowClues.length, rowClues, colClues, solution);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static int[] reverseList(int[] array) {
    // 反转数组，方便绘制提示数字
    int[] result = new int[array.length];
    for (int i = 0; i < array.length; i++) {
      result[i] = array[array.length - i - 1];
    }
    return result;
  }

  public static void updateGame(Nonogram game) {
    // 点击之后，更新游戏内部的solution和clues
    // 根据当前的grid，计算出solution
    int[][] grid = new int[game.getHeight()][game.getWidth()];
    for (int row = 0; row < game.getHeight(); row++) {
      for (int col = 0; col < game.getWidth(); col++) {
        if (game.getCurrentGrid(row, col) == 1) {
          grid[row][col] = 1;
        }
      }
    }
    game.setSolution(grid);
    // 计算出rowClues，为了方便先用List<List<Integer>>，最后转换成int[][]
    List<List<Integer>> rowClues = new ArrayList<>();
    int count = 0;
    for (int row = 0; row < game.getHeight(); row++) {
      List<Integer> rowClue = new ArrayList<>();
      for (int col = 0; col < game.getWidth(); col++) {
        if (grid[row][col] == 1) {
          count++;
          if (col == game.getWidth() - 1) {
            rowClue.add(count);
            count = 0;
          }
        } else {
          if (count > 0) {
            rowClue.add(count);
            count = 0;
          }
        }
      }
      rowClues.add(rowClue);
    }
    int[][] rowCluesArray = rowClues.stream().map(NonogramUtils::toIntArray).toArray(int[][]::new);
    game.setRowClues(rowCluesArray);
    // 计算出colClues，为了方便先用List<List<Integer>>，最后转换成int[][]
    List<List<Integer>> colClues = new ArrayList<>();
    count = 0;
    for (int col = 0; col < game.getWidth(); col++) {
      List<Integer> colClue = new ArrayList<>();
      for (int row = 0; row < game.getHeight(); row++) {
        if (grid[row][col] == 1) {
          count++;
          if (row == game.getHeight() - 1) {
            colClue.add(count);
            count = 0;
          }
        } else {
          if (count > 0) {
            colClue.add(count);
            count = 0;
          }
        }
      }
      colClues.add(colClue);
    }
    int[][] colCluesArray = colClues.stream().map(NonogramUtils::toIntArray).toArray(int[][]::new);
    game.setColClues(colCluesArray);
  }

  private static int[] toIntArray(List<Integer> list) {
    // 辅助函数方便把List<Integer>转换成int[]
    return list.stream().mapToInt(Integer::intValue).toArray();
  }

  public static Bitmap drawNonogram(Nonogram game) {
    int[][] solution = game.getSolution();
    int targetSize = 100;
    int ratio = Math.max(1, Math.min(targetSize / game.getWidth(), targetSize / game.getHeight()));
    Bitmap bitmap = Bitmap.createBitmap(
        game.getWidth() * ratio, game.getHeight() * ratio, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    paint.setColor(Color.BLACK);
    paint.setStyle(Paint.Style.FILL);
    for (int row = 0; row < game.getHeight(); row++) {
      for (int col = 0; col < game.getWidth(); col++) {
        if (solution[row][col] == 1) {
          canvas.drawRect(col * ratio, row * ratio, (col + 1) * ratio, (row + 1) * ratio, paint);
        }
      }
    }
    return bitmap;
  }
}
