package edu.neu.picogram;

import java.util.UUID;

public class UserNonogram extends Nonogram{

    // 如果需要也可以添加新的参数和方法
    private String creator;
    private int likedNum;
    private String gameId;
    // 时间参数
    private String createTime;

    public UserNonogram(String name, String creator, int likedNum, String createTime, int width, int height, int[][] rowClues, int[][] colClues, int[][] solution) {
        super(name, width, height, rowClues, colClues, solution);
        this.gameId = UUID.randomUUID().toString();
        this.creator = creator;
        this.likedNum = likedNum;
        this.createTime = createTime;
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


}
