package utils;

import java.awt.geom.Point2D;

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

    public static double getDist(double[] v1, double[] v2) {
        double length = 0;
        for (int i = 0; i < v1.length; i++) {
            double dv = v1[i] - v2[i];
            length += dv * dv;
        }
        return Math.sqrt(length);
    }
}
