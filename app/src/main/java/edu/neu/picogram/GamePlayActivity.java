package edu.neu.picogram;

import static edu.neu.picogram.NonogramGameConstants.getGames;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GamePlayActivity extends AppCompatActivity {

  RecyclerView smallScaleGameRecyclerView;
  List<Nonogram> games;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    games = getGames();
    setContentView(R.layout.activity_game_play);
    createRecyclerView();
  }

  public void createRecyclerView() {
    smallScaleGameRecyclerView = findViewById(R.id.recyclerView2);
    GameAdapter gameAdapter = new GameAdapter(this, games);
    smallScaleGameRecyclerView.setAdapter(gameAdapter);
    smallScaleGameRecyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager =
        new GridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false);
    smallScaleGameRecyclerView.setLayoutManager(layoutManager);
  }
}

class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

  private final List<Nonogram> games;
  private final Context context;

  public GameAdapter(Context context, List<Nonogram> games) {
    this.context = context;
    this.games = games;
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
          Intent intent = new Intent(context, GameActivity.class);
          intent.putExtra("index", position);
          context.startActivity(intent);
        });
  }

  @Override
  public int getItemCount() {
    return games.size();
  }

  public static class GameViewHolder extends RecyclerView.ViewHolder {

    private final TextView gameName;

    public GameViewHolder(@NonNull View itemView) {
      super(itemView);
      gameName = itemView.findViewById(R.id.name);
    }
  }
}
