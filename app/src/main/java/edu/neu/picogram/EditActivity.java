package edu.neu.picogram;

import static android.content.ContentValues.TAG;
import static edu.neu.picogram.NonogramUtils.addGameToUserCreatedGames;
import static edu.neu.picogram.NonogramUtils.convertArrayToString;
import static edu.neu.picogram.NonogramUtils.fetchUsername;
import static edu.neu.picogram.NonogramUtils.saveGame;
import static edu.neu.picogram.NonogramUtils.saveNonogramToFireStore;
import static edu.neu.picogram.NonogramUtils.updateGame;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class EditActivity extends AppCompatActivity {
  private int[][] colClues;
  private int[][] rowClues;
  private int[][] solution;
  String colString;
  String rowString;
  String solutionString;

  private String creator;
  private String createTime;
  private String gameName = "";
  private int width = 0;
  private int height = 0;
  private int likedNum = 0;
  UserNonogram createdGame;
  private FirebaseAuth.AuthStateListener mAuthStateListener;

  private FirebaseFirestore db;
  private FirebaseUser user;

  TextInputEditText inputGameName;
  Button saveNameButton;
  Button cancelButton;
  NonogramView nonogramView;
  Uri bitmapUri;
  Nonogram game;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit);
    // access database
    db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Intent intent = getIntent();
    if (intent.getData() != null) {
      bitmapUri = intent.getParcelableExtra("BitmapUri");
      // 使用Uri数据进行操作
    }

    if (bitmapUri != null) {
      Log.d(TAG, "uri is not null");
      UserNonogram userNonogram = new UserNonogram();
      // userNonogram.setBitmapUri(bitmapUri);
    } else {
      Log.d(TAG, "uri is null");
    }

    mAuthStateListener = firebaseAuth -> {
      user = firebaseAuth.getCurrentUser();
      if (user != null) {

        String uid = user.getUid();
        fetchUsername(uid)
            .thenAccept(
                username -> {
                  Log.d(TAG, "Fetched username: " + username);
                  creator = username;
                })
            .exceptionally(
                throwable -> {
                  Log.e(TAG, "Error fetching username: " + throwable.getMessage());
                  return null;
                });
      }
    };
    mAuth.addAuthStateListener(mAuthStateListener);
    nonogramView = findViewById(R.id.nonogramView);
    nonogramView.setEditMode(true);
    // 创建一个全0的5*5的游戏，作为初始状态，传入NonogramEditView
    game = createEmptyGame(5, 5);
    nonogramView.setGame(game);
    // 保存按钮，点击后将游戏保存为json格式
    Spinner spinner = findViewById(R.id.spinner);
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        this, R.array.spinner_items, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    spinner.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (position == 0) {
              game = createEmptyGame(5, 5);
              nonogramView.setGame(game);
            }
            if (position == 1) {
              game = createEmptyGame(10, 10);
              nonogramView.setGame(game);
            }
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {
          }
        });

    Button button = findViewById(R.id.saveAnswer);
    button.setOnClickListener(
        v -> {
          // save game data
          Nonogram newGame = nonogramView.getGame();
          if (newGame != null) {
            createUserNonogram(newGame);
          }
        });
    Button resetButton = findViewById(R.id.reset);
    resetButton.setOnClickListener(
        v -> {
          // reset game data
          Nonogram newGame = nonogramView.getGame();
          if (newGame != null) {
            newGame.setCurrentGrid(new int[newGame.getWidth()][newGame.getHeight()]);
            updateGame(newGame);
            nonogramView.invalidate();
          }
        });
  }

  private void createUserNonogram(Nonogram newGame) {
    rowClues = newGame.getRowClues();
    rowString = Arrays.deepToString(rowClues);
    colClues = newGame.getColClues();
    colString = Arrays.deepToString(colClues);
    solution = newGame.getSolution();
    solutionString = Arrays.deepToString(solution);
    saveGame(gameName, rowClues, colClues, solution);
    width = newGame.getWidth();
    height = newGame.getHeight();
    // create time
    Instant instant = Instant.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .withZone(ZoneId.of("UTC"));
    createTime = formatter.format(instant);
    createdGame = new UserNonogram(
        gameName, creator, likedNum, createTime, width, height, rowClues, colClues, solution);
    showSaveGameDialog();
    // UserNonogram createdGame = new UserNonogram();
  }

  // create a dialog so users can edit their own game name
  private void showSaveGameDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    // Inflate the dialog layout
    LayoutInflater inflater = this.getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.saveeditgame, null);

    // Find the input field and buttons in the dialog layout
    inputGameName = dialogView.findViewById(R.id.gameName_edit_text);
    saveNameButton = dialogView.findViewById(R.id.save_name_button);
    cancelButton = dialogView.findViewById(R.id.cancel_button);

    // Create the dialog
    builder.setView(dialogView);
    AlertDialog dialog = builder.create();
    dialog.show();

    // Set up click listeners for the buttons
    saveNameButton.setOnClickListener(v -> {
      // catch game name input
      String gameName = inputGameName.getText().toString() + "_" + creator;
      checkGameName(gameName)
          .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
              int result = task.getResult();
              if (result == -1) {
                Toast.makeText(this, "Game name already exists", Toast.LENGTH_LONG).show();
              } else if (result == 1) {
                createdGame.setName(gameName);
                saveNonogramToFireStore(createdGame.getName(),
                    createdGame.getWidth(),
                    createdGame.getHeight(),
                    rowString,
                    colString,
                    solutionString,
                    createdGame.getCreator(),
                    createdGame.getLikedNum(),
                    createdGame.getCreateTime())
                    .addOnCompleteListener(task1 -> {
                      if (task1.isSuccessful()) {
                        int result1 = task1.getResult();
                        if (result1 == 1) {
                          addGameToUserCreatedGames(createdGame.getName());
                          Toast.makeText(this, "Game saved successfully", Toast.LENGTH_LONG).show();
                        } else if (result1 == -1) {
                          Toast.makeText(this, "Failed to save this game", Toast.LENGTH_LONG).show();
                        } else {
                          Toast.makeText(this, "Failed to access this game", Toast.LENGTH_LONG).show();
                        }
                      }
                    });

              } else {
                Toast.makeText(this, "Error checking game name", Toast.LENGTH_LONG).show();
              }
            }
          });
      dialog.dismiss();
    });

    // cancel button
    cancelButton.setOnClickListener(v -> dialog.dismiss());
    // Show the dialog
    dialog.show();
  }

  private Task<Integer> checkGameName(String gameName) {
    if (db == null) {
      throw new IllegalStateException("FirebaseFirestore instance is not initialized.");
    }
    TaskCompletionSource<Integer> taskCompletionSource = new TaskCompletionSource<>();

    db.collection("games")
        .whereEqualTo("name", gameName)
        .get()
        .addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            // if game name exists
            if (!task.getResult().isEmpty()) {
              taskCompletionSource.setResult(-1);
            } else {
              taskCompletionSource.setResult(1);
            }
          } else {
            Toast.makeText(this, "Error checking game name", Toast.LENGTH_LONG).show();
          }
        });
    return taskCompletionSource.getTask();
  }

  public Nonogram createEmptyGame(int width, int height) {
    // 创建一个全0的5*5的游戏，作为初始状态，传入NonogramEditView
    rowClues = new int[height][];
    colClues = new int[width][];
    solution = new int[width][height];
    return new Nonogram("", width, height, rowClues, colClues, solution);
  }
}