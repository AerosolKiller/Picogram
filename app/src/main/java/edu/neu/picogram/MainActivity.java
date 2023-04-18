package edu.neu.picogram;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

  private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
  private static final int CAMERA_REQUEST_CODE = 1002;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // 找到主页的底部导航栏，设置监听器，点击不同的按钮跳转到不同的页面
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
    bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
      switch (item.getItemId()) {
        case R.id.nav_play:
          Intent intent = new Intent(MainActivity.this, MainActivity.class);
          startActivity(intent);
          break;
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