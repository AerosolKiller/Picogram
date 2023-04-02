package edu.neu.picogram;

import static edu.neu.picogram.NonogramUtils.saveGame;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {
  int[][] colClues;
  int[][] rowClues;
  int[][] solution;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit);
    NonogramView nonogramView = findViewById(R.id.nonogramView);
    nonogramView.setEditMode(true);
    // 创建一个全0的5*5的游戏，作为初始状态，传入NonogramEditView
    int width = 5;
    int height = 5;
    rowClues = new int[height][];
    colClues = new int[width][];
    solution = new int[width][height];
    Nonogram game = new Nonogram("", width, height, rowClues, colClues, solution);
    nonogramView.setGame(game);
    // 保存按钮，点击后将游戏保存为json格式
    Button button = findViewById(R.id.saveAnswer);
    button.setOnClickListener(
        v -> {
          Nonogram newGame = nonogramView.getGame();
          saveGame("", newGame.getRowClues(), newGame.getColClues(), newGame.getSolution());
        });

    //    boolean[][] solution = convertToNonogramMatrix(this, R.drawable.tutorial_img1, 200);
    //    JSONObject jsonObject = new JSONObject();
    //    try {
    //      JSONArray solutionArray = new JSONArray();
    //      for (int i = 0; i < solution.length; i++) {
    //        JSONArray rowArray = new JSONArray();
    //        for (int j = 0; j < solution[0].length; j++) {
    //          rowArray.put(solution[i][j]);
    //        }
    //        solutionArray.put(rowArray);
    //      }
    //      jsonObject.put("solution", solutionArray);
    //    } catch (Exception e) {
    //      Log.e("EditActivity", "Error when converting solution to json");
    //    }
    //    savaJson(this, jsonObject, "tutorial_img1");
  }
}
