package edu.neu.picogram;

import static edu.neu.picogram.NonogramTutorialConstants.getTutorials;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import java.util.List;

public class TutorialActivity extends AppCompatActivity {

  private int tutorialId = 0;
  private NonogramView nonogramView;
  private List<NonogramTutorial> tutorials;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tutorial);
    nonogramView = findViewById(R.id.nonogramView);
    tutorials = getTutorials();
    setTutorial();
  }

  private void setTutorial() {
    NonogramTutorial tutorial = tutorials.get(tutorialId);
    TextView description = findViewById(R.id.description);
    if (tutorial.getGame()) {
      nonogramView.setGame(tutorial);
    }
    Button button = findViewById(R.id.checkAnswer);
    button.setOnClickListener(
        v -> {
          boolean isSolved = nonogramView.getGame().isSolved();
          Toast.makeText(this, isSolved ? "Solved!" : "Not solved yet", Toast.LENGTH_SHORT).show();
          if (isSolved) {
            tutorialId++;
            if (tutorialId < tutorials.size()) {
              setTutorial();
            } else {
              description.setText("You have finished all the tutorials!");
            }
          }
        });
    button.setText("Next");
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

    description.setText(tutorial.getDescription());
  }
}
