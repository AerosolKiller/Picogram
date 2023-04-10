package edu.neu.picogram;

import java.util.List;


public class User {
    private String username;
    private String email;
    private List<String> playedSmallGameList;
    private List<String> playedLargeGameList;
    private List<String> collectedGameList;
    private List<String> likedGameList;
    private List<String> creationGameList;

    public User() {}

    public User(
      String username,
      String email,
      List<String> playedSmallGameList,
      List<String> playedLargeGameList,
      List<String> collectedGameList,
      List<String> likedGameList,
      List<String> creationGameList) {
        this.username = username;
        this.email = email;
        this.playedSmallGameList = playedSmallGameList;
        this.playedLargeGameList = playedLargeGameList;
        this.collectedGameList = collectedGameList;
        this.likedGameList = likedGameList;
        this.creationGameList = creationGameList;
    }

    public List<String> getPlayedSmallGameList() {
        return playedSmallGameList;
    }

    public void setPlayedSmallGameList(List<String> playedSmallGameList) {
        this.playedSmallGameList = playedSmallGameList;
    }

    public List<String> getPlayedLargeGameList() {
        return playedLargeGameList;
    }

    public void setPlayedLargeGameList(List<String> playedLargeGameList) {
        this.playedLargeGameList = playedLargeGameList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
      }
      public void setEmail(String email) {
        this.email = email;
      }

      public List<String> getCollectedGameList() {
        return collectedGameList;
      }

      public void setCollectedGameList(List<String> collectedGameList) {
        this.collectedGameList = collectedGameList;
      }

      public List<String> getLikedGameList() {
        return likedGameList;
      }

      public void setLikedGameList(List<String> likedGameList) {
        this.likedGameList = likedGameList;
      }

      public List<String> getCreationGameList() {
        return creationGameList;
      }

      public void setCreationGameList(List<String> creationGameList) {
        this.creationGameList = creationGameList;
      }
}
