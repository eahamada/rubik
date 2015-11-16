package rubik;

import java.util.function.Function;

public enum MatrixOperations {
  INSTANCE;
  public static int[] multiplyVect(int[] u, int[] v) {
    if (u == null || v == null) {
      throw new IllegalArgumentException();
    }
    int m = u.length;
    int n = v.length;
    if (m != n || m != 3)
      throw new IllegalArgumentException();
    return new int[] { u[2] * v[3] - u[3] * v[2], u[3] * v[1] - u[1] * v[3],
        u[1] * v[2] - u[2] * v[1] };
  }

  // return x^T y
  public static int dot(int[] x, int[] y) {
    if (x.length != y.length)
      throw new RuntimeException("Illegal vector dimensions.");
    int sum = 0;
    for (int i = 0; i < x.length; i++)
      sum += x[i] * y[i];
    return sum;
  }

  // matrix-vector multiplication (y = A * x)
  public static int[] multiply(int[] A, int x) {
    return multiply(COLUMN_TO_MATRIX.apply(A), x)[0];
  }

  public static int[][] multiply(int[][] a, int x) {
    if (a == null) {
      throw new IllegalArgumentException();
    }
    int m = a.length;
    int n = a[0].length;
    int[][] result = new int[m][n];
    for (int i = 0; i < m; i++)
      for (int j = 0; j < n; j++)
        result[i][j] = a[i][j] * x;
    return result;
  }

  // return C = A + B
  public static int[] add(int[] A, int[] B) {

    return add(COLUMN_TO_MATRIX.apply(A), COLUMN_TO_MATRIX.apply(B))[0];
  }

  // return C = A + B
  public static int[][] add(int[][] A, int[][] B) {
    int m = A.length;
    int n = A[0].length;
    int[][] C = new int[m][n];
    for (int i = 0; i < m; i++)
      for (int j = 0; j < n; j++)
        C[i][j] = A[i][j] + B[i][j];
    return C;
  }

  // return C = A * B
  public static int[] multiply(int[] A, int[] B) {
   
    return multiply(COLUMN_TO_MATRIX.apply(A), COLUMN_TO_MATRIX.apply(B))[0];

  }
  public final static Function<int[], int[][]> COLUMN_TO_MATRIX = a -> {
    int[][] result = new int[a.length][1];
    for (int i = 0; i < a.length; i++) {
      result[i][0]= a[i];
    }
    return result;
  };
  public final static Function<int[][], int[]> MATRIX_TO_COLUMN = a -> {
    int[] result = new int[a.length];
    for (int i = 0; i < a.length; i++) {
      result[i]= a[i][0];
    }
    return result;
  };

  // return C = A * B
  public static int[][] multiply(int[][] A, int[][] B) {
    int mA = A.length;
    int nA = A[0].length;
    int mB = B.length;
    int nB = B[0].length;
    if (nA != mB)
      throw new RuntimeException("Illegal matrix dimensions.");
    int[][] C = new int[mA][nB];
    for (int i = 0; i < mA; i++)
      for (int j = 0; j < nB; j++)
        for (int k = 0; k < nA; k++)
          C[i][j] += A[i][k] * B[k][j];
    return C;
  }
}
