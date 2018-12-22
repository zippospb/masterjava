package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final List<Future<int[]>> futures = new ArrayList<>();
        for (int i = 0; i < matrixSize; i++) {
            final int[] matrixBColumn = new int[matrixSize];
            for (int k = 0; k < matrixSize; k++) {
                matrixBColumn[k] = matrixB[k][i];
            }
            futures.add(executor.submit(() -> multiply(matrixA, matrixBColumn)));
        }
        for (int i = 0; i < matrixSize; i++) {
            try {
                matrixC[i] = futures.get(i).get(10, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(final int[][] matrixA, final int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final int[] matrixBColumn = new int[matrixSize];

        try {
            for (int i = 0; ; i++) {
                for (int k = 0; k < matrixSize; k++) {
                    matrixBColumn[k] = matrixB[k][i];
                }

                for (int j = 0; j < matrixSize; j++) {
                    int sum = 0;
                    int[] matrixARow = matrixA[j];
                    for (int k = 0; k < matrixSize; k++) {
                        sum += matrixARow[k] * matrixBColumn[k];
                    }
                    matrixC[i][j] = sum;
                }
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int[] multiply(int[][] matrixA, int[] matrixBColumn) {
        final int matrixSize = matrixBColumn.length;
        final int[] multiplyRow = new int[matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            int sum = 0;
            int[] matrixARow = matrixA[i];
            for (int k = 0; k < matrixSize; k++) {
                sum += matrixARow[k] * matrixBColumn[k];
            }
            multiplyRow[i] = sum;
        }
        return multiplyRow;
    }
}
