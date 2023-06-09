package edu.neu.picogram;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SettingActivity extends AppCompatActivity {

  private FirebaseAuth mAuth;
  private FirebaseAuth.AuthStateListener mAuthStateListener;
  private Button signInOptionButton,
      createAccountOptionButton,
      signOutButton,
      helpButton,
      TutorialButton;
  private ImageButton homeButton;
  private FirebaseFirestore db;
  private FirebaseUser user;

  private TextView tv_signInOn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);

    // create buttons for create account and sign in
    signInOptionButton = findViewById(R.id.bt_signin);
    createAccountOptionButton = findViewById(R.id.bt_createAccount);
    homeButton = findViewById(R.id.ib_homeButton);
    tv_signInOn = findViewById(R.id.tv_signInOn);
    signOutButton = findViewById(R.id.bt_signOut);
    helpButton = findViewById(R.id.bt_help);
    TutorialButton = findViewById(R.id.bt_tutorial);

    mAuth = FirebaseAuth.getInstance();
    db = FirebaseFirestore.getInstance();

    mAuthStateListener =
        firebaseAuth -> {
          user = firebaseAuth.getCurrentUser();
          if (user != null) {
            signInOptionButton.setVisibility(View.GONE);
            createAccountOptionButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
            String uid = user.getUid();
            getUserFromFirestore(uid);
            fetchUsername(uid);
          }
        };

    signInOptionButton.setOnClickListener(v -> showSignInDialog());
    createAccountOptionButton.setOnClickListener(v -> showCreateAccountDialog());
    signOutButton.setOnClickListener(v -> signOutAccount());
    homeButton.setOnClickListener(v -> backToHome());
    helpButton.setOnClickListener(v -> helpPage());
    TutorialButton.setOnClickListener(v -> tutorialPage());
  }

  protected void onStart() {
    super.onStart();
    mAuth.addAuthStateListener(mAuthStateListener);
  }

  protected void onStop() {
    super.onStop();
    mAuth.removeAuthStateListener(mAuthStateListener);
  }

  private void showSignInDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    View view = inflater.inflate(R.layout.dialog_signin, null);

    EditText et_email = view.findViewById(R.id.email_edit_text);
    EditText et_password = view.findViewById(R.id.password_edit_text);
    Button signInToAccount = view.findViewById(R.id.sign_in_button);

    builder.setView(view);
    AlertDialog signInDialog = builder.create();
    signInDialog.show();

    signInToAccount.setOnClickListener(
        v -> {
          String email = et_email.getText().toString();
          String password = et_password.getText().toString();

          if (!email.isEmpty() && !password.isEmpty()) {
            signIn(email, password);
            signInDialog.dismiss();
          }
        });
  }

  private void signIn(String email, String password) {
    mAuth
        .signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(
            this,
            task -> {
              if (task.isSuccessful()) {
                Toast.makeText(this, "sign in successfully", Toast.LENGTH_SHORT).show();
              } else {
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
              }
            });
  }

  private void showCreateAccountDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    View view = inflater.inflate(R.layout.dialog_createaccount, null);

    EditText et_userName = view.findViewById(R.id.username_edit_text);
    EditText et_email = view.findViewById(R.id.email_edit_text);
    EditText et_password = view.findViewById(R.id.password_edit_text);
    Button createAccountButton = view.findViewById(R.id.create_account_button);

    builder.setView(view);
    AlertDialog createAccountDialog = builder.create();
    createAccountDialog.show();

    createAccountButton.setOnClickListener(
        v -> {
          String email = et_email.getText().toString();
          String password = et_password.getText().toString();
          String userName = et_userName.getText().toString();

          if (!email.isEmpty() && !password.isEmpty() && !userName.isEmpty()) {
            checkUsernameAndEmail(userName, email)
                .addOnCompleteListener(
                    task -> {
                      if (task.isSuccessful()) {
                        int result = task.getResult();
                        if (result == -1) {
                          Toast.makeText(
                                  this, "Username or email already exists", Toast.LENGTH_LONG)
                              .show();
                        } else if (result == 1) {
                          createAccount(email, password, userName);
                        } else {
                          Toast.makeText(this, "Failed to save user's info", Toast.LENGTH_LONG)
                              .show();
                        }
                      }
                    });
            createAccountDialog.dismiss();
          } else {
            Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_LONG).show();
          }
        });
  }

  private void createAccount(String email, String password, String userName) {
    mAuth
        .createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(
            this,
            task -> {
              if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();

                List<String> playedSmallGameList = new ArrayList<>();
                List<String> playedLargeGameList = new ArrayList<>();
                List<String> likedGameList = new ArrayList<>();
                List<String> collectedGameList = new ArrayList<>();
                List<String> creationGameList = new ArrayList<>();
                saveUserToFireStore(
                    uid,
                    userName,
                    email,
                    playedSmallGameList,
                    playedLargeGameList,
                    collectedGameList,
                    likedGameList,
                    creationGameList);
              } else {
                Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show();
              }
            });
  }

  private void saveUserToFireStore(
      String uid,
      String username,
      String email,
      List<String> playedSmallGameList,
      List<String> playedLargeGameList,
      List<String> collectedGameList,
      List<String> likedGameList,
      List<String> creationGameList) {
    User user =
        new User(
            username,
            email,
            playedSmallGameList,
            playedLargeGameList,
            collectedGameList,
            likedGameList,
            creationGameList);

    db.collection("users")
        .document(uid)
        .set(user)
        .addOnSuccessListener(
            aVoid -> {
              Toast.makeText(this, "User's info saved successfully", Toast.LENGTH_SHORT).show();
            })
        .addOnFailureListener(
            error -> {
              Toast.makeText(this, "Failed to save user's info", Toast.LENGTH_SHORT).show();
            });
  }

  private void getUserFromFirestore(String uid) {
    db.collection("users")
        .document(uid)
        .get()
        .addOnSuccessListener(
            documentSnapshot -> {
              if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                // Access user attributes
                String username = user.getUsername();
                String email = user.getEmail();
                List<String> likedGameList = user.getLikedGameList();
                List<String> collectedGameList = user.getCollectedGameList();
                List<String> creationGameList = user.getCreationGameList();
              }
            })
        .addOnFailureListener(
            e -> {
              Toast.makeText(this, "Failed to get user's info", Toast.LENGTH_SHORT).show();
            });
  }

  // check if username or email already exist or not
  // if there is a duplicate, return -1
  // if all things are valid, return 1
  private Task<Integer> checkUsernameAndEmail(String username, String email) {
    TaskCompletionSource<Integer> taskCompletionSource = new TaskCompletionSource<>();

    db.collection("users")
        .whereEqualTo("username", username)
        .get()
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                if (!task.getResult().isEmpty()) {
                  taskCompletionSource.setResult(-1);
                } else {
                  db.collection("users")
                      .whereEqualTo("email", email)
                      .get()
                      .addOnCompleteListener(
                          task2 -> {
                            if (task2.isSuccessful()) {
                              if (!task2.getResult().isEmpty()) {
                                taskCompletionSource.setResult(-1);
                              } else {
                                taskCompletionSource.setResult(1);
                              }
                            } else {
                              Log.w(TAG, "Error checking email", task2.getException());
                              taskCompletionSource.setException(
                                  Objects.requireNonNull(task2.getException()));
                            }
                          });
                }
              } else {
                Log.w(TAG, "Error checking username", task.getException());
                taskCompletionSource.setException(Objects.requireNonNull(task.getException()));
              }
            });

    return taskCompletionSource.getTask();
  }

  private void signOutAccount() {
    mAuth.signOut();
    signOutButton.setVisibility(View.GONE);
    signInOptionButton.setVisibility(View.VISIBLE);
    createAccountOptionButton.setVisibility(View.VISIBLE);
    String tv_signInReminder = "Log in or create an account to save your picogram journey";
    tv_signInOn.setText(tv_signInReminder);
  }

  private void fetchUsername(String uid) {
    db.collection("users")
        .document(uid)
        .get()
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                  String username = document.getString("username");
                  tv_signInOn.setText("Welcome!\n" + username);
                }
              }
            });
  }

  private void saveNonogramToFireStore(Nonogram nonogram) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    db.collection("nonogram")
        .document(nonogram.getName())
        .set(nonogram)
        .addOnSuccessListener(
            aVoid -> {
              Toast.makeText(this, "Game saved successfully", Toast.LENGTH_SHORT).show();
            })
        .addOnFailureListener(
            e -> {
              Toast.makeText(this, "Failed to save this game", Toast.LENGTH_SHORT).show();
            });
  }

  private void getNonogramFromFireStore(String gameName) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    db.collection("nonogram")
        .document(gameName)
        .get()
        .addOnSuccessListener(
            documentSnapshot -> {
              if (documentSnapshot.exists()) {
                UserNonogram nonogram = documentSnapshot.toObject(UserNonogram.class);

                // Access UserNonogram attributes
                assert nonogram != null;
                String name = nonogram.getName();
                String creator = nonogram.getCreator();
                String createTime = nonogram.getCreateTime();
                // add other attribures if needed

              }
            })
        .addOnFailureListener(
            e -> {
              Toast.makeText(this, "Failed to retrieve game data", Toast.LENGTH_SHORT).show();
            });
  }

  private void backToHome() {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }

  private void helpPage() {
    Intent intent = new Intent(this, helpActivity.class);
    startActivity(intent);
  }

  private void tutorialPage() {
    Intent intent = new Intent(this, TutorialActivity.class);
    startActivity(intent);
  }
}
