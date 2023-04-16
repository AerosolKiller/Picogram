package edu.neu.picogram;

import static edu.neu.picogram.NonogramImageConverter.convertToNonogramMatrix;
import static edu.neu.picogram.NonogramUtils.convertStringToArray;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class UserNonogram extends Nonogram{

    // 如果需要也可以添加新的参数和方法
    private String creator;
    private int likedNum;
    private String gameId;
    // 时间参数
    private String createTime;

    public UserNonogram() {}
    public UserNonogram(String name,
                        String creator,
                        int likedNum,
                        String createTime,
                        int width,
                        int height,
                        int[][] rowClues,
                        int[][] colClues,
                        int[][] solution) {
        super(name, width, height, rowClues, colClues, solution);
        this.gameId = UUID.randomUUID().toString();
        this.creator = creator;
        this.likedNum = likedNum;
        this.createTime = createTime;
    }


    public void setSolution(int[][] solution) {
        this.solution = solution;
    }
    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getCreator() {
        return creator;
    }

    public int getLikedNum() {
        return likedNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setLikedNum(int likedNum) {
        this.likedNum = likedNum;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserNonogram{" +
                "creator='" + creator + '\'' +
                ", likedNum=" + likedNum + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }

    public static UserNonogram fromDocument(DocumentSnapshot document) {
        String name = document.getString("name");
        String creator = document.getString("creator");
        String createTime = document.getString("createTime");
        int likedNum = (document.getLong("likedNum")).intValue();
        int width = (document.getLong("width")).intValue();
        int height = (document.getLong("height")).intValue();
        String rowCluesString = document.getString("rowClues");
        assert rowCluesString != null;
        int[][] rowClues = convertStringToArray(rowCluesString, width, height);
        String colCluesString = document.getString("colClues");
        assert colCluesString != null;
        int[][] colClues = convertStringToArray(colCluesString, width, height);
        String solutionString = document.getString("solution");
        assert solutionString != null;
        int[][] solution = convertStringToArray(solutionString, width, height);

        return new UserNonogram(name,
                creator,
                likedNum, createTime, width, height, rowClues, colClues, solution);


    }

    public void setBitmapUri(Uri bitmapUri, Context context) throws IOException {
        Uri uri = bitmapUri;
        Bitmap bitmap = getBitmapFromUri(uri, context);

        int[][] gameMatrix = convertToNonogramMatrix(bitmap, 50);

        this.width = gameMatrix.length;
        this.height = gameMatrix[0].length;

    }

    public Bitmap getBitmapFromUri(Uri uri, Context context) throws IOException {
        InputStream input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        input.close();
        return bitmap;
    }


}
