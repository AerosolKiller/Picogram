package edu.neu.picogram;

import static android.content.ContentValues.TAG;
import static edu.neu.picogram.CommunityActivity.getUserFromFirestore;
import static edu.neu.picogram.NonogramUtils.drawNonogram;
import static edu.neu.picogram.NonogramUtils.getLikedNumFromFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CommunityActivity extends AppCompatActivity {

  // back button
  private ImageButton backButton;
  private Button popularButton;
  private Button newestButton;

  private Button favoriteButton;


  RecyclerView recyclerView;

  public static List<UserNonogram> nonogramList;

  public List<UserNonogram> testList;

  private FirebaseAuth mAuth;
  private FirebaseAuth.AuthStateListener mAuthStateListener;
  public static FirebaseUser user;

  private static FirebaseFirestore db;

  CommunityAdapter adapter = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_community);

    backButton = findViewById(R.id.imageButton1);
    popularButton = findViewById(R.id.popular);
    newestButton = findViewById(R.id.newest);
    favoriteButton = findViewById(R.id.favorite);

    mAuth = FirebaseAuth.getInstance();
    db = FirebaseFirestore.getInstance();

    mAuthStateListener = firebaseAuth -> {
      user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());

        } else {
            // User is signed out
            Log.d("TAG", "onAuthStateChanged:signed_out");
        }

    };

    mAuth.addAuthStateListener(mAuthStateListener);


    // 找到控件recyclerview
    recyclerView = findViewById(R.id.recyclerView);

    // 准备数据
    initData();


    // the recycler view is in horizontal mode
    LinearLayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false);
    recyclerView.setLayoutManager(layoutManager);


    // the adapter is set to the recycler view
    if(adapter == null) adapter = new CommunityAdapter(this, nonogramList);
    recyclerView.setAdapter(adapter);


    popularButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        //sortByPopularity(nonogramList);
        adapter.sortByPopular();
        Log.d("TAG", nonogramList.toString());
        //
        adapter.notifyDataSetChanged();

      }
    });


    newestButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        adapter.sortByNewest();
        Log.d("TAG", nonogramList.toString());
        adapter.notifyDataSetChanged();
      }
    });

    favoriteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        // check if the user is not logged in, then jump to the login page
        if (user == null) {
          // User is signed out
//          Toast.makeText(CommunityActivity.this, "Please login firstly.", Toast.LENGTH_SHORT).show();
            // snack bar to pop up and ask the user to login
          startSnackbar(v);
        }
        else {
          // if the user is logged in, then list the favorite nonograms

        }
      }
    });


    // 找到主页的底部导航栏，设置监听器，点击不同的按钮跳转到不同的页面
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
    bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
      if( this.getClass().equals(MainActivity.class)) {
        return true;
      }
      switch (item.getItemId()) {
        case R.id.nav_play:
          Intent intent1 = new Intent(CommunityActivity.this, MainActivity.class);
          startActivity(intent1);
          break;
        case R.id.nav_create:
          Intent intent2 = new Intent(CommunityActivity.this, EditActivity.class);
          startActivity(intent2);
          break;

      }
      return true;
    });
  }

  public static CompletableFuture<User> getUserFromFirestore(String uid) {
    db = FirebaseFirestore.getInstance();

    CompletableFuture<User> future = new CompletableFuture<>();

    db.collection("users")
            .document(uid)
            .get()
            .addOnCompleteListener(task -> {
              if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                  User user = document.toObject(User.class);
                  future.complete(user);
                } else {
                  future.completeExceptionally(new Exception("User not found."));
                }
              } else {
                Log.w(TAG, "Error getting user", task.getException());
                future.completeExceptionally(task.getException());
              }
            });

    return future;
  }

  private void initData() {
    // List<Data> list ---> Apdater ---> setAdapter ---> 显示数据
    nonogramList = new ArrayList<>();

    //创建数据对象；
    GameRepository gameRepository = new GameRepository();
    gameRepository.fetchAllGames()
            .thenAccept(games -> {
              nonogramList.addAll(games);
              //Log.d("gameList", nonogramList.toString());
              adapter = new CommunityAdapter(this, nonogramList);
              //设置到Recycler view里面去
              recyclerView.setAdapter(adapter);
            })
            .exceptionally(throwable -> {
              // Handle any error that occurs during the fetch operation
              Log.e("TAG", "Error fetching games: " + throwable.getMessage());
              return null;
              });
  }


  public void startSnackbar(View view) {
    Snackbar.make(view, "Please Login Firstly.", Snackbar.LENGTH_LONG)
            .setAction("Login", new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                Snackbar.make(view, "", Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(CommunityActivity.this, SettingActivity.class);
                startActivity(intent);
              }
            }).show();
  }
}

class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {

  private Context context;
  private List<UserNonogram> nonogramList;
  FirebaseFirestore db;


  public CommunityAdapter(Context context, List<UserNonogram> nonogramList) {
    this.context = context;
    this.nonogramList = nonogramList;
  }

  // update the nonogram list by popularity
  public void sortByPopular() {
    this.nonogramList = (ArrayList<UserNonogram>) nonogramList.stream()
            .sorted((a, b) -> b.getLikedNum() - a.getLikedNum())
            .collect(Collectors.toList());
//    Log.d("TAG", nonogramList.toString());
  }

