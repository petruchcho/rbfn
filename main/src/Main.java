import data.DataHolder;

public class Main {
    public static void main(String[] args) {
        DataHolder<Seed> seedHolder = new DataHolder<>(new SeedReader());
        System.out.println(seedHolder.getData().size());
    }
}
