package edu.neu.picogram;

import static edu.neu.picogram.NonogramUtils.drawNonogram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import edu.neu.picogram.gamedata.LargeScaleGameConstants;
import java.util.List;

public class BigScaleGameActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_big_scale_game);
    GridLayout gridLayout = findViewById(R.id.gridLayout);
    int index = getIntent().getIntExtra("index", 0);
    List<Nonogram> games = LargeScaleGameConstants.getGames(this, index);
    if (games == null) {
      Log.e("BigScaleGameActivity", "games is null");
      return;
    }
    gridLayout.setRowCount(LargeScaleGameConstants.getGameHeight(this, index) / 10);
    gridLayout.setColumnCount(LargeScaleGameConstants.getGameWidth(this, index) / 10);
    for (int i = 0; i < games.size(); i++) {
      Nonogram game = games.get(i);
      ImageView imageView = new ImageView(this);
      imageView.setImageBitmap(drawNonogram(game, 150));
      int finalI = i;
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
}
