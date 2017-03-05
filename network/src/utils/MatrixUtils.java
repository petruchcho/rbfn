package utils;

public class MatrixUtils {
    private MatrixUtils() {
    }

    public static double[] multiply(double[][] matrix, double[] v) {
        int n = v.length;
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[i] += v[i] * matrix[i][j];
            }
        }
        return result;
    }

    public static double multiply(double[] a, double[] b) {
        int n = a.length;
        double result = 0;
        for (int i = 0; i < n; i++) {
            result += a[i] * b[i];
        }
        return result;
    }
}
