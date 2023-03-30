package edu.neu.picogram;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {

  private FirebaseAuth mAuth;
  private FirebaseAuth.AuthStateListener mAuthStateListener;
  private Button signInOptionButton, createAccountOptionButton, signOutButton;
  private ImageButton homeButton;
  private FirebaseFirestore db;

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

    mAuth = FirebaseAuth.getInstance();
    db = FirebaseFirestore.getInstance();

    mAuthStateListener = firebaseAuth -> {
      FirebaseUser user = firebaseAuth.getCurrentUser();
      if (user != null) {
        signInOptionButton.setVisibility(View.GONE);
        createAccountOptionButton.setVisibility(View.GONE);
        signOutButton.setVisibility(View.VISIBLE);
        fetchUsername(user.getUid());
      }
    };

    signInOptionButton.setOnClickListener(v -> showSignInDialog());
    createAccountOptionButton.setOnClickListener(v -> showCreateAccountDialog());
    signOutButton.setOnClickListener(v -> signOutAccount());
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

    signInToAccount.setOnClickListener(
            v -> signIn(et_email.getText().toString(), et_password.getText().toString())
    );

    builder.setView(view);
    AlertDialog signInDialog = builder.create();
    signInDialog.show();
  }

  private void signIn(String email, String password) {
    mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {
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

    createAccountButton.setOnClickListener(
            v -> createAccount(et_email.getText().toString(),
                    et_password.getText().toString(),
                    et_userName.getText().toString())
    );

    builder.setView(view);
    AlertDialog createAccountDialog = builder.create();
    createAccountDialog.show();
  }

  private void createAccount(String email, String password, String userName) {
    mAuth
        .createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(
            this,
            task -> {
              if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                  // save username to database
                  Map<String, Object> userData = new HashMap<>();
                  userData.put("Username", userName);

                  db.collection("users").
                          document(user.getUid()).
                          set(userData).
                          addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Username saved successfully", Toast.LENGTH_SHORT).show();
                          }).
                          addOnFailureListener(error -> {
                            Toast.makeText(this, "Failed to save username", Toast.LENGTH_SHORT).show();
                          });
                }
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();

              } else {
                Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show();
              }
            });
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
    db.collection("users").document(uid)
            .get()
            .addOnCompleteListener(task -> {
              if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                  String username = document.getString("Username");
                  tv_signInOn.setText("Welcome!\n" + username);
                }
              }
            });
  }


}
