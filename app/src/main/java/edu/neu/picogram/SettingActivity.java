package edu.neu.picogram;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingActivity extends AppCompatActivity {

  private FirebaseAuth mAuth;
  private FirebaseAuth.AuthStateListener mAuthStateListener;
  private Button signInOptionButton, createAccountOptionButton;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);

    // create buttons for create account and sign in
    signInOptionButton = findViewById(R.id.bt_signin);
    createAccountOptionButton = findViewById(R.id.bt_createAccount);

    mAuth = FirebaseAuth.getInstance();

    mAuthStateListener = firebaseAuth -> {
      FirebaseUser user = firebaseAuth.getCurrentUser();

      if (user != null) {
        finish();
        startActivity(new Intent(this, GamePlayActivity.class));
      }
    };

    signInOptionButton.setOnClickListener(v -> showSignInDialog());
    createAccountOptionButton.setOnClickListener(v -> showCreateAccountDialog());
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
            v -> createAccount(et_email.getText().toString(), et_password.getText().toString())
    );

    builder.setView(view);
    AlertDialog createAccountDialog = builder.create();
    createAccountDialog.show();
  }

  private void createAccount(String email, String password) {
    mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {
              if (task.isSuccessful()) {
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();
              } else {
                Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show();
              }
            });
  }

}
