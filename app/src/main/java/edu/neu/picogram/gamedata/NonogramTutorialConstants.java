package edu.neu.picogram.gamedata;

import edu.neu.picogram.NonogramTutorial;
import java.util.ArrayList;

public class NonogramTutorialConstants {

  public static ArrayList<NonogramTutorial> getTutorials() {
    ArrayList<NonogramTutorial> tutorials = new ArrayList<>();
    NonogramTutorial nonogramTutorial2 =
        new NonogramTutorial(
            "Nonogram Tutorial 2",
            "Playing the game is easy! Just click on a square to fill it in. \n Fill in all the squares in this line",
            true,
            3,
            1,
            null,
            null,
            new int[][] {{1, 1, 1}});

    tutorials.add(nonogramTutorial2);

    NonogramTutorial nonogramTutorial3 =
        new NonogramTutorial(
            "Nonogram Tutorial 3",
            "To empty a square just click on it again. \n Clean them all!",
            true,
            3,
            1,
            null,
            null,
            new int[][] {{0, 0, 0}},
            new int[][] {{1, 1, 1}});

    tutorials.add(nonogramTutorial3);

    NonogramTutorial nonogramTutorial4 =
        new NonogramTutorial(
            "Nonogram Tutorial 4",
            "The numbers at the end of the line indicate the number of squares to be filled in this line. \n"
                + "Fill in the number of squares indicated by the numbers on the left fo the line",
            true,
            5,
            1,
            new int[][] {{5}},
            null,
            new int[][] {{1, 1, 1, 1, 1}});

    tutorials.add(nonogramTutorial4);

    return tutorials;
  }
}