  // update the nonogram list by newest
    public void sortByNewest() {
        this.nonogramList = (ArrayList<UserNonogram>) nonogramList.stream()
                .sorted((a, b) -> b.getCreateTime().compareTo(a.getCreateTime()))
                .collect(Collectors.toList());
    }



  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // 这里view就是单个item条目的界面
    View view = LayoutInflater.from(context).inflate(R.layout.activity_item_card, parent, false);
    return new ViewHolder(view);
  }



  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    // 绑定数据
    UserNonogram nonogram = nonogramList.get(position);
    holder.setData(nonogram);

    // get the user likedgameList, compare with the nonogram name, if the nonogram is in the list, then set the like button to be red.
    db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    if(user == null) {
      holder.likeButton.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
    }
    else {
      String uid = user.getUid();
      getUserFromFirestore(uid)
              .thenAccept(user1 -> {
                boolean isLiked = false;
                for(String gameName : user1.getLikedGameList()) {
                  if(gameName.equals(nonogram.getName())) {
                    isLiked = true;
                    break;
                  }
                }
                if(isLiked) {
                  holder.likeButton.setImageResource(R.drawable.baseline_thumb_up_alt_24);
                }
                else {
                  holder.likeButton.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
                }
              });
    }
    // if not, set the like button to be white

    holder.imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra("nonogram", nonogram.getName());
        intent.putExtra("mode", "firebase");
        context.startActivity(intent);
      }
    });

    // set the like button click listener
    holder.likeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick (View v){
        if(user == null) {
          Toast.makeText(context, "Please signin firstly!", Toast.LENGTH_SHORT).show();
          return;
        }
        String uid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        getUserFromFirestore(uid)
                .thenAccept(user1 -> {
                  boolean isLiked = false;
                  for(String gameName : user1.getLikedGameList()) {
                    if (gameName.equals(nonogram.getName())) {
                      isLiked = true;
                      break;
                    }
                  }

                  if(!isLiked) {
                    //user1.getLikedGameList().add(nonogram.getName());
                    //(nonogram).setLikedNum((nonogram).getLikedNum() + 1);
                    // 把当前的nonogram从likedGameList里面添加
                    db.collection("users").document(uid).update("likedGameList", FieldValue.arrayUnion(nonogram.getName()));
                    DocumentReference userRef = db.collection("games").document(nonogram.getName());

//                    userRef.update("likedNum", FieldValue.increment(1));
                    if(nonogram.getLikedNum() < 0) nonogram.setLikedNum(0);
                    nonogram.setLikedNum(nonogram.getLikedNum() + 1);
                    userRef.update("likedNum", nonogram.getLikedNum());
                    holder.likeButton.setImageResource(R.drawable.baseline_thumb_up_alt_24);

                    getLikedNumFromFirestore(nonogram.getName())
                            .thenAccept(likedNum -> {
                              holder.likeNum.setText(String.valueOf(likedNum));
                            })
                            .exceptionally( e-> {
                                      Log.w(TAG, "Error getting likedNum", e);
                                      return null;
                                    });
                    isLiked = true;
                  } else{
                    // 把当前的nonogram从likedGameList里面删除
                    db.collection("users").document(uid).update("likedGameList", FieldValue.arrayRemove(nonogram.getName()));
//                    user1.getLikedGameList().remove(nonogram.getName());
//                    (nonogram).setLikedNum(nonogram.getLikedNum() - 1);
                    DocumentReference userRef = db.collection("games").document(nonogram.getName());

                    if(nonogram.getLikedNum() < 0) nonogram.setLikedNum(0);
                    nonogram.setLikedNum(nonogram.getLikedNum() - 1);
                    userRef.update("likedNum", nonogram.getLikedNum());
                    holder.likeButton.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
                    getLikedNumFromFirestore(nonogram.getName())
                            .thenAccept(likedNum -> {
                              holder.likeNum.setText(String.valueOf(likedNum));
                            })
                            .exceptionally( e-> {
                              Log.w(TAG, "Error getting likedNum", e);
                              return null;
                            });
                    isLiked = false;
                  }
                })
                .exceptionally(e -> {
                  Log.w(TAG, "Error getting user", e);
                  return null;
                });
      }
    });
  }


  // 返回数据的个数
  @Override
  public int getItemCount() {
    if (nonogramList != null) return nonogramList.size();
    else return 0;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView textView;
    private ImageButton likeButton;
    private TextView likeNum;


    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      // 找到控件
      imageView = itemView.findViewById(R.id.image);
      textView = itemView.findViewById(R.id.name);
      likeButton = itemView.findViewById(R.id.like_button);
      likeNum = itemView.findViewById(R.id.likedNumber);

    }

    // 绑定数据

    public void setData(UserNonogram nonogram) {
      imageView.setImageBitmap(drawNonogram(nonogram));
      textView.setText(nonogram.getName());
      if(nonogram.getLikedNum() > 0) likeNum.setText(nonogram.getLikedNum() + "");
      else likeNum.setText("0");
    }
  }

}

