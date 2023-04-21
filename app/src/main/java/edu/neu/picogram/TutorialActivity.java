package edu.neu.picogram;

import static edu.neu.picogram.gamedata.NonogramTutorialConstants.getTutorials;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import java.util.List;

public class TutorialActivity extends AppCompatActivity {

  private int tutorialId = 0;
  private NonogramView nonogramView;
  private TextView description;
  private Button button;
  private CheckBox checkBox;
  private SwitchCompat switchCompat;
  private ImageView imageView;
  private List<NonogramTutorial> tutorials;
  private ImageView crossSign, solidSquare;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tutorial);
    nonogramView = findViewById(R.id.nonogramView);
    description = findViewById(R.id.description);
    button = findViewById(R.id.checkAnswer);
    checkBox = findViewById(R.id.hint);
    switchCompat = findViewById(R.id.mode);
    crossSign = findViewById(R.id.cross_sign);
    solidSquare = findViewById(R.id.solid_square);
    imageView = findViewById(R.id.image);
    tutorials = getTutorials();
    setTutorial();
  }

  private void setTutorial() {
    NonogramTutorial tutorial = tutorials.get(tutorialId);

    if (tutorial.getGame()) {
      nonogramView.setVisibility(NonogramView.VISIBLE);
      imageView.setVisibility(ImageView.GONE);
      checkBox.setVisibility(CheckBox.VISIBLE);
      switchCompat.setVisibility(SwitchCompat.VISIBLE);
      crossSign.setVisibility(View.VISIBLE);
      solidSquare.setVisibility(View.VISIBLE);
      nonogramView.setGame(tutorial);
      nonogramView.setTutorialMode(true);

      button.setOnClickListener(
          v -> {
            boolean isSolved = nonogramView.getGame().isSolved();
            Toast.makeText(this, isSolved ? "Solved!" : "Not solved yet", Toast.LENGTH_SHORT)
                .show();
            if (isSolved) {
              tutorialId++;
              if (tutorialId < tutorials.size()) {
                setTutorial();
              } else {
                endTutorial();
              }
            }
          });
      button.setText("Next");
      // 目前显示hint，会把正确答案全部显示出来，nonogramView内部有个状态变量代表是否显示hint
      // 通过set方法，把状态变量传递给nonogramView，然后重新绘制View

      checkBox.setOnCheckedChangeListener(
          (buttonView, isChecked) -> {
            nonogramView.setShowSolution(isChecked);
            nonogramView.invalidate();
          });
      // 切换游戏模式，是给当前单元格填充颜色还是打叉，通过nonogramView内部的状态变量来控制

      switchCompat.setOnCheckedChangeListener(
          (buttonView, isChecked) -> {
            nonogramView.setMode(!isChecked);
            nonogramView.invalidate();
          });
    } else {
      nonogramView.setVisibility(NonogramView.INVISIBLE);
      imageView.setVisibility(ImageView.VISIBLE);
      checkBox.setVisibility(CheckBox.INVISIBLE);
      switchCompat.setVisibility(SwitchCompat.INVISIBLE);
      crossSign.setVisibility(View.INVISIBLE);
      solidSquare.setVisibility(View.INVISIBLE);
      imageView.setImageResource(tutorial.getImageId());
      button.setOnClickListener(
          v -> {
            tutorialId++;
            if (tutorialId < tutorials.size()) {
              setTutorial();
            } else {
              endTutorial();
            }
          });
      button.setText("Next");
      checkBox.setOnCheckedChangeListener(null);
      switchCompat.setOnCheckedChangeListener(null);
    }

    description.setText(tutorial.getDescription());
  }

  private void endTutorial() {
    description.setText("You have finished all the tutorials!");
    nonogramView.setVisibility(NonogramView.INVISIBLE);
    imageView.setVisibility(ImageView.INVISIBLE);
    switchCompat.setVisibility(SwitchCompat.INVISIBLE);
    checkBox.setVisibility(CheckBox.INVISIBLE);
    button.setText("Finish");
    button.setOnClickListener(
        v1 -> {
          finish();
        });
  }

  @Override
  public void onBackPressed() {
    new AlertDialog.Builder(this)
        .setTitle("Exit")
        .setMessage("Are you sure you want to exit?")
        .setPositiveButton(
            "Yes",
            (dialog, which) -> {
              dialog.dismiss();
              finish();
            })
        .setNegativeButton(
            "No",
            (dialog, which) -> {
              dialog.dismiss();
            })
        .create()
        .show();
  }
}
