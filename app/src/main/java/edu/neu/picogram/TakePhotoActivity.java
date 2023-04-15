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
import android.content.ActivityNotFoundException;
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
    private static final int CROP_REQUEST_CODE = 3;
    private static final int PERMISSIONS_CODE = 1;

    private static final int REQUEST_CODE = 1;

    private Button takePhotoButton;

    private Button getPhotoButton;

    private Button transformPhotoButton;

    private ActivityResultLauncher<Intent> resultLauncher;
    ImageView imageView;

    String currentPhotoPath;

    private Bitmap bitmap;

    private String photoPath;
    Uri photoUri;

    Uri contentUri;


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

                if (ContextCompat.checkSelfPermission(TakePhotoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(TakePhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(TakePhotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TakePhotoActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                    //申请读取文件和照片的权限
                    ActivityCompat.requestPermissions(TakePhotoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                    ActivityCompat.requestPermissions(TakePhotoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    // 打开拍照界面
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    // 尝试保存拍的照片
                    if (intent.resolveActivity(getPackageManager()) != null) {
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
//                if(bitmap != null) transfromPhotoToGameArray(bitmap);
                if (photoUri != null) {

                    cropPhoto(photoUri);

//                    Intent intent = new Intent("com.android.camera.action.CROP");
//                    intent.setDataAndType(photoUri, "image/*");
//                    intent.putExtra("crop", "true");
//                    intent.putExtra("aspectX", 1);
//                    intent.putExtra("aspectY", 1);
//                    intent.putExtra("outputX", 200);
//                    intent.putExtra("outputY", 200);
//                    intent.putExtra("scale", true);
//                    intent.putExtra("return-data", true);
//                    startActivityForResult(intent, 3);
                } else
                    Toast.makeText(TakePhotoActivity.this, "Please take a photo first!", Toast.LENGTH_SHORT).show();
            }
        });

        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (intent != null) {
                    contentUri = intent.getData();
                    imageView.setImageURI(contentUri);
                }


            }
        });


    }

    // 根据Uri的文件来裁剪照片
//    private void cropPhoto(Uri uri) {
//        try{
//            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//            // indicate image type and Uri
//            cropIntent.setDataAndType(uri, "image/*");
//            // set crop properties
//            cropIntent.putExtra("crop", "true");
//            // indicate aspect of desired crop
//            cropIntent.putExtra("aspectX", 1);
//            cropIntent.putExtra("aspectY", 1);
//            // indicate output X and Y
//            cropIntent.putExtra("outputX", 128);
//            cropIntent.putExtra("outputY", 128);
//            // retrieve data on return
//            cropIntent.putExtra("return-data", true);
//            // start the activity - we handle returning in onActivityResult
//            startActivityForResult(cropIntent, 3);
//
//        }
//        catch(ActivityNotFoundException anfe){
//            // display an error message
//            String errorMessage = "Whoops - your device doesn't support the crop action!";
//            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }

    private void transfromPhotoToGameArray(Bitmap bitmap) {

        boolean[][] gameArray = new NonogramImageConverter().convertToNonogramMatrix(bitmap, 10);

        String[] gameArrayString = new String[10];
        //打印gameArray
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (gameArray[i][j]) gameArrayString[i] += "1";
                else gameArrayString[i] += "0";
            }
        }

        // 打印gameArrayString
        for (int i = 0; i < 10; i++) {
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
            Log.d("photoUri", photoUri.toString());

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

        if (requestCode == CROP_REQUEST_CODE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            if(extras != null) bitmap = extras.getParcelable("data");
//            Uri uri = data.getData();
//            photoUri = uri;
//            imageView.setImageURI(photoUri);


            if(contentUri != null) imageView.setImageURI(contentUri);

        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                .format(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, timeStamp);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
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


    private void cropPhoto(Uri uri) {
        //在7.0以上系统裁剪完毕之后，会提示“无法保存经过裁剪的图片”
        //这是因为，我们在7.0以上跨文件传输uri时候，需要用FileProvider,但是这里需要用
        //Uri.fromFile(file)生成的，而不是使用FileProvider.getUriForFile
        //intent.putExtra("set-as-wallpaper",true); 默认是false,当你弄成true的时候，你就会发现打开不是裁剪的，而是设置为壁纸的操作。
        // intent.putExtra("return-data", true);下面就可以获取到该bitmap
        // if (data != null && data.getParcelableExtra("data") != null) {
        //                mStream = new ByteArrayOutputStream();
        //                mBitmap = data.getParcelableExtra("data");
        //                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, mStream);
        //                /**图片可以应用了*/
        //                /**接下来就是上传到服务器*/
        //                File files = creatFile(mBitmap);//变成文件
        //                ...后续根据需要来...
        //}
        contentUri = Uri.fromFile(new File(getPhotoPath()));
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //Android 7.0需要临时添加读取Url的权限， 添加此属性是为了解决：调用裁剪框时候提示：图片无法加载或者加载图片失败或者无法加载此图片
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//发送裁剪信号，去掉也能进行裁剪
        intent.putExtra("scale", true);// 设置缩放
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //上述两个属性控制裁剪框的缩放比例。
        //当用户用手拉伸裁剪框时候，裁剪框会按照上述比例缩放。
        intent.putExtra("outputX", 300);//属性控制裁剪完毕，保存的图片的大小格式。
        intent.putExtra("outputY", 300);//你按照1:1的比例来裁剪的，如果最后成像是800*400，那么按照2:1的样式保存，
        intent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());//输出裁剪文件的格式
        intent.putExtra("return-data", true);//是否返回裁剪后图片的Bitmap
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);//设置输出路径
        startActivityForResult(intent, CROP_REQUEST_CODE);
        resultLauncher.launch(intent);
    }

    private String getPhotoPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/crop_photo.jpg";
        return path;
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


