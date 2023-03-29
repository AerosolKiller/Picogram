package edu.neu.picogram;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingActivity extends AppCompatActivity {

  private FirebaseAuth mAuth;
  private FirebaseAuth.AuthStateListener mAuthStateListener;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);

    // create buttons for create account and sign in
    Button signInButton = findViewById(R.id.bt_signin);
    Button createAccountButton = findViewById(R.id.bt_createAccount);

    signInButton.setOnClickListener(v -> showSignInDialog());
    createAccountButton.setOnClickListener(v -> showCreateAccountDialog());

    mAuth = FirebaseAuth.getInstance();

    mAuthStateListener = firebaseAuth -> {
      FirebaseUser user = firebaseAuth.getCurrentUser();
    }
  }

  private void showSignInDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    View view = inflater.inflate(R.layout.dialog_signin, null);
    builder.setView(view);

    AlertDialog signInDialog = builder.create();
    signInDialog.show();
  }

  private void showCreateAccountDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    View view = inflater.inflate(R.layout.dialog_createaccount, null);
    builder.setView(view);

    AlertDialog createAccountDialog = builder.create();
    createAccountDialog.show();
  }
}
