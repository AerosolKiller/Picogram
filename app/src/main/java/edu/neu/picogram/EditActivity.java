package edu.neu.picogram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Arrays;

public class EditActivity extends AppCompatActivity {
    int[][] colClues;
    int[][] rowClues;
    boolean[][] solution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        NonogramEditView nonogramView = findViewById(R.id.nonogramView);
        // 创建一个全0的5*5的游戏，作为初始状态，传入NonogramEditView
        rowClues = new int[5][];
        colClues = new int[5][];
        solution = new boolean[5][5];
        Nonogram game = new Nonogram(solution.length, solution[0].length, rowClues, colClues, solution);
        nonogramView.setGame(game);
        // 保存按钮，点击后将游戏保存为json格式
        Button button = findViewById(R.id.saveAnswer);
        button.setOnClickListener(
                v -> {
                    Nonogram newGame = nonogramView.getGame();
                    saveGame(newGame.getRowClues(), newGame.getColClues(), newGame.getSolution());
                });
    }

    private void saveGame(int[][] rowClues, int[][] colClues, boolean[][] solution) {
        // 把当前游戏的状态转换为json数组，然后转换为json字符串
        // 当前只是在Logcat中打印出来，后续会把json字符串保存为文件
        JSONArray jsonArray = new JSONArray(Arrays.asList(colClues));
        JSONArray jsonArray1 = new JSONArray(Arrays.asList(rowClues));
        JSONArray jsonArray2 = new JSONArray(Arrays.asList(solution));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("colClues", jsonArray);
            jsonObject.put("rowClues", jsonArray1);
            jsonObject.put("solution", jsonArray2);
            String json = jsonObject.toString();
            Log.d("json", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}