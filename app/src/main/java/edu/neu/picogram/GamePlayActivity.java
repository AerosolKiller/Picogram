package edu.neu.picogram;

import static edu.neu.picogram.NonogramUtils.drawNonogram;
import static edu.neu.picogram.gamedata.NonogramGameConstants.getGames;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import edu.neu.picogram.gamedata.LargeScaleGameConstants;

public class GamePlayActivity extends AppCompatActivity {

  RecyclerView smallScaleGameRecyclerView;
  RecyclerView largeScaleGameRecyclerView;
  List<Nonogram> smallScaleGames;
  List<Nonogram> largeScaleGames;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    smallScaleGames = getGames();
    largeScaleGames = LargeScaleGameConstants.getGames(this);
    setContentView(R.layout.activity_game_play);
    createRecyclerView();
  }

  public void createRecyclerView() {
    smallScaleGameRecyclerView = findViewById(R.id.recyclerView1);
    largeScaleGameRecyclerView = findViewById(R.id.recyclerView2);
    GameAdapter gameAdapter = new GameAdapter(this, smallScaleGames, "small");
    smallScaleGameRecyclerView.setAdapter(gameAdapter);
    smallScaleGameRecyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager =
        new GridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false);
    smallScaleGameRecyclerView.setLayoutManager(layoutManager);
    GameAdapter gameAdapter2 = new GameAdapter(this, largeScaleGames, "large");
    largeScaleGameRecyclerView.setAdapter(gameAdapter2);
    largeScaleGameRecyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager2 =
        new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
    largeScaleGameRecyclerView.setLayoutManager(layoutManager2);
  }
}

class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

  private final List<Nonogram> games;
  private final Context context;
  private final String mode;

  public GameAdapter(Context context, List<Nonogram> games, String mode) {
    this.context = context;
    this.games = games;
    this.mode = mode;
  }

  @NonNull
  @Override
  public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.game_item, parent, false);
    return new GameViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
    Nonogram game = games.get(position);
    holder.gameName.setText(game.getName());
    holder.itemView.setOnClickListener(
        v -> {
          if (mode.equals("large")) {
            Intent intent = new Intent(context, BigScaleGameActivity.class);
            intent.putExtra("index", position);
            context.startActivity(intent);
            return;
          }
          Intent intent = new Intent(context, GameActivity.class);
          intent.putExtra("mode", "small");
          intent.putExtra("index", position);
          context.startActivity(intent);
        });
    if (mode.equals("large")) {
      holder.gameImage.setImageBitmap(drawNonogram(game, 300));
    } else {
      holder.gameImage.setImageBitmap(drawNonogram(game));
    }
  }

  @Override
  public int getItemCount() {
    return games.size();
  }

  public static class GameViewHolder extends RecyclerView.ViewHolder {

    private final TextView gameName;
    private final ImageView gameImage;

    public GameViewHolder(@NonNull View itemView) {
      super(itemView);
      gameName = itemView.findViewById(R.id.name);
      gameImage = itemView.findViewById(R.id.image);
    }
  }
}
