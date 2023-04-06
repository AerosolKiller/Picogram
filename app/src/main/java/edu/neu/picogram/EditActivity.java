package edu.neu.picogram;

import static edu.neu.picogram.NonogramUtils.fetchUserName;
import static edu.neu.picogram.NonogramUtils.generateGameName;
import static edu.neu.picogram.NonogramUtils.saveGame;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class EditActivity extends AppCompatActivity {
  private int[][] colClues;
  private int[][] rowClues;
  private int[][] solution;

  private String creator;
  private String createTime;
  private String gameName;
  private int width = 0;
  private int height = 0;
  private int likedNum = 0;
  private FirebaseAuth.AuthStateListener mAuthStateListener;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit);
    // access database
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // find user
    mAuthStateListener = firebaseAuth -> {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            // update creator in database
            //fetchUserName(uid, );
        }
    };

    NonogramView nonogramView = findViewById(R.id.nonogramView);
    nonogramView.setEditMode(true);
    // 创建一个全0的5*5的游戏，作为初始状态，传入NonogramEditView
    width = 5;
    height = 5;
    rowClues = new int[height][];
    colClues = new int[width][];
    solution = new int[width][height];
    Nonogram game = new Nonogram("", width, height, rowClues, colClues, solution);
    nonogramView.setGame(game);
    // 保存按钮，点击后将游戏保存为json格式
    Button button = findViewById(R.id.saveAnswer);
    button.setOnClickListener(
        v -> {
          // create time
          Instant instant = Instant.now();
          DateTimeFormatter formatter =
                  DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC"));
          createTime = formatter.format(instant);

          // create unique game name based on time stamp
          String gameName = generateGameName("Game_");


          // save game data
          Nonogram newGame = nonogramView.getGame();
          saveGame(gameName, newGame.getRowClues(), newGame.getColClues(), newGame.getSolution());

          //UserNonogram createdGame = new UserNonogram();

        });

    //    boolean[][] solution = convertToNonogramMatrix(this, R.drawable.tutorial_img1, 200);
    //    JSONObject jsonObject = new JSONObject();
    //    try {
    //      JSONArray solutionArray = new JSONArray();
    //      for (int i = 0; i < solution.length; i++) {
    //        JSONArray rowArray = new JSONArray();
    //        for (int j = 0; j < solution[0].length; j++) {
    //          rowArray.put(solution[i][j]);
    //        }
    //        solutionArray.put(rowArray);
    //      }
    //      jsonObject.put("solution", solutionArray);
    //    } catch (Exception e) {
    //      Log.e("EditActivity", "Error when converting solution to json");
    //    }
    //    savaJson(this, jsonObject, "tutorial_img1");
  }
}
