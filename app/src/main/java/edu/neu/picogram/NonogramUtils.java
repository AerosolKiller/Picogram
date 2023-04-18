package edu.neu.picogram;

import static android.content.ContentValues.TAG;

import static edu.neu.picogram.CommunityActivity.nonogramList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONObject;

public class NonogramUtils {

  public static JSONObject saveGame(String name, int[][] rowClues, int[][] colClues, int[][] solution) {
    // 把当前游戏的状态转换为json数组，然后转换为json字符串
    // 当前只是在Logcat中打印出来，后续会把json字符串保存为文件
    JSONArray jsonArray = new JSONArray(Arrays.asList(rowClues));
    JSONArray jsonArray1 = new JSONArray(Arrays.asList(colClues));
    JSONArray jsonArray2 = new JSONArray(Arrays.asList(solution));
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("name", name);
      jsonObject.put("rowClues", jsonArray);
      jsonObject.put("colClues", jsonArray1);
      jsonObject.put("solution", jsonArray2);
      jsonObject.put("width", rowClues.length);
        jsonObject.put("height", colClues.length);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return jsonObject;
  }

  public static Nonogram restoreGame(Context context, File file) {
    try {
      InputStream inputStream = Files.newInputStream(file.toPath());
      return restoreGame(inputStream);
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }


  public static Nonogram restoreGame(Context context, int fileID) {
    try {
      InputStream inputStream = context.getResources().openRawResource(fileID);
      return restoreGame(inputStream);
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }

  public static Nonogram restoreGame(InputStream inputStream) {
    int[][] rowClues;
    int[][] colClues;
    int[][] solution;
    try {
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int length;
      while ((length = inputStream.read(buffer)) != -1) {
        result.write(buffer, 0, length);
      }
      inputStream.close();
      String jsonString = result.toString("UTF-8");
      JSONObject jsonObject = new JSONObject(jsonString);
      JSONArray jsonArray = jsonObject.getJSONArray("rowClues");
      JSONArray jsonArray1 = jsonObject.getJSONArray("colClues");
      JSONArray jsonArray2 = jsonObject.getJSONArray("solution");
      String name = jsonObject.getString("name");
      int width = Integer.parseInt(jsonObject.getString("width"));
      int height = Integer.parseInt(jsonObject.getString("height"));
      rowClues = new int[jsonArray.length()][];
      colClues = new int[jsonArray1.length()][];
      solution = new int[jsonArray2.length()][];
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONArray jsonArray3 = jsonArray.getJSONArray(i);
        int[] ints = new int[jsonArray3.length()];
        for (int j = 0; j < jsonArray3.length(); j++) {
          ints[j] = jsonArray3.getInt(j);
        }
        rowClues[i] = ints;
      }
      for (int i = 0; i < jsonArray1.length(); i++) {
        JSONArray jsonArray3 = jsonArray1.getJSONArray(i);
        int[] ints = new int[jsonArray3.length()];
        for (int j = 0; j < jsonArray3.length(); j++) {
          ints[j] = jsonArray3.getInt(j);
        }
        colClues[i] = ints;
      }
      for (int i = 0; i < jsonArray2.length(); i++) {
        JSONArray jsonArray3 = jsonArray2.getJSONArray(i);
        int[] ints = new int[jsonArray3.length()];
        for (int j = 0; j < jsonArray3.length(); j++) {
          ints[j] = jsonArray3.getInt(j);
        }
        solution[i] = ints;
      }
      return new Nonogram(name, width, height, rowClues, colClues, solution);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static int[] reverseList(int[] array) {
    // 反转数组，方便绘制提示数字
    int[] result = new int[array.length];
    for (int i = 0; i < array.length; i++) {
      result[i] = array[array.length - i - 1];
    }
    return result;
  }

  public static void updateGame(Nonogram game) {
    // 点击之后，更新游戏内部的solution和clues
    // 根据当前的grid，计算出solution
    int[][] grid = new int[game.getHeight()][game.getWidth()];
    for (int row = 0; row < game.getHeight(); row++) {
      for (int col = 0; col < game.getWidth(); col++) {
        if (game.getCurrentGrid(row, col) == 1) {
          grid[row][col] = 1;
        }
      }
    }
    game.setSolution(grid);
    // 计算出rowClues，为了方便先用List<List<Integer>>，最后转换成int[][]
    List<List<Integer>> rowClues = new ArrayList<>();
    int count = 0;
    for (int row = 0; row < game.getHeight(); row++) {
      List<Integer> rowClue = new ArrayList<>();
      for (int col = 0; col < game.getWidth(); col++) {
        if (grid[row][col] == 1) {
          count++;
          if (col == game.getWidth() - 1) {
            rowClue.add(count);
            count = 0;
          }
        } else {
          if (count > 0) {
            rowClue.add(count);
            count = 0;
          }
        }
      }
      rowClues.add(rowClue);
    }
    int[][] rowCluesArray = rowClues.stream().map(NonogramUtils::toIntArray).toArray(int[][]::new);
    game.setRowClues(rowCluesArray);
    // 计算出colClues，为了方便先用List<List<Integer>>，最后转换成int[][]
    List<List<Integer>> colClues = new ArrayList<>();
    count = 0;
    for (int col = 0; col < game.getWidth(); col++) {
      List<Integer> colClue = new ArrayList<>();
      for (int row = 0; row < game.getHeight(); row++) {
        if (grid[row][col] == 1) {
          count++;
          if (row == game.getHeight() - 1) {
            colClue.add(count);
            count = 0;
          }
        } else {
          if (count > 0) {
            colClue.add(count);
            count = 0;
          }
        }
      }
      colClues.add(colClue);
    }
    int[][] colCluesArray = colClues.stream().map(NonogramUtils::toIntArray).toArray(int[][]::new);
    game.setColClues(colCluesArray);
  }

  private static int[] toIntArray(List<Integer> list) {
    // 辅助函数方便把List<Integer>转换成int[]
    return list.stream().mapToInt(Integer::intValue).toArray();
  }

  public static Bitmap drawNonogram(Nonogram game, int... targetSize) {
    int[][] solution = game.getSolution();
    int size = 100;
    if (targetSize.length != 0) {
      size = targetSize[0];
    }
    int ratio = Math.max(1, Math.min(size / game.getWidth(), size / game.getHeight()));
    Bitmap bitmap =
        Bitmap.createBitmap(
            game.getWidth() * ratio, game.getHeight() * ratio, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    paint.setColor(Color.BLACK);
    paint.setStyle(Paint.Style.FILL);
    for (int row = 0; row < game.getHeight(); row++) {
      for (int col = 0; col < game.getWidth(); col++) {
        if (solution[row][col] == 1) {
          canvas.drawRect(col * ratio, row * ratio, (col + 1) * ratio, (row + 1) * ratio, paint);
        }
      }
    }
    return bitmap;
  }

  // 保存json文件, 保存在本机
  public static void savaJson(Context context, JSONObject jsonObject, String fileName) {
    // Save json file
    try {
      File dir = new File(context.getExternalFilesDir(null), "nonogram");
        if (!dir.exists()) {
            boolean success = dir.mkdir();
            if (!success) {
            return;
            }
        }
        File file = new File(dir, fileName + ".json");
      Log.d("NonogramUtils", "savaJson: " + file.getAbsolutePath());
      if (!file.exists()) {
        boolean success = file.createNewFile();
        if (!success) {
          return;
        }
      }
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      fileOutputStream.write(jsonObject.toString().getBytes());
      fileOutputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 转换int[][] 到 String
  public static String convertArrayToString(int[][] array) {
    StringBuilder stringBuilder = new StringBuilder();

    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array[i].length; j++) {
        stringBuilder.append(array[i][j]);
        if (j < array[i].length - 1) {
          stringBuilder.append(", ");
        }
      }
      if (i < array.length - 1) {
        stringBuilder.append("; ");
      }
    }
    return stringBuilder.toString();
  }

  // 转换strings 到 2d array
  public static int[][] convertStringToArray(String input) {
    String[] rows = input.split(";\\s*");
    int[][] array = new int[rows.length][];

    for (int i = 0; i < rows.length; i++) {
      String[] cols = rows[i].split(",\\s*");
      ArrayList<Integer> validCols = new ArrayList<>();

      for (String col : cols) {
        if (!col.isEmpty()) {
          validCols.add(Integer.parseInt(col));
        }
      }

      array[i] = new int[validCols.size()];
      for (int j = 0; j < validCols.size(); j++) {
        array[i][j] = validCols.get(j);
      }
    }

    return array;
  }

  public static Task<Integer> saveNonogramToFireStore(
      String name,
      int width,
      int height,
      String rowClues,
      String colClues,
      String solution,
      String creator,
      int likedNum,
      String createTime) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TaskCompletionSource<Integer> taskCompletionSource = new TaskCompletionSource<>();

    SerializableNonogram serializableGame =
        new SerializableNonogram(
            name,
            width,
            height,
            rowClues,
            colClues,
            solution,
            creator,
            likedNum,
            createTime);

    db.collection("games")
        .document(name)
        .set(serializableGame)
        .addOnSuccessListener(
            aVoid -> {
              Log.d("Firestore", "Nonogram saved successfully");
              taskCompletionSource.setResult(1);
            })
        .addOnFailureListener(
            e -> {
              Log.w("Firestore", "Error saving nonogram", e);
              taskCompletionSource.setResult(-1);
            });
    return taskCompletionSource.getTask();
  }

  public static Nonogram getNonogramFromFireStore(String gameName) {
    Log.d("gameList", nonogramList.toString());

    for (UserNonogram game : nonogramList) {
        if (game.getName().equals(gameName)) {
            return game;
        }
    }
    return null;
  }


  // 从数据库中获取所有的nonogram的List
  public static List<UserNonogram> getNonogramListFromFireStore() {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<UserNonogram> nonogramList = new ArrayList<>();
    db.collection("games")
        .get()
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                  nonogramList.add(document.toObject(UserNonogram.class));
                }
              } else {
                Log.w("Firestore", "Error getting nonogram list", task.getException());
              }
            });
    return nonogramList;
  }

  public static void addPlayedSmallGameToUser(String userId, String name) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection("users")
        .document(userId)
        .update("playedSmallGameList", FieldValue.arrayUnion(name))
        .addOnSuccessListener(
            aVoid -> {
              Log.d("Firestore", "Played game added successfully");
            })
        .addOnFailureListener(
            e -> {
              Log.w("Firestore", "Error adding played game", e);
            });
  }

  public static void addGameToUserCreatedGames(String name) {
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    if (currentUser != null) {
      String userId = currentUser.getUid();
      FirebaseFirestore db = FirebaseFirestore.getInstance();

      DocumentReference userRef = db.collection("users")
              .document(userId);
      userRef
          .update("creationGameList", FieldValue.arrayUnion(name))
          .addOnSuccessListener(
              aVoid -> Log.d(TAG, "Successfully added gameId to user's createdGames"))
          .addOnFailureListener(e -> Log.e(TAG, "Failed to add gameId to user's createdGames", e));
    }
  }

  public static void addGameToUserLikedList() {

  }

  public static void saveGameHelper(UserNonogram game) {
    String rowCluesList = Arrays.deepToString(game.getRowClues());
    String colCluesList = Arrays.deepToString(game.getColClues());
    String solutionList = Arrays.deepToString(game.getSolution());

    NonogramUtils.saveNonogramToFireStore(
        game.getName(),
        game.getWidth(),
        game.getHeight(),
        rowCluesList,
        colCluesList,
        solutionList,
        game.getCreator(),
        game.getLikedNum(),
        game.getCreateTime());
  }

  public static void saveCreator(String userName, String name) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection("games")
        .document(name)
        .update("creator", userName)
        .addOnSuccessListener(
            aVoid -> {
              Log.d("FireStore", "creator saved successfully");
            })
        .addOnFailureListener(
            e -> {
              Log.w("Firestore", "Error saveing creator name", e);
            });
  }

  public static String generateGameName(String prefix) {
    Instant instant = Instant.now();
    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC"));
    String timeStamp = formatter.format(instant);
    return prefix + timeStamp;
  }

  // , OnSuccessListener<String> sListener
  public static CompletableFuture<String> fetchUsername(String userId) {
    CompletableFuture<String> completableFuture = new CompletableFuture<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    db.collection("users")
        .document(userId)
        .get()
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                  String username = document.getString("username");
                  completableFuture.complete(username);
                } else {
                  completableFuture.completeExceptionally(new Exception("User document not found"));
                }
              } else {
                completableFuture.completeExceptionally(task.getException());
              }
            });

    return completableFuture;
  }

  public static CompletableFuture<Integer> getLikedNumFromFirestore(String gameName) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    if (db == null) {
      throw new IllegalStateException("FirebaseFirestore instance is not initialized.");
    }

    CompletableFuture<Integer> future = new CompletableFuture<>();

    db.collection("games")
            .document(gameName)
            .get()
            .addOnCompleteListener(task -> {
              if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                  Integer likedNum = document.getLong("likedNum").intValue();
                  future.complete(likedNum);
                } else {
                  future.completeExceptionally(new Exception("Game not found."));
                }
              } else {
                Log.w(TAG, "Error getting likedNum", task.getException());
                future.completeExceptionally(task.getException());
              }
            });

    return future;
  }

