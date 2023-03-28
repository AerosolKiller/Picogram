package edu.neu.picogram;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void onClick(android.view.View view) {
    if (view.getId() == R.id.ibt_settingButton) {
      Intent intent = new Intent(this, SettingActivity.class);
      startActivity(intent);
    }
    if (view.getId() == R.id.bt_tutorial) {
      Intent intent = new Intent(this, TutorialActivity.class);
      startActivity(intent);
    }
    if (view.getId() == R.id.bt_gameplay) {
      Intent intent = new Intent(this, GamePlayActivity.class);
      startActivity(intent);
    }
    if (view.getId() == R.id.bt_community) {
      Intent intent = new Intent(this, CommunityActivity.class);
      startActivity(intent);
    }
    if (view.getId() == R.id.bt_photo) {
      Intent intent = new Intent(this, TakePhotoActivity.class);
      startActivity(intent);
    }
    if (view.getId() == R.id.bt_create) {
      Intent intent = new Intent(this, EditActivity.class);
      startActivity(intent);
    }
  }
}