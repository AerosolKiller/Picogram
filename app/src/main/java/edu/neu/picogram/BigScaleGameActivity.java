package edu.neu.picogram;

import static edu.neu.picogram.NonogramUtils.drawNonogram;

import android.content.Intent;
import android.os.Bundle;
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
    List<Nonogram> games = LargeScaleGameConstants.getGames(this, R.raw.nonogram);
    for (int i = 0; i < games.size(); i++) {
      Nonogram game = games.get(i);
      ImageView imageView = new ImageView(this);
      imageView.setImageBitmap(drawNonogram(game, 150));
      int finalI = i;
      imageView.setOnClickListener(
          v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("mode", "large");
            intent.putExtra("index", finalI);
            startActivity(intent);
          });
      gridLayout.addView(imageView);
    }
  }
}
