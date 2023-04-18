package edu.neu.picogram;

import static edu.neu.picogram.NonogramUtils.drawNonogram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import edu.neu.picogram.gamedata.LargeScaleGameConstants;
import java.util.List;

public class BigScaleGameActivity extends AppCompatActivity {

  SharedPreferences sharedPreferences;
  int index;
  String gameName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_big_scale_game);
    GridLayout gridLayout = findViewById(R.id.gridLayout);
    index = getIntent().getIntExtra("index", 0);
    Nonogram BigGame = LargeScaleGameConstants.getGames(this).get(index);
    gameName = BigGame.getName();
    List<Nonogram> games = LargeScaleGameConstants.getGames(this, index);
    if (games == null) {
      Log.e("BigScaleGameActivity", "games is null");
      return;
    }
    gridLayout.setRowCount(BigGame.getHeight() / 10);
    gridLayout.setColumnCount(BigGame.getWidth() / 10);
    boolean isUnlocked = loadIsUnlocked();
    updateGrid(gridLayout, games, isUnlocked);
  }

  private void updateGrid(GridLayout gridLayout, List<Nonogram> games, boolean isUnlocked) {
    for (int i = 0; i < games.size(); i++) {
      Nonogram game = games.get(i);
      ImageView imageView = new ImageView(this);
      imageView.setImageBitmap(drawNonogram(game, 150));
      int finalI = i;
      if (!isUnlocked && i > loadGameProgress()) {
        imageView.setAlpha(0.5f);
        gridLayout.addView(imageView);
        continue;
      }
      imageView.setOnClickListener(
          v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("mode", "large");
            intent.putExtra("innerIndex", finalI);
            intent.putExtra("index", index);
            startActivity(intent);
          });
      gridLayout.addView(imageView);
    }
  }

  private int loadGameProgress() {
    sharedPreferences = getSharedPreferences("large_game_progress", MODE_PRIVATE);
    return sharedPreferences.getInt(gameName + "current_level", 0);
  }

  private boolean loadIsUnlocked() {
    sharedPreferences = getSharedPreferences("game_progress", MODE_PRIVATE);
    return sharedPreferences.getBoolean("unlockAll", false);
  }

  @Override
  public void onResume() {
    super.onResume();
    GridLayout gridLayout = findViewById(R.id.gridLayout);
    gridLayout.removeAllViews();
    List<Nonogram> games = LargeScaleGameConstants.getGames(this, index);
    if (games == null) {
      Log.e("BigScaleGameActivity", "games is null");
      return;
    }
    boolean isUnlocked = loadIsUnlocked();
    updateGrid(gridLayout, games, isUnlocked);
  }
}
