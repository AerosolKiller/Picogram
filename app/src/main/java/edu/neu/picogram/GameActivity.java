package edu.neu.picogram;

import static edu.neu.picogram.NonogramGameConstants.getGame;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class GameActivity extends AppCompatActivity {
  Nonogram game;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // 游戏关卡存储在raw文件夹中，每个关卡对应一个json文件，读取第一关的json文件作为默认关卡
    //        restoreGame(this, R.raw.level1);
    int gameId = this.getIntent().getExtras().getInt("index");
    game = getGame(gameId);
    // 读取关卡后，实际上给solution，rowClues，colClues赋值了，创建Nonogram对象
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
  }
}
