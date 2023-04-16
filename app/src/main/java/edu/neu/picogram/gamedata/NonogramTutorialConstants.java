package edu.neu.picogram.gamedata;

import edu.neu.picogram.NonogramTutorial;
import edu.neu.picogram.R;
import java.util.ArrayList;

public class NonogramTutorialConstants {

  public static ArrayList<NonogramTutorial> getTutorials() {
    ArrayList<NonogramTutorial> tutorials = new ArrayList<>();
    NonogramTutorial nonogramTutorial1 =
        new NonogramTutorial(
            "Nonogram Tutorial 1",
            "Picogram is a puzzle game which involves revealing a picture by filling in the square on a grid.\n"
                + "The numbers on top and on the left hand side of the grid help finding which squares should be filled in.",
            false,
            R.drawable.tutorial_img1);

    tutorials.add(nonogramTutorial1);

    NonogramTutorial nonogramTutorial2 =
        new NonogramTutorial(
            "Nonogram Tutorial 2",
            "Playing the game is easy! Just click on a square to fill it in.\nFill in all the squares in this line",
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

    NonogramTutorial nonogramTutorial5 =
        new NonogramTutorial(
            "Nonogram Tutorial 5",
            "In this new example fill in two adjacent squares and then,"
                + " before filling in two others leave at least one square empty between the two groups of squares.\n"
                + "Fill in the right squares",
            true,
            5,
            1,
            new int[][] {{2, 2}},
            null,
            new int[][] {{1, 1, 0, 1, 1}});

    tutorials.add(nonogramTutorial5);

    NonogramTutorial nonogramTutorial6 =
        new NonogramTutorial(
            "Nonogram Tutorial 6",
            "When the right squares have been filled in on a line it is wise to make the empty squares black. to finish the puzzle\n"
                + "Mark the remaining square with a cross.\n",
            true,
            5,
            1,
            new int[][] {{2, 2}},
            null,
            new int[][] {{1, 1, 2, 1, 1}},
            new int[][] {{1, 1, 0, 1, 1}});

    tutorials.add(nonogramTutorial6);

    NonogramTutorial nonogramTutorial7 =
        new NonogramTutorial(
            "Nonogram Tutorial 7",
            "Fill in this line.\n",
            true,
            5,
            1,
            new int[][] {{3, 1}},
            null,
            new int[][] {{1, 1, 1, 2, 1}});

    tutorials.add(nonogramTutorial7);

    NonogramTutorial nonogramTutorial8 =
        new NonogramTutorial(
            "Nonogram Tutorial 8",
            "Fill in this line.\n",
            true,
            5,
            1,
            new int[][] {{0}},
            null,
            new int[][] {{2, 2, 2, 2, 2}});

    tutorials.add(nonogramTutorial8);

    NonogramTutorial nonogramTutorial9 =
        new NonogramTutorial(
            "Nonogram Tutorial 9",
            "Sometimes a set of squares can be put in different palces on the line."
                + "In this case you should count the squares to be filled in from one end and then form the other and only fill in the squares which are common to both groups\n",
            false,
            R.drawable.tutorial_img2);

    tutorials.add(nonogramTutorial9);

    NonogramTutorial nonogramTutorial10 =
        new NonogramTutorial(
            "Nonogram Tutorial 10",
            "Using this method which squares can be filled in?\n",
            true,
            5,
            1,
            new int[][] {{4}},
            null,
            new int[][] {{0, 1, 1, 1, 0}});

    tutorials.add(nonogramTutorial10);

    NonogramTutorial nonogramTutorial11 =
        new NonogramTutorial(
            "Nonogram Tutorial 11",
            "WHich squares can be filled in?\n",
            true,
            5,
            1,
            new int[][] {{3}},
            null,
            new int[][] {{0, 0, 1, 0, 0}});
    tutorials.add(nonogramTutorial11);

    NonogramTutorial nonogramTutorial12 =
        new NonogramTutorial(
            "Nonogram Tutorial 12",
            "WHich squares can be filled in?\n",
            true,
            5,
            1,
            new int[][] {{1, 2}},
            null,
            new int[][] {{0, 0, 0, 1, 0}});

    tutorials.add(nonogramTutorial12);

    return tutorials;
  }
}
