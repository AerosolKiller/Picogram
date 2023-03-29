package edu.neu.picogram;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.json.JSONArray;
import org.json.JSONObject;

public class GameActivity extends AppCompatActivity {
  int[][] colClues;
  int[][] rowClues;
  boolean[][] solution;
  int level;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // 游戏关卡存储在raw文件夹中，每个关卡对应一个json文件，读取第一关的json文件作为默认关卡
    //        restoreGame(this, R.raw.level1);
    int gameId = this.getIntent().getExtras().getInt("level");
    Log.d("GameActivity", "onCreate: " + gameId);
    if (gameId != 0) {
      restoreGame(this, gameId);
    } else {
      restoreGame(this, R.raw.level1);
    }
    // 读取关卡后，实际上给solution，rowClues，colClues赋值了，创建Nonogram对象
    Nonogram game = new Nonogram(solution.length, solution[0].length, rowClues, colClues, solution);
    setContentView(R.layout.activity_game);
    // NonogramView是我们自定义的绘制游戏界面的View，作为一个组建，activity_game中引用
    // 这种方式不方便写构造器，通过set方法，把游戏数据传递给NonogramView
    NonogramView nonogramView = findViewById(R.id.nonogramView);
    nonogramView.setGame(game);
    // 目前检查当前游戏是否完成，是通过按钮手动触发，并不是实时检查，通过调用 Nonogram 内部的 isSolved 方法
    Button button = findViewById(R.id.checkAnswer);
    button.setOnClickListener(
        v -> {
          boolean isSolved = nonogramView.getGame().isSolved();
          Toast.makeText(this, isSolved ? "Solved!" : "Not solved yet", Toast.LENGTH_SHORT).show();
        });
    // 目前显示hint，会把正确答案全部显示出来，nonogramView内部有个状态变量代表是否显示hint
    // 通过set方法，把状态变量传递给nonogramView，然后重新绘制View
    CheckBox checkBox = findViewById(R.id.hint);
    checkBox.setOnCheckedChangeListener(
        (buttonView, isChecked) -> {
          nonogramView.setShowSolution(isChecked);
          nonogramView.invalidate();
        });
    // 切换游戏模式，是给当前单元格填充颜色还是打叉，通过nonogramView内部的状态变量来控制
    SwitchCompat switchCompat = findViewById(R.id.mode);
    switchCompat.setOnCheckedChangeListener(
        (buttonView, isChecked) -> {
          nonogramView.setMode(!isChecked);
          nonogramView.invalidate();
        });
    //        // 目前实现多关卡，在最上面有个下拉菜单，通过spinner的onItemSelected方法，来切换关卡
    //        Spinner spinner = findViewById(R.id.spinner);
    //        // 下拉菜单的内容，存储在array.levels中，通过ArrayAdapter来读取
    //        ArrayAdapter<CharSequence> adapter =
    //                ArrayAdapter.createFromResource(this, R.array.levels,
    // android.R.layout.simple_spinner_item);
    //        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    //        spinner.setAdapter(adapter);
    //        // 用哈希表，把下拉菜单内的position和具体的关卡json文件对应起来
    //        Map<Integer, Integer> levelMap =
    //                Map.of(
    //                        0, R.raw.level1, 1, R.raw.level2, 2, R.raw.level3, 3, R.raw.level11,
    // 4, R.raw.level12);
    //        // 下拉菜单的onItemSelected方法，通过哈希表，找到对应的关卡json文件，创建游戏数据，并重新绘制View
    //        spinner.setOnItemSelectedListener(
    //                new AdapterView.OnItemSelectedListener() {
    //                    @Override
    //                    public void onItemSelected(AdapterView<?> parent, View view, int position,
    // long id) {
    //                        level = levelMap.getOrDefault(position, R.raw.level1);
    //                        restoreGame(GameActivity.this, level);
    //                        Nonogram newGame =
    //                                new Nonogram(solution.length, solution[0].length, rowClues,
    // colClues, solution);
    //                        nonogramView.setGame(newGame);
    //                        nonogramView.invalidate();
    //                    }
    //
    //                    @Override
    //                    public void onNothingSelected(AdapterView<?> parent) {}
    //                });
  }

  /**
   * 读取关卡json文件，更新solution，rowClues，colClues这几个变量.
   * 方法本质是打开文件，把文件内部先读到byte数组，再转成String，最后通过json解析，转化为json对象.
   * 在json对象中，通过key，找到对应的json数组，再把json数组转成int数组，或者boolean数组.
   *
   * @param context
   * @param level res 内raw 文件夹中的文件，实际上有个int类型的id， 通过id，可以找到对应的文件.
   */
  private void restoreGame(Context context, int level) {
    try {
      InputStream inputStream = context.getResources().openRawResource(level);
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int length;
      while ((length = inputStream.read(buffer)) != -1) {
        result.write(buffer, 0, length);
      }
      inputStream.close();
      String jsonString = result.toString("UTF-8");
      JSONObject jsonObject = new JSONObject(jsonString);
      JSONArray jsonArray = jsonObject.getJSONArray("colClues");
      JSONArray jsonArray1 = jsonObject.getJSONArray("rowClues");
      JSONArray jsonArray2 = jsonObject.getJSONArray("solution");
      colClues = new int[jsonArray.length()][];
      rowClues = new int[jsonArray1.length()][];
      solution = new boolean[jsonArray2.length()][];
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONArray jsonArray3 = jsonArray.getJSONArray(i);
        int[] ints = new int[jsonArray3.length()];
        for (int j = 0; j < jsonArray3.length(); j++) {
          ints[j] = jsonArray3.getInt(j);
        }
        colClues[i] = ints;
      }
      for (int i = 0; i < jsonArray1.length(); i++) {
        JSONArray jsonArray3 = jsonArray1.getJSONArray(i);
        int[] ints = new int[jsonArray3.length()];
        for (int j = 0; j < jsonArray3.length(); j++) {
          ints[j] = jsonArray3.getInt(j);
        }
        rowClues[i] = ints;
      }
      for (int i = 0; i < jsonArray2.length(); i++) {
        JSONArray jsonArray3 = jsonArray2.getJSONArray(i);
        boolean[] booleans = new boolean[jsonArray3.length()];
        for (int j = 0; j < jsonArray3.length(); j++) {
          booleans[j] = jsonArray3.getBoolean(j);
        }
        solution[i] = booleans;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
