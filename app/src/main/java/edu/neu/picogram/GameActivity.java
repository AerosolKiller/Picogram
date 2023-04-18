package edu.neu.picogram;

import static edu.neu.picogram.NonogramUtils.addPlayedSmallGameToUser;
import static edu.neu.picogram.NonogramUtils.getNonogramFromFireStore;
import static edu.neu.picogram.gamedata.NonogramGameConstants.getGame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import com.google.firebase.auth.FirebaseAuth;
import edu.neu.picogram.gamedata.LargeScaleGameConstants;

public class GameActivity extends AppCompatActivity {
  private Chronometer chronometer;
  SharedPreferences sharedPreferences;
  Nonogram game;
  String userId;
  int badgeCount;
  int gameId;
  String gameName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
      userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    // 游戏关卡存储在raw文件夹中，每个关卡对应一个json文件，读取第一关的json文件作为默认关卡
    //        restoreGame(this, R.raw.level1);
    gameId = this.getIntent().getExtras().getInt("index");
    String mode = this.getIntent().getExtras().getString("mode");
    if (mode.equals("small")) {
      game = getGame(gameId);
    } else if (mode.equals("large")) {
      int innerIndex = this.getIntent().getExtras().getInt("innerIndex");
      game = LargeScaleGameConstants.getGame(this, gameId, innerIndex);
      gameName = LargeScaleGameConstants.getGames(this).get(gameId).getName();
    } else if (mode.equals("firebase")) {
      gameName = this.getIntent().getExtras().getString("nonogram");
      game = getNonogramFromFireStore(gameName);
    }
    // 读取关卡后，实际上给solution，rowClues，colClues赋值了，创建Nonogram对象
    setContentView(R.layout.activity_game);
    // NonogramView是我们自定义的绘制游戏界面的View，作为一个组建，activity_game中引用
    // 这种方式不方便写构造器，通过set方法，把游戏数据传递给NonogramView

    chronometer = findViewById(R.id.chronometer);
    chronometer.setBase(SystemClock.elapsedRealtime());
    chronometer.start();
    NonogramView nonogramView = findViewById(R.id.nonogramView);
    nonogramView.setGame(game);
    // 目前检查当前游戏是否完成，是通过按钮手动触发，并不是实时检查，通过调用 Nonogram 内部的 isSolved 方法
    Button button = findViewById(R.id.checkAnswer);
    button.setOnClickListener(
        v -> {
          boolean isSolved = nonogramView.getGame().isSolved();
          if (!isSolved) {
            Toast.makeText(this, "Not solved yet", Toast.LENGTH_SHORT).show();
            return;
          }
          if (userId != null) {
            addPlayedSmallGameToUser(userId, game.getName());
          }
          updateGameProgress(gameId, mode);
          chronometer.stop();
          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setTitle("Congratulations!");
          builder.setMessage("You have solved the puzzle!\nTime used: " + chronometer.getText());
          builder.setPositiveButton(
              "OK",
              (dialog, which) -> {
                dialog.dismiss();
                finish();
              });
          builder.show();
          Toast.makeText(this, "Solved", Toast.LENGTH_SHORT).show();
        });
    // 目前显示hint，会把正确答案全部显示出来，nonogramView内部有个状态变量代表是否显示hint
    // 通过set方法，把状态变量传递给nonogramView，然后重新绘制View
    Button hintButton = findViewById(R.id.hint);
    TextView badge = findViewById(R.id.badge);
    badgeCount = Integer.parseInt(badge.getText().toString());
    hintButton.setOnClickListener(
        v -> {
          if (badgeCount > 0) {
            nonogramView.setShowSolution(true);
            nonogramView.invalidate();
            badgeCount--;
            badge.setText(String.valueOf(badgeCount));
            if (badgeCount == 0) {
              badge.setVisibility(TextView.GONE);
            }
          } else {
            Toast.makeText(this, "No more hints", Toast.LENGTH_SHORT).show();
          }
        });

    // 切换游戏模式，是给当前单元格填充颜色还是打叉，通过nonogramView内部的状态变量来控制
    SwitchCompat switchCompat = findViewById(R.id.mode);
    switchCompat.setOnCheckedChangeListener(
        (buttonView, isChecked) -> {
          nonogramView.setMode(!isChecked);
          nonogramView.invalidate();
        });
  }

  private void updateGameProgress(int index, String mode) {
    if (mode.equals("small")) {
      sharedPreferences = getSharedPreferences("game_progress", MODE_PRIVATE);
      SharedPreferences.Editor editor = sharedPreferences.edit();
      boolean isSolved = sharedPreferences.getBoolean(index + "isSolved", false);
      if (isSolved) {
        return;
      }
      int currentLevel = sharedPreferences.getInt("current_level", 4);
      editor.putInt("current_level", currentLevel + 1);
      editor.putBoolean(index + "isSolved", true);
      editor.apply();
    } else if (mode.equals("large")) {
      sharedPreferences = getSharedPreferences("large_game_progress", MODE_PRIVATE);
      SharedPreferences.Editor editor = sharedPreferences.edit();
      boolean isSolved = sharedPreferences.getBoolean(gameName + " " + index + "isSolved", false);
      if (isSolved) {
        return;
      }
      int currentLevel = sharedPreferences.getInt(gameName + "current_level", 0);
      editor.putInt(gameName + "current_level", currentLevel + 1);
      editor.putBoolean(gameName + " " + index + "isSolved", true);
      editor.apply();
    }
  }
}
