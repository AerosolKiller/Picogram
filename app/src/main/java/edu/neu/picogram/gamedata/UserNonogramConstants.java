package edu.neu.picogram.gamedata;

import static edu.neu.picogram.NonogramUtils.saveGameHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import edu.neu.picogram.Nonogram;
import edu.neu.picogram.NonogramUtils;
import edu.neu.picogram.SerializableNonogram;
import edu.neu.picogram.UserNonogram;

public class UserNonogramConstants {

    public static ArrayList<UserNonogram> getUserGames() {
    ArrayList<UserNonogram> games = new ArrayList<>();
        UserNonogram game1 =
                new UserNonogram("Puzzle 1","Tom", 5, "2021-11-11",
                        5,
                        5,
                        new int[][] {{0}, {5}, {1, 1}, {1, 1}, {1, 1}},
                        new int[][] {{1}, {4}, {1}, {3}, {1, 1}},
                        new int[][] {
                                {0, 0, 0, 0, 0},
                                {1, 1, 1, 1, 1},
                                {0, 1, 0, 1, 0},
                                {0, 1, 0, 1, 0},
                                {0, 1, 0, 0, 1}
                        });
        saveGameHelper(game1);
        games.add(game1);

        UserNonogram game2 = new UserNonogram("Puzzle 2","Mike", 6, "2022-11-11",
                5,
                5,
                new int[][] {{1}, {3}, {3}, {5}, {1}},
                new int[][] {{1}, {3}, {5}, {3}, {1}},
                new int[][] {
                        {0, 0, 1, 0, 0},
                        {0, 1, 1, 1, 0},
                        {0, 1, 1, 1, 0},
                        {1, 1, 1, 1, 1},
                        {0, 0, 1, 0, 0}
                });
        saveGameHelper(game2);
        games.add(game2);

        UserNonogram game3 = new UserNonogram("Puzzle 3","Tom", 9, "2023-3-3",
                5,
                5,
                new int[][] {{3}, {2, 2}, {1, 1, 1}, {2, 2}, {3}},
                new int[][] {{3}, {2, 2}, {1, 1, 1}, {2, 2}, {3}},
                new int[][] {
                        {0, 1, 1, 1, 0},
                        {1, 1, 0, 1, 1},
                        {1, 0, 1, 0, 1},
                        {1, 1, 0, 1, 1},
                        {0, 1, 1, 1, 0}
                });
        saveGameHelper(game3);
        games.add(game3);
        return games;
    }

    public static Nonogram getUserGame(int index) {
        return getUserGames().get(index);
    }
}
