package edu.neu.picogram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

  private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
  private static final int CAMERA_REQUEST_CODE = 1002;
  private SharedPreferences sharedPreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    sharedPreferences = getSharedPreferences("tutorial", MODE_PRIVATE);

    // 找到主页的底部导航栏，设置监听器，点击不同的按钮跳转到不同的页面
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
    bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//      if( this.getClass().equals(MainActivity.class)) {
//        return true;
//      }
      switch (item.getItemId()) {
        case R.id.nav_create:
          Intent intent1 = new Intent(MainActivity.this, EditActivity.class);
          startActivity(intent1);
          break;
        case R.id.nav_community:
          Intent intent2 = new Intent(MainActivity.this, CommunityActivity.class);
          startActivity(intent2);
          break;

      }
      return true;
    });



  }

  public void onClick(View view) {
    if (view.getId() == R.id.ibt_settingButton) {
      Intent intent = new Intent(this, SettingActivity.class);
      startActivity(intent);
    }
    if (view.getId() == R.id.bt_tutorial) {
      Intent intent = new Intent(this, TutorialActivity.class);
      sharedPreferences.edit().putBoolean("hasSeenTutorial", true).apply();
      startActivity(intent);
    }
    if (view.getId() == R.id.bt_gameplay) {
      if (!sharedPreferences.getBoolean("hasSeenTutorial", false)) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tutorial");
        builder.setMessage("You haven't seen the tutorial yet. Do you want to see it now?");
        builder.setPositiveButton(
            "Yes",
            (dialog, which) -> {
              Intent intent = new Intent(this, TutorialActivity.class);
              sharedPreferences.edit().putBoolean("hasSeenTutorial", true).apply();
              startActivity(intent);
            });
        builder.setNegativeButton(
            "No",
            (dialog, which) -> {
              Intent intent = new Intent(this, GamePlayActivity.class);
              startActivity(intent);
            });
        builder.show();
      } else {
        Intent intent = new Intent(this, GamePlayActivity.class);
        startActivity(intent);
      }
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
