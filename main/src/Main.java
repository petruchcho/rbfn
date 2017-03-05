import data.DataHolder;
import network.rbfn.RadialBasisFunctionNetwork;

public class Main {
    public static void main(String[] args) {
        DataHolder<Seed> seedHolder = new DataHolder<>(new SeedReader());
        Seed v = seedHolder.getData().get(0);
        RadialBasisFunctionNetwork network = new RadialBasisFunctionNetwork(3, v.asVector().getSize());


        System.out.println(network.calculateOutput(v.asVector()));
        for (int it = 0; it < 5; it++) {
            network.learn(v);
            System.out.println(network.calculateOutput(v.asVector()));
        }
    }
}
