package edu.neu.picogram;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SettingActivity extends AppCompatActivity {

  private Button signButton;
  Dialog signDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);

//    signButton.setOnClickListener(new View.OnClickListener() {
//      @Override public void onClick(View view) {
//        showDialog();
//      }
//    });
  }

  private void showDialog() {
    AlertDialog.Builder signAlert;

  }
}