//  public static User getCurrentUser() {
//    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//    Consumer mAuthStateListener = firebaseAuth -> {
//      user = firebaseAuth.getCurrentUser();
//      if (user != null) {
//        // User is signed in
//        Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
//      } else {
//        // User is signed out
//        Log.d("TAG", "onAuthStateChanged:signed_out");
//      }
//
//    };
//    return new User(currentUser.getUid(), currentUser.getDisplayName());
//  }


  // TODO: 2020/4/26 从数据库中获取当前用户的信息
//  public static User getUserFromFirestore() {
//    FirebaseAuth mAuth;
//    FirebaseAuth.AuthStateListener mAuthStateListener;
//    FirebaseFirestore db;
//    final FirebaseUser[] firebaseUser = new FirebaseUser[1];
//    final String[] uid = new String[1];
//    final User[] user = {null};
//
//    mAuth = FirebaseAuth.getInstance();
//    db = FirebaseFirestore.getInstance();
//
//    mAuthStateListener = firebaseAuth -> {
//      firebaseUser[0] = firebaseAuth.getCurrentUser();
//      if (firebaseUser[0] != null) {
//        uid[0] = firebaseUser[0].getUid();
//        fetchUsername(uid[0]);
//      }
//    };
//
//    db = FirebaseFirestore.getInstance();
//    db.collection("users").document(uid[0])
//            .get()
//            .addOnSuccessListener(documentSnapshot -> {
//              if (documentSnapshot.exists()) {
//                user[0] = documentSnapshot.toObject(User.class);
//                // Access user attributes
//                String username = user[0].getUsername();
//                String email = user[0].getEmail();
//                List<String> likedGameList = user[0].getLikedGameList();
//                List<String> collectedGameList = user[0].getCollectedGameList();
//                List<String> creationGameList = user[0].getCreationGameList();
//              }
//            })
//            .addOnFailureListener(e -> {
//              Log.d("TAG", "Error getting user", e);
//            });
//
//    return user[0];
//  }



}
