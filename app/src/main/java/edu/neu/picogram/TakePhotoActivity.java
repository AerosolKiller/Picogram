package edu.neu.picogram;

import android.Manifest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class TakePhotoActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1002;
    private static final int CAMERA_REQUEST_CODE = 1002;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PERMISSIONS_CODE = 1;

    private static final int REQUEST_CODE = 1;

    private Button takePhotoButton;

    private Button getPhotoButton;

    private Button transformPhotoButton;
    ImageView imageView;

    String currentPhotoPath;

    private Bitmap bitmap;

    private String photoPath;
    Uri photoUri;


    // 拍照，并且保存到本地相册，显示在image View中。但是每次拍照，不会更新image View。
    // 从相册中获取图片，并且显示在image View中。每次选择，可以更新image View。
    // TODO: 实现每次拍照，都可以更新image View。

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        takePhotoButton = findViewById(R.id.btn_takephoto);
        getPhotoButton = findViewById(R.id.btn_getPhoto);
        transformPhotoButton = findViewById(R.id.btn_transformPhoto);

        imageView = findViewById(R.id.imageView1);
        bitmap = null;

        // 从相册中获取图片,并显示在ImageView中
        getPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 检查是否有相机权限和读取相册权限，如果没有则申请相机权限
                if (ContextCompat.checkSelfPermission(TakePhotoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(TakePhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(TakePhotoActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                    //申请读取文件和照片的权限
                    ActivityCompat.requestPermissions(TakePhotoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                    ActivityCompat.requestPermissions(TakePhotoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    // 有权限，直接读取系统相册
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
            }
        });

        // 打开相机拍照,并显示在ImageView中,并保存到本地
        // TODO: 每次拍照，更新image View，更新有问题。
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(TakePhotoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(TakePhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TakePhotoActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                    //申请读取文件和照片的权限
                    ActivityCompat.requestPermissions(TakePhotoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
//                    ActivityCompat.requestPermissions(TakePhotoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    // 打开拍照界面
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    // 尝试保存拍的照片
                    if(intent.resolveActivity(getPackageManager()) != null) {
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

//                        photoFile = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), "photo.jpg");
                        photoUri = FileProvider.getUriForFile(TakePhotoActivity.this, "edu.neu.picogram.fileProvider", photoFile);

                        Log.d("photoUri", photoUri.toString());

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, CAMERA_REQUEST_CODE);
                        imageView.setImageURI(photoUri);

                    }
                }

            }
        });

        transformPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(TakePhotoActivity.this, TransformPhotoActivity.class);
                if(bitmap != null) transfromPhotoToGameArray(bitmap);
                else Toast.makeText(TakePhotoActivity.this, "Please take a photo first!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void transfromPhotoToGameArray(Bitmap bitmap) {

        boolean[][] gameArray = new NonogramImageConverter().convertToNonogramMatrix(bitmap, 10);

        String[] gameArrayString = new String[10];
         //打印gameArray
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                if(gameArray[i][j]) gameArrayString[i] += "1";
                else gameArrayString[i] += "0";
            }
        }

        // 打印gameArrayString
        for(int i = 0; i < 10; i++) {
            Log.d("gameArrayString", gameArrayString[i]);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 从相册中获取图片,并显示在ImageView中
        if (resultCode == RESULT_OK && requestCode == 2) {
            Uri uri = data.getData();
            photoUri = uri;
            imageView.setImageURI(photoUri);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // 打开相机拍照,并显示在ImageView中,并保存到本地
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            imageView.setImageURI(photoUri);

            Bitmap imageBitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                saveToAlbum(imageBitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                .format(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, timeStamp);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Picogram");
        }


        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void saveToAlbum(Bitmap bitmap) throws IOException {

        // Create file path to save the image
        // val filename = "my_image.jpg"
        // Generate time-ordered filename
        String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis()) + ".jpg";

        String path =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .getAbsolutePath() + File.separator + filename;

        // Save the image
        File file = new File(path);
        FileOutputStream outputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

        outputStream.flush();
        outputStream.close();

        // Refresh the gallery
//         MediaScannerConnection.scanFile(
//            context,
//            arrayOf<String>(file.toString()),
//            arrayOf<String>("image/jpeg"),
//            null
//         )
    }

}


