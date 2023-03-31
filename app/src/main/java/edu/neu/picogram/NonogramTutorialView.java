package edu.neu.picogram;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class NonogramTutorialView extends View {
  private NonogramTutorial game;
  private Paint gridPaint;
  private Paint cluePaint;
  private Paint cellPaint;
  private Paint solutionPaint;
  private int prevCol = -1;
  private int prevRow = -1;
  private boolean mode = true;
  private boolean showSolution = false;
  private float ratio = 0.75f;
  int cellSize;
  int offsetX;
  int offsetY;

  /**
   * 继承View类，自定义游戏界面，view可以接收nonogram对象，根据nonogram对象的信息绘制游戏界面.
   * 但是nonogram对象不方便在构造器内接收，通过setGame方法接收nonogram对象
   *
   * @param context
   * @param attrs
   */
  public NonogramTutorialView(Context context, AttributeSet attrs) {
    super(context, attrs);
    // 因为是画出来游戏界面，自定义画笔去画格子，提示，hint，以及游戏界面
    gridPaint = new Paint();
    gridPaint.setColor(Color.BLACK);
    gridPaint.setStrokeWidth(2f);

    cluePaint = new Paint();
    cluePaint.setColor(Color.BLACK);
    cluePaint.setTextSize(40f);
    cluePaint.setTextAlign(Paint.Align.RIGHT);

    cellPaint = new Paint();
    cellPaint.setColor(Color.DKGRAY);

    solutionPaint = new Paint();
    solutionPaint.setColor(Color.BLUE);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    // 通过当前view的宽高，以及是几乘几的nonogram对象，计算出每个格子的大小
    // 通过ratio来控制格子的大小，让格子不至于太大或者太小
    cellSize =
        (int) (Math.min(getWidth() / game.getWidth(), getHeight() / game.getHeight()) * ratio);
    // 因为要让游戏界面居中，默认是在左上角绘制，所以需要一些偏移量
    offsetX = (int) ((getWidth() - game.getWidth() * cellSize) / 2);
    offsetY = (int) ((getHeight() - game.getHeight() * cellSize) / 2) + cellSize / 2;

    // 画格子，画横竖线
    for (int i = 0; i <= game.getWidth(); i++) {
      canvas.drawLine(
          i * cellSize + offsetX,
          offsetY,
          i * cellSize + offsetX,
          game.getHeight() * cellSize + offsetY,
          gridPaint);
    }
    for (int i = 0; i <= game.getHeight(); i++) {
      canvas.drawLine(
          offsetX,
          i * cellSize + offsetY,
          game.getWidth() * cellSize + offsetX,
          i * cellSize + offsetY,
          gridPaint);
    }
    // 画提升数字，同样两个for循环，分别画横向和纵向的提升数字
    for (int row = 0; row < game.getHeight(); row++) {
      if ((game.getRowClues() == null)) {
        break;
      } else if (game.getRowClues(row).length > 0) {
        int[] rowClues = reverse(game.getRowClues(row));
        for (int i = 0; i < rowClues.length; i++) {
          String clue = String.valueOf(rowClues[i]);
          float xPosition = offsetX - cluePaint.getTextSize() * (i + 1);
          float yPosition = (row + 0.5f) * cellSize + offsetY;
          canvas.drawText(clue, xPosition, yPosition, cluePaint);
        }
      }
    }
    for (int col = 0; col < game.getWidth(); col++) {
      if ((game.getRowClues() == null)) {
        break;
      } else if (game.getColClues(col).length > 0) {
        int[] colClues = reverse(game.getColClues(col));
        for (int i = 0; i < colClues.length; i++) {
          String clue = String.valueOf(colClues[i]);
          float xPosition = (col + 0.5f) * cellSize + offsetX;
          float yPosition = offsetY - cluePaint.getTextSize() * (i + 1);
          canvas.drawText(clue, xPosition, yPosition, cluePaint);
        }
      }
    }

    // 根据每个格子的状态，绘制不同的颜色
    for (int row = 0; row < game.getHeight(); row++) {
      for (int col = 0; col < game.getWidth(); col++) {
        // 如果当前格子被选中，绘制一个灰色的格子
        if (game.getCurrentGrid(row, col) == 1) {
          canvas.drawRect(
              col * cellSize + offsetX,
              row * cellSize + offsetY,
              (col + 1) * cellSize + offsetX,
              (row + 1) * cellSize + offsetY,
              cellPaint);
        }
        // 如果当前格子被标记为错误，绘制一个叉
        else if (game.getCurrentGrid(row, col) == 2) {
          // Draw a cross
          canvas.drawLine(
              col * cellSize + offsetX,
              row * cellSize + offsetY,
              (col + 1) * cellSize + offsetX,
              (row + 1) * cellSize + offsetY,
              gridPaint);
          canvas.drawLine(
              (col + 1) * cellSize + offsetX,
              row * cellSize + offsetY,
              col * cellSize + offsetX,
              (row + 1) * cellSize + offsetY,
              gridPaint);
        }
        // 如果当前需要显示提示，并且当前格子没有被选中，绘制一个蓝色的格子
        else if (showSolution && game.getSolution(row, col) == 1) {
          canvas.drawRect(
              col * cellSize + offsetX,
              row * cellSize + offsetY,
              (col + 1) * cellSize + offsetX,
              (row + 1) * cellSize + offsetY,
              solutionPaint);
        }
      }
    }
  }

  /**
   * 设置点击事件，根据点击的横纵坐标，计算出当前点击的是哪个格子，然后根据当前的模式，调用nonogram对象的toggleCell方法.
   *
   * @param event The motion event.
   * @return
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    performClick();
    int action = event.getAction();
    // 可以是按下当前格子，或者从其他格子滑动过来
    boolean isTouchDown = (action == MotionEvent.ACTION_DOWN);
    boolean isTouchMove = (action == MotionEvent.ACTION_MOVE);
    if (isTouchDown || isTouchMove) {
      // 根据当前的横纵坐标，计算出当前点击的是哪个格子
      int col = (int) ((event.getX() - offsetX) / cellSize);
      int row = (int) ((event.getY() - offsetY) / cellSize);
      // if语句避免isTouchMove重复判断同一个格子，导致鬼畜闪烁
      if (isTouchMove && col == prevCol && row == prevRow) {
        return false;
      }
      // 如果当前点击的格子在游戏范围内，更新当前的状态，然后重绘
      if (col >= 0 && col < game.getWidth() && row >= 0 && row < game.getHeight()) {
        prevCol = col;
        prevRow = row;
        game.toggleCell(row, col, this.mode);
        invalidate();
        return true;
      }
    }
    return false;
  }

  @Override
  // 要使用onTouchEvent，必需重写performClick方法
  public boolean performClick() {
    return super.performClick();
  }

  public void setGame(NonogramTutorial game) {
    this.game = game;
    invalidate();
  }

  private int[] reverse(int[] array) {
    // 反转数组，方便绘制提示数字
    int[] result = new int[array.length];
    for (int i = 0; i < array.length; i++) {
      result[i] = array[array.length - i - 1];
    }
    return result;
  }

  public void setMode(boolean mode) {
    // 更新状态，是要标记当前格子，还是要标记当前格子为错误
    this.mode = mode;
  }

  public void setShowSolution(boolean showSolution) {
    // 更新状态，要不要显示提示
    this.showSolution = showSolution;
    invalidate();
  }

  public NonogramTutorial getGame() {
    return game;
  }
}
