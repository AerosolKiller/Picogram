package edu.neu.picogram;

import static android.content.Context.MODE_PRIVATE;
import static edu.neu.picogram.NonogramUtils.deleteJson;
import static edu.neu.picogram.NonogramUtils.drawNonogram;
import static edu.neu.picogram.gamedata.NonogramGameConstants.getGames;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
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
  SharedPreferences sharedPreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    smallScaleGames = getGames();
    largeScaleGames = LargeScaleGameConstants.getGames(this);
    setContentView(R.layout.activity_game_play);
    sharedPreferences = getSharedPreferences("game_progress", MODE_PRIVATE);

    SwitchCompat unlockAll = findViewById(R.id.unLockALl);
    unlockAll.setOnClickListener(
        v -> {
          sharedPreferences.edit().putBoolean("unlockAll", unlockAll.isChecked()).apply();
          createRecyclerView();
        });
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

  @Override
  public void onResume() {
    super.onResume();
    createRecyclerView();
  }
}

class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

  private final List<Nonogram> games;
  private final Context context;
  private final String mode;
  SharedPreferences sharedPreferences;

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
    // 设置每个图像的宽度和高度
    ViewGroup.LayoutParams layoutParams = holder.gameImage.getLayoutParams();
    if (mode.equals("large")) {
      layoutParams.width = 200;
      layoutParams.height = 200;
    } else {
      layoutParams.width = 100;
      layoutParams.height = 100;
    }

    boolean isUnlocked = position <= loadGameProgress() || loadIsUnlocked();
    holder.itemView.setOnClickListener(
        v -> {
          if (mode.equals("large")) {
            Intent intent = new Intent(context, BigScaleGameActivity.class);
            intent.putExtra("index", position);
            context.startActivity(intent);
            return;
          }
          if (!isUnlocked) {
            return;
          }
          Intent intent = new Intent(context, GameActivity.class);
          intent.putExtra("mode", "small");
          intent.putExtra("index", position);
          context.startActivity(intent);
        });
    holder.itemView.setOnLongClickListener(
        v -> {
          if (!mode.equals("large")) {
            return false;
          }
          AlertDialog.Builder builder = new AlertDialog.Builder(context);
          builder.setMessage("Are you sure to delete this game?")
                  .setPositiveButton("Yes", (dialog, which) -> {
                    // 删除数据
                    deleteJson(context, games.get(holder.getAdapterPosition()).getName());
                    games.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                  })
                  .setNegativeButton("No", null)
                  .show();
          return true;
        });
    if (mode.equals("large")) {
      holder.gameImage.setImageBitmap(drawNonogram(game, 300));
    } else if (isUnlocked) {
      holder.gameImage.setImageBitmap(drawNonogram(game));
    } else {
      holder.gameImage.setImageResource(R.drawable.baseline_question_mark_24);
    }
  }

  private int loadGameProgress() {
    sharedPreferences = context.getSharedPreferences("game_progress", MODE_PRIVATE);
    return sharedPreferences.getInt("current_level", 4);
  }

  private boolean loadIsUnlocked() {
    sharedPreferences = context.getSharedPreferences("game_progress", MODE_PRIVATE);
    return sharedPreferences.getBoolean("unlockAll", false);
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
