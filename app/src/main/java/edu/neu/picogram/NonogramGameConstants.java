package edu.neu.picogram;

import java.util.ArrayList;

public class NonogramGameConstants {
  public static ArrayList<Nonogram> getGames() {
    ArrayList<Nonogram> games = new ArrayList<>();
    Nonogram game1 =
        new Nonogram(
            "Puzzle 1",
            5,
            5,
            new int[][] {{1}, {4}, {1}, {3}, {1, 1}},
            new int[][] {{0}, {5}, {1, 1}, {1, 1}, {1, 1}},
            new int[][] {
              {0, 0, 0, 0, 0},
              {1, 1, 1, 1, 1},
              {0, 1, 0, 1, 0},
              {0, 1, 0, 1, 0},
              {0, 1, 0, 0, 1}
            });
    games.add(game1);

    Nonogram game2 =
        new Nonogram(
            "Puzzle 2",
            5,
            5,
            new int[][] {{1}, {3}, {5}, {3}, {1}},
            new int[][] {{1}, {3}, {3}, {5}, {1}},
            new int[][] {
              {0, 0, 1, 0, 0},
              {0, 1, 1, 1, 0},
              {0, 1, 1, 1, 0},
              {1, 1, 1, 1, 1},
              {0, 0, 1, 0, 0}
            });
    games.add(game2);

    Nonogram game3 =
        new Nonogram(
            "Puzzle 3",
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

    Nonogram game4 =
        new Nonogram(
            "Puzzle 4",
            10,
            10,
            new int[][] {{0}, {0}, {2}, {4}, {4}, {9}, {2}, {2}, {0}, {0}},
            new int[][] {{1}, {2}, {3}, {1, 1}, {1}, {1}, {3}, {4}, {4}, {2}},
            new int[][] {
              {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 1, 1, 0, 0, 0},
              {0, 0, 0, 0, 0, 1, 1, 1, 0, 0},
              {0, 0, 0, 0, 0, 1, 0, 1, 0, 0},
              {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
              {0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
              {0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
              {0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
              {0, 0, 0, 1, 1, 0, 0, 0, 0, 0}
            });

    games.add(game4);

    Nonogram game5 =
        new Nonogram(
            "Puzzle 5",
            10,
            10,
            new int[][] {{0}, {0}, {1}, {2}, {1, 1, 2}, {1, 2, 2}, {4}, {2}, {0}, {0}},
            new int[][] {{0}, {4}, {2, 2}, {2}, {2}, {2}, {0}, {2}, {2}, {0}},
            new int[][] {
              {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
              {0, 0, 0, 1, 1, 1, 1, 0, 0, 0},
              {0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
              {0, 0, 0, 0, 0, 0, 1, 1, 0, 0},
              {0, 0, 0, 0, 0, 1, 1, 0, 0, 0},
              {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
              {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
              {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            });
    games.add(game5);

    return games;
  }

  public static Nonogram getGame(int index) {
    return getGames().get(index);
  }
}
