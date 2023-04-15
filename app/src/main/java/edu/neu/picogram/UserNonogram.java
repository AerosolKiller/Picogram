package edu.neu.picogram;

import static edu.neu.picogram.NonogramUtils.convertStringToArray;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;

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

    public UserNonogram(){}

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
                ", likedNum=" + likedNum +
                ", gameId='" + gameId + '\'' +
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
        int[][] rowClues = convertStringToArray(rowCluesString, width, height);
        String colCluesString = document.getString("colClues");
        int[][] colClues = convertStringToArray(colCluesString, width, height);
        String solutionString = document.getString("solution");
        int[][] solution = convertStringToArray(solutionString, width, height);

        UserNonogram game = new UserNonogram(name,
                creator,
                likedNum, createTime, width, height, rowClues, colClues, solution);

        return game;
    }
}
