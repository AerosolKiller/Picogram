package edu.neu.picogram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class GamePlayActivity extends AppCompatActivity {

  ImageButton homeButton;
  RecyclerView smallScaleGameRecyclerViewclerView;
  List<GameItem> games;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    games = new ArrayList<>();
    games.add(new GameItem("level1", R.raw.level1));
    games.add(new GameItem("level2", R.raw.level2));
    games.add(new GameItem("level3", R.raw.level3));
    games.add(new GameItem("level11", R.raw.level11));
    games.add(new GameItem("level21", R.raw.level12));
    setContentView(R.layout.activity_game_play);
    createRecyclerView();

    homeButton = findViewById(R.id.homeButton);
    homeButton.setOnClickListener(v -> backHome());
  }

  public void createRecyclerView() {
    smallScaleGameRecyclerViewclerView = findViewById(R.id.recyclerView2);
    GameAdapter gameAdapter = new GameAdapter(this, games);
    smallScaleGameRecyclerViewclerView.setAdapter(gameAdapter);
    smallScaleGameRecyclerViewclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
    smallScaleGameRecyclerViewclerView.setLayoutManager(layoutManager);
  }

  private void backHome() {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }
}

class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

  private List<GameItem> games;
  private Context context;

  public GameAdapter(Context context, List<GameItem> games) {
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
    GameItem game = games.get(position);
    holder.gameName.setText(game.getGameName());
    holder.itemView.setOnClickListener(
        v -> {
          Intent intent = new Intent(context, GameActivity.class);
          intent.putExtra("level", game.getLevel());
          context.startActivity(intent);
        });
  }

  @Override
  public int getItemCount() {
    return games.size();
  }

  public class GameViewHolder extends RecyclerView.ViewHolder {

    private TextView gameName;

    public GameViewHolder(@NonNull View itemView) {
      super(itemView);
      gameName = itemView.findViewById(R.id.name);
    }
  }

}
