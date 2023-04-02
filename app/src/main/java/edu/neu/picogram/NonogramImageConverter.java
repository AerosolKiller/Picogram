package edu.neu.picogram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class NonogramImageConverter {
  public static boolean[][] convertToNonogramMatrix(String imagePath, int targetSize) {
    Bitmap originalBitmap = BitmapFactory.decodeFile(imagePath);
    Bitmap grayscaleBitmap = convertToGrayscale(originalBitmap);
    Bitmap resizedBitmap = resizeAndCropImage(grayscaleBitmap, targetSize);
    Bitmap edgeBitmap = detectEdges(resizedBitmap);
    boolean[][] binaryMatrix = imageToBinaryMatrix(edgeBitmap);
    return binaryMatrix;
  }

  public static boolean[][] convertToNonogramMatrix(
      Context context, int resources, int targetSize) {
    Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), resources);
    Bitmap grayscaleBitmap = convertToGrayscale(originalBitmap);
    Bitmap resizedBitmap = resizeAndCropImage(grayscaleBitmap, targetSize);
    //    Bitmap edgeBitmap = detectEdges(resizedBitmap);
    //    boolean[][] binaryMatrix = imageToBinaryMatrix(edgeBitmap);
    boolean[][] binaryMatrix = imageToBinaryMatrix(resizedBitmap);
    return binaryMatrix;
  }

  private static Bitmap convertToGrayscale(Bitmap originalBitmap) {
    Bitmap grayscaleBitmap =
        Bitmap.createBitmap(
            originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(grayscaleBitmap);
    Paint paint = new Paint();
    ColorMatrix colorMatrix = new ColorMatrix();
    colorMatrix.setSaturation(0);
    ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
    paint.setColorFilter(colorFilter);
    canvas.drawBitmap(originalBitmap, 0, 0, paint);
    return grayscaleBitmap;
  }

  private static Bitmap resizeAndCropImage(Bitmap grayscaleBitmap, int targetSize) {
    int width = grayscaleBitmap.getWidth();
    int height = grayscaleBitmap.getHeight();
    float aspectRatio = (float) width / height;

    int newWidth, newHeight;
    if (aspectRatio > 1) {
      newWidth = height;
      newHeight = height;
    } else {
      newWidth = width;
      newHeight = width;
    }

    int startX = (width - newWidth) / 2;
    int startY = (height - newHeight) / 2;
    Bitmap croppedBitmap =
        Bitmap.createBitmap(grayscaleBitmap, startX, startY, newWidth, newHeight);

    return Bitmap.createScaledBitmap(croppedBitmap, targetSize, targetSize, true);
  }

  private static Bitmap detectEdges(Bitmap resizedBitmap) {
    int width = resizedBitmap.getWidth();
    int height = resizedBitmap.getHeight();
    Bitmap edgeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(edgeBitmap);
    Paint paint = new Paint();
    paint.setAntiAlias(true);

    for (int y = 1; y < height - 1; y++) {
      for (int x = 1; x < width - 1; x++) {
        int pixelTopLeft = resizedBitmap.getPixel(x - 1, y - 1);
        int pixelTop = resizedBitmap.getPixel(x, y - 1);
        int pixelTopRight = resizedBitmap.getPixel(x + 1, y - 1);
        int pixelLeft = resizedBitmap.getPixel(x - 1, y);
        int pixelRight = resizedBitmap.getPixel(x + 1, y);
        int pixelBottomLeft = resizedBitmap.getPixel(x - 1, y + 1);
        int pixelBottom = resizedBitmap.getPixel(x, y + 1);
        int pixelBottomRight = resizedBitmap.getPixel(x + 1, y + 1);

        int grayScaleKernelX =
            -Color.red(pixelTopLeft)
                - 2 * Color.red(pixelLeft)
                - Color.red(pixelBottomLeft)
                + Color.red(pixelTopRight)
                + 2 * Color.red(pixelRight)
                + Color.red(pixelBottomRight);
        int grayScaleKernelY =
            -Color.red(pixelTopLeft)
                - 2 * Color.red(pixelTop)
                - Color.red(pixelTopRight)
                + Color.red(pixelBottomLeft)
                + 2 * Color.red(pixelBottom)
                + Color.red(pixelBottomRight);

        int edgeIntensity =
            (int)
                Math.sqrt(
                    grayScaleKernelX * grayScaleKernelX + grayScaleKernelY * grayScaleKernelY);
        int edgeColor = Color.argb(255, edgeIntensity, edgeIntensity, edgeIntensity);
        paint.setColor(edgeColor);
        canvas.drawPoint(x, y, paint);
      }
    }

    return edgeBitmap;
  }

  private static boolean[][] imageToBinaryMatrix(Bitmap edgeBitmap) {
    int width = edgeBitmap.getWidth();
    int height = edgeBitmap.getHeight();
    boolean[][] binaryMatrix = new boolean[height][width];

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int pixel = edgeBitmap.getPixel(x, y);
        binaryMatrix[y][x] = Color.red(pixel) > 128;
      }
    }

    return binaryMatrix;
  }
}
