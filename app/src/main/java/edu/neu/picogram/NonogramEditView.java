package edu.neu.picogram;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class NonogramEditView extends View {
    private Nonogram game;
    private Paint gridPaint;
    private Paint cluePaint;
    private Paint cellPaint;
    private Paint solutionPaint;
    private int prevCol = -1;
    private int prevRow = -1;
    private boolean mode = true;
    private float ratio = 0.75f;
    int cellSize;
    int offsetX;
    int offsetY;

    public NonogramEditView(Context context, AttributeSet attrs) {
        // 编辑游戏界面，继承View类，和NonogramView大体一样，多了个记录当前状态的函数
        super(context, attrs);
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
        // onDraw方法和NonogramView一样，只是取消了显示solution的功能
        cellSize =
                (int) (Math.min(getWidth() / game.getWidth(), getHeight() / game.getHeight()) * ratio);
        offsetX = (getWidth() - game.getWidth() * cellSize) / 2;
        offsetY = ((getHeight() - game.getHeight() * cellSize) / 2) + cellSize / 2;

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
        for (int row = 0; row < game.getHeight(); row++) {
            if ((game.getRowClues(row) == null) || (game.getRowClues(row).length == 0)) {
                String clue = "0";
                float xPosition = offsetX - cluePaint.getTextSize();
                float yPosition = (row + 0.5f) * cellSize + offsetY;
                canvas.drawText(clue, xPosition, yPosition, cluePaint);
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
            if ((game.getColClues(col) == null) || (game.getColClues(col).length == 0)) {
                String clue = "0";
                float xPosition = (col + 0.5f) * cellSize + offsetX;
                float yPosition = offsetY - cluePaint.getTextSize();
                canvas.drawText(clue, xPosition, yPosition, cluePaint);
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

        for (int row = 0; row < game.getHeight(); row++) {
            for (int col = 0; col < game.getWidth(); col++) {
                if (game.getCurrentGrid(row, col) == 1) {
                    canvas.drawRect(
                            col * cellSize + offsetX,
                            row * cellSize + offsetY,
                            (col + 1) * cellSize + offsetX,
                            (row + 1) * cellSize + offsetY,
                            cellPaint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // onTouchEvent方法和NonogramView一样
        // 多了当点击之后，添加了更改game实例的功能
        performClick();
        int action = event.getAction();
        boolean isTouchDown = (action == MotionEvent.ACTION_DOWN);
        boolean isTouchMove = (action == MotionEvent.ACTION_MOVE);
        if (isTouchDown || isTouchMove) {
            int col = (int) ((event.getX() - offsetX) / cellSize);
            int row = (int) ((event.getY() - offsetY) / cellSize);
            if (isTouchMove && col == prevCol && row == prevRow) {
                return false;
            }

            if (col >= 0 && col < game.getWidth() && row >= 0 && row < game.getHeight()) {
                prevCol = col;
                prevRow = row;
                game.toggleCell(row, col, this.mode);
                updateGame();
                invalidate();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void setGame(Nonogram game) {
        this.game = game;
        invalidate();
    }

    private int[] reverse(int[] array) {
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[array.length - i - 1];
        }
        return result;
    }

    private void updateGame() {
        // 点击之后，更新游戏内部的solution和clues
        // 根据当前的grid，计算出solution
        boolean[][] grid = new boolean[game.getHeight()][game.getWidth()];
        for (int row = 0; row < game.getHeight(); row++) {
            for (int col = 0; col < game.getWidth(); col++) {
                grid[row][col] = game.getCurrentGrid(row, col) == 1;
            }
        }
        game.setSolution(grid);
        // 计算出rowClues，为了方便先用List<List<Integer>>，最后转换成int[][]
        List<List<Integer>> rowClues = new ArrayList<>();
        int count = 0;
        for (int row = 0; row < game.getHeight(); row++) {
            List<Integer> rowClue = new ArrayList<>();
            for (int col = 0; col < game.getWidth(); col++) {
                if (grid[row][col]) {
                    count++;
                    if (col == game.getWidth() - 1) {
                        rowClue.add(count);
                        count = 0;
                    }
                } else {
                    if (count > 0) {
                        rowClue.add(count);
                        count = 0;
                    }
                }
            }
            rowClues.add(rowClue);
        }
        int[][] rowCluesArray = rowClues.stream().map(this::toIntArray).toArray(int[][]::new);
        game.setRowClues(rowCluesArray);
        // 计算出colClues，为了方便先用List<List<Integer>>，最后转换成int[][]
        List<List<Integer>> colClues = new ArrayList<>();
        count = 0;
        for (int col = 0; col < game.getWidth(); col++) {
            List<Integer> colClue = new ArrayList<>();
            for (int row = 0; row < game.getHeight(); row++) {
                if (grid[row][col]) {
                    count++;
                    if (row == game.getHeight() - 1) {
                        colClue.add(count);
                        count = 0;
                    }
                } else {
                    if (count > 0) {
                        colClue.add(count);
                        count = 0;
                    }
                }
            }
            colClues.add(colClue);
        }
        int[][] colCluesArray = colClues.stream().map(this::toIntArray).toArray(int[][]::new);
        game.setColClues(colCluesArray);
        invalidate();
    }

    private int[] toIntArray(List<Integer> list) {
        // 辅助函数方便把List<Integer>转换成int[]
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    public Nonogram getGame() {
        return game;
    }
}
