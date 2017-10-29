package stream.dojo;


public class GaussianPair {
    private final int a;
    private final int b;


    public GaussianPair(int a, int b) {
        if(a + b != 100) {
            throw new IllegalArgumentException("The sum of elements should be 100");
        }
        if (a<=0 || b <=0) {
            throw new IllegalArgumentException("Elements should be strictly positive");
        }
        if (b < a) {
            throw new IllegalArgumentException("First element should be greater or equal to the second");
        }

        this.a = a;
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    @Override
    public String toString() {
        return "GaussianPair{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}
