package edu.neu.picogram;

import android.Manifest;
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

public class MainActivity extends AppCompatActivity {

  private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
  private static final int CAMERA_REQUEST_CODE = 1002;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
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