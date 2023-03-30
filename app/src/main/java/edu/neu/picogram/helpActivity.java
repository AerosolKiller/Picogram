package edu.neu.picogram;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class helpActivity extends AppCompatActivity {

  private ImageButton backButton;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_help);

    backButton = findViewById(R.id.backSettingButton);
    backButton.setOnClickListener(v -> backHome());

  }

  private void backHome() {
    Intent intent = new Intent(this, SettingActivity.class);
    startActivity(intent);
  }
}
