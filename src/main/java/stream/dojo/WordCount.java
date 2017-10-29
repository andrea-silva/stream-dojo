package stream.dojo;

@SuppressWarnings("WeakerAccess")
public class WordCount {
    private final String word;
    private final Long count;

    public WordCount(String word, Long count) {
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public Long getCount() {
        return count;
    }

    @Override
    public String toString() {
        return String.format("%s %d", word, count);
    }
}
