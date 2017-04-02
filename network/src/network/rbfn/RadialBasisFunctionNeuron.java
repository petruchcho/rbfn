package network.rbfn;

import data.Vector;
import network.Neuron;
import utils.MatrixUtils;

import java.util.Arrays;
import java.util.Random;

class RadialBasisFunctionNeuron implements Neuron {

    private final int N;
    double[] c;
    private double[][] Q;

    RadialBasisFunctionNeuron(int inputVectorSize) {
        N = inputVectorSize;
        c = new double[N];
        Q = new double[N][N];
    }

    void initValues(Random random) {
        for (int i = 0; i < N; i++) {
            c[i] = 0;//random.nextDouble();
            Q[i][i] = 1; //22
        }
    }

    double getZ(int r, Vector x) {
        double z = 0;
        for (int j = 0; j < Q.length; j++) {
            z += Q[j][r] * (x.v()[j] - c[j]);
        }
        return z;
    }

    @Override
    public double output(double[] v) {
        double[] difVector = new double[v.length];
        for (int i = 0; i < difVector.length; i++) {
            difVector[i] = v[i] - c[i];
        }
        double[] vector = MatrixUtils.multiply(Q, difVector);
        return Math.exp(-0.5 * MatrixUtils.multiply(vector, vector));
    }

    double getDistToCenter(Vector v) {
        return MatrixUtils.getDist(c, v.v());
    }

    void modify(double y, double d, double u, double w, Vector x, double learningStep) {
        double[] deltaC = calculateDeltaC(y, d, u, w, x);
        double[][] deltaQ = calculateDeltaQ(y, d, u, w, x);
        for (int i = 0; i < N; i++) {
            c[i] -= learningStep * deltaC[i];
        }
        for (int j = 0; j < N; j++) {
            for (int r = 0; r < N; r++) {
                Q[j][r] += learningStep * 10 * deltaQ[j][r]; // TODO !!!!!
            }
        }
    }

    private double[][] calculateDeltaQ(double y, double d, double u, double w, Vector x) {
        double[][] deltaQ = new double[N][N];
        for (int j = 0; j < N; j++) {
            for (int r = 0; r < N; r++) {
                deltaQ[j][r] = -Math.exp(0.5 * u) * w * (y - d) * (x.v()[j] - c[j]) * getZ(r, x);
            }
        }
        return deltaQ;
    }

    private double[] calculateDeltaC(double y, double d, double u, double w, Vector x) {
        double[] deltaC = new double[N];
        for (int j = 0; j < N; j++) {
            for (int r = 0; r < N; r++) {
                deltaC[j] += Q[j][r] * getZ(r, x);
            }
            deltaC[j] *= -Math.exp(0.5 * u) * w * (y - d);
        }
        return deltaC;
    }

    void setCenter(Vector center) {
        this.c = Arrays.copyOf(center.v(), center.v().length);
    }

    void setSigma(double sigma) {
        for (int i = 0; i < N; i++) {
            Q[i][i] = sigma; //22
        }
    }
}
