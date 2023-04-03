package edu.neu.picogram.gamedata;

import java.util.ArrayList;

import edu.neu.picogram.Nonogram;
import edu.neu.picogram.UserNonogram;

public class UserNonogramConstants {

    public static ArrayList<Nonogram> getUserGames() {
        ArrayList<Nonogram> games = new ArrayList<>();

        Nonogram game1 =
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
        games.add(game1);

        Nonogram game2 = new UserNonogram("Puzzle 2","Mike", 6, "2022-11-11",
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
        games.add(game2);

        Nonogram game3 = new UserNonogram("Puzzle 3","Tom", 9, "2023-3-3",
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
        games.add(game3);

        return games;
    }
}
