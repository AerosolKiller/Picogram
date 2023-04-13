package edu.neu.picogram.gamedata;

import static edu.neu.picogram.NonogramUtils.saveGameHelper;
import android.util.Log;

import androidx.annotation.NonNull;

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
                new UserNonogram("Puzzle 1","Tom", 7, "2013-11-11",
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

        UserNonogram game3 = new UserNonogram("Puzzle 3","Tim", 5, "2021-3-3",
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

        UserNonogram game4 = new UserNonogram("Puzzle 4","Jay", 10, "2020-3-3",
                5,
                5,
                new int[][] {{3}, {2, 2}, {1, 1, 1}, {2, 2}, {3}},
                new int[][] {{3}, {2, 2}, {1, 1, 1}, {2, 2}, {3}},
                new int[][] {
                        {0, 0, 1, 1, 0},
                        {0, 0, 1, 1, 1},
                        {1, 0, 1, 0, 0},
                        {1, 1, 1, 0, 0},
                        {1, 1, 1, 0, 0}
                });
        saveGameHelper(game4);
        games.add(game4);

        return games;
    }

    public static Nonogram getUserGame(int index) {
        return getUserGames().get(index);
    }

    @NonNull
    @Override
    public String toString() {
        // try to print out the nonogram list by calling the toString() method
        // of each nonogram in the list
        StringBuilder sb = new StringBuilder();
        for (UserNonogram game : getUserGames()) {
            sb.append(game.toString());
        }

        return sb.toString();
    }
}
