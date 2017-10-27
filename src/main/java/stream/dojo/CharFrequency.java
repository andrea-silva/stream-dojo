package stream.dojo;

public class CharFrequency {
    private final Character chr;
    private final int frequency;

    private CharFrequency(Character chr, int frequency) {
        this.chr = chr;
        this.frequency = frequency;
    }

    public Character getChr() {
        return chr;
    }

    public int getFrequency() {
        return frequency;
    }
}
