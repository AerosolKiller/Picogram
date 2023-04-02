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

//      final int REQUEST_IMAGE_CAPTURE = 1;
//
//      Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//      try {
//        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//      } catch (ActivityNotFoundException e) {
//        // display error state to the user
//      }
//
//      startActivity(takePictureIntent);

      // 检查相机权限，如果没有授权，则请求相机权限
//      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//          requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
//        } else {
//          // 如果已经授权，则打开相机拍照
//          Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//          startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
//        }
//      }
//      if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
//              != PackageManager.PERMISSION_GRANTED
//              && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
//              != PackageManager.PERMISSION_GRANTED) {
//        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//        return;
//      }



    }
    if (view.getId() == R.id.bt_create) {
      Intent intent = new Intent(this, EditActivity.class);
      startActivity(intent);
    }
  }
}