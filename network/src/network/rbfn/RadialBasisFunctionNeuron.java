package network.rbfn;

import data.Vector;
import network.Neuron;
import utils.MatrixUtils;

import java.util.Random;

class RadialBasisFunctionNeuron implements Neuron {

    private final int N;
    private double[] c;
    private double[][] Q;

    RadialBasisFunctionNeuron(int inputVectorSize) {
        N = inputVectorSize;
        c = new double[N];
        Q = new double[N][N];
    }

    void initValues(Random random) {
        for (int i = 0; i < N; i++) {
            c[i] = 0;
            Q[i][i] = 1;
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
    public double output(Vector v) {
        double[] vector = MatrixUtils.multiply(Q, v.v());
        return Math.exp(-0.5 * MatrixUtils.multiply(vector, vector));
    }

    public double getDistToCenter(Vector v) {
        return MatrixUtils.getDist(c, v.v());
    }

    void modify(int y, double d, double u, double w, Vector x, double learningStep) {
        double[] deltaC = calculateDeltaC(y, d, u, w, x);
        double[][] deltaQ = calculateDeltaQ(y, d, u, w, x);
        for (int i = 0; i < N; i++) {
            c[i] -= learningStep * deltaC[i];
        }
        for (int j = 0; j < N; j++) {
            for (int r = 0; r < N; r++) {
                Q[j][r] -= learningStep * deltaQ[j][r];
            }
        }
    }

    private double[][] calculateDeltaQ(int y, double d, double u, double w, Vector x) {
        double[][] deltaQ = new double[N][N];
        for (int j = 0; j < N; j++) {
            for (int r = 0; r < N; r++) {
                deltaQ[j][r] = -Math.exp(0.5 * u) * w * (y - d) * (x.v()[j] - c[j]) * getZ(r, x);
            }
        }
        return deltaQ;
    }

    private double[] calculateDeltaC(int y, double d, double u, double w, Vector x) {
        double[] deltaC = new double[N];
        for (int j = 0; j < N; j++) {
            for (int r = 0; r < N; r++) {
                deltaC[j] += Q[j][r] * getZ(r, x);
            }
            deltaC[j] *= -Math.exp(0.5 * u) * w * (y - d);
        }
        return deltaC;
    }

    public void moveCenter(Vector x, double learningStep) {
        double[] c = new double[N];
        for (int i = 0; i < N; i++) {
            c[i] = this.c[i] + learningStep * (x.v()[i] - this.c[i]);
        }
        this.c = c;
    }
}
