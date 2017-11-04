package stream.dojo;

import org.junit.jupiter.api.Test;
import stream.dojo.solution.StreamDojo1Solution;

import java.net.URI;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StreamDojo1Test {

    private StreamDojo1 streamDojo1 = new StreamDojo1();
    private StreamDojo1Solution streamDojo1Solution = new StreamDojo1Solution();

    @Test
    void shouldWordsCountByType() throws Exception {
        final URI uri = ClassLoader.getSystemResource("numbersFile.txt").toURI();
        final String mainPath = Paths.get(uri).toString();

        final long[] wordsCounts = streamDojo1.findWordsCountByType(mainPath);

        assertNotNull(wordsCounts);
        assertEquals(3, wordsCounts.length);
        assertEquals(3, wordsCounts[0]);
        assertEquals(4, wordsCounts[1]);
        assertEquals(4, wordsCounts[2]);
    }

    @Test
    void shouldFindThreeMostFrequentWords() throws Exception {
        final URI uri = ClassLoader.getSystemResource("files").toURI();
        final String mainPath = Paths.get(uri).toString();

        final List<WordCount> wordsCounts = streamDojo1.findThreeMostFrequentWordsInDir(mainPath);

        assertNotNull(wordsCounts);
        assertEquals(3, wordsCounts.size());
        assertEquals("the", wordsCounts.get(0).getWord());
        assertEquals(Long.valueOf(44), wordsCounts.get(0).getCount());
        assertEquals("you", wordsCounts.get(1).getWord());
        assertEquals(Long.valueOf(38), wordsCounts.get(1).getCount());
        assertEquals("and", wordsCounts.get(2).getWord());
        assertEquals(Long.valueOf(38), wordsCounts.get(2).getCount());
    }

    @Test
    void shouldFindTheNumberOfTripletsSum() {
        final long numberOfTripletsSum = streamDojo1.findTheNumberOfTripletsSum();

        assertEquals(2601, numberOfTripletsSum);
    }

    @Test
    void shouldFindTheFirstFiveSmallestTaxicabNumbers() {
        final List<Long> taxicabNumbers = streamDojo1.findTheFirstFiveSmallestTaxicabNumbers();

        assertNotNull(taxicabNumbers);
        assertEquals(5, taxicabNumbers.size());
        assertEquals(Long.valueOf(1729), taxicabNumbers.get(0));
        assertEquals(Long.valueOf(4104), taxicabNumbers.get(1));
        assertEquals(Long.valueOf(13832), taxicabNumbers.get(2));
        assertEquals(Long.valueOf(20683), taxicabNumbers.get(3));
        assertEquals(Long.valueOf(32832), taxicabNumbers.get(4));

    }
}