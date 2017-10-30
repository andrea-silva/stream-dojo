package stream.dojo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.LongUnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

@SuppressWarnings({"Convert2Lambda", "WeakerAccess", "ForLoopReplaceableByForEach"})
class StreamDojo1 {

    /**
     * Given a text files you should return an array of 3 elements as follows:
     * <p>
     * [numberOfDoubles, numberOfIntegers, numberOfWordsThatAreNotNumbers]
     * <p>
     * Words are separated by one or more blank chars (spaces, tabs, new lines).
     * <p>
     * In the context of this exercise you can use the following over simplistic functionns to check is a word is a double
     * or an integer:
     * <p>
     * {@link Utils#isDouble(String)}
     * {@link Utils#isInteger(String)}
     * <p>
     * Note: no word that will be considered at the same time an integer and a double.
     * <p>
     * Example:
     * 2   4.9 abc
     * -45 +2
     * <p>
     * the expected result is [1, 3, 1]
     * <p>
     * Refactor the following code to lambda and streams.
     */
    public long[] findWordsCountByType(String filePath) {
        final long[] counts = {0, 0, 0};
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] words = line.split("\\s+");
                for (int i = 0; i < words.length; i++) {
                    if (Utils.isDouble(words[i])) {
                        counts[0] = counts[0] + 1;
                    } else if (Utils.isInteger(words[i])) {
                        counts[1] = counts[1] + 1;
                    } else {
                        counts[2] = counts[2] + 1;
                    }
                }
            }

            return counts;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Given a directory containing only text files (only ASCII chars allowed) and no sub folder,
     * find the 3 most frequent words and their count. In case of ties, favor
     * words that comes last in alphabet order.
     * <p>
     * Words have to be sanitized using {@link Utils#sanitize(String)}
     * <p>
     * The clumsy imperative-style solution below, has to be refactored using Java 8 features
     * <p>
     * Tip: java.nio is more stream-friendly than java.io.
     * Consider using {@link Files#list(Path)} and {@link Files#lines(Path)}, but be careful about not leaving file handles
     * open in case of exception.
     * <p>
     * Extra challenge: how about using parallel streams for this problem? Is it possible? Would it help?
     * <p>
     * Have fun ;-)
     */
    public List<WordCount> findThreeMostFrequentWordsInDir(final String directoryPath) throws URISyntaxException {
        //Put all the words in a map
        final Map<String, Long> wordCountMap = new HashMap<>();
        final File mainDir = new File(directoryPath);
        final File[] files = mainDir.listFiles();
        assert files != null;
        for (File file : files) {
            try (final BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    final String[] words = line.split("\\s+");
                    for (String word : words) {
                        String sanitizedWord = Utils.sanitize(word);
                        final Long count = wordCountMap.get(sanitizedWord);
                        if (count == null) {
                            wordCountMap.put(sanitizedWord, 1L);
                        } else {
                            wordCountMap.put(sanitizedWord, count + 1);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //Transform the map in an array on entries and sort it
        @SuppressWarnings("unchecked") final Map.Entry<String, Long>[] wordsCounts = new ArrayList<>(wordCountMap.entrySet()).toArray(new Map.Entry[wordCountMap.size()]);
        Arrays.sort(wordsCounts, new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> entry1, Map.Entry<String, Long> entry2) {
                long countComparison = entry2.getValue() - entry1.getValue();
                if (countComparison == 0) {
                    return entry2.getKey().compareTo(entry1.getKey());
                }
                return (int) countComparison;
            }

        });

        //Convert to the final desired type and limit the number of results
        final List<WordCount> counts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            counts.add(new WordCount(wordsCounts[i].getKey(), wordsCounts[i].getValue()));
        }

        return counts;
    }

    /**
     * This exercise is a warm-up on java 8 numeric streams.
     * <p>
     * Let's define a triplet sum as an array [a,b,c] where
     * a, b and c are integers with a value between 0 (inclusive) and 100 (inclusive)
     * <p>
     * and a <= b
     * <p>
     * and a + b = c
     * <p>
     * Find the number of distinct triplets that match the above definition.
     * <p>
     * You have to refactor the code below to use java streams.
     * <p>
     * Tip: consider using:
     * <p>
     * {@link IntStream#rangeClosed(int, int)} and {@link IntStream#mapToObj(IntFunction)}
     */
    public long findTheNumberOfTripletsSum() {
        final List<int[]> triplets = new ArrayList<>();
        for (int i = 0; i <= 50; i++) {
            for (int j = i; j <= 100; j++) {
                if (i + j > 100) {
                    break;
                }
                triplets.add(new int[]{i, j});
            }
        }
        return triplets.size();
    }


    /**
     * A taxicab number of order N is an integer that can be expressed as the sum of two positive cube numbers in N distinct ways.
     * Usually, only the smallest of such numbers is considered a taxicab number. In this exercise we are going to relax this definition
     * and we will consider taxicab numbers of order 2 all the numbers that can be expressed as
     * <p>
     * X = a^3 + b^3 and X = c^3 + d^3
     * <p>
     * where a != c and a != d
     * <p>
     * For instance 1729 is a taxicab number because
     * <p>
     * 1729 = 1^3 + 12^3
     * 1729 = = 9^3 + 10^3
     * <p>
     * Your mission, should you choose to accept it, is to refactor the following code
     * to find the 2 smallest taxicab numbers of order 2 using java 8 numeric streams.
     * <p>
     * This is a tough one!
     * <p>
     * N.B. The following solution is a brute-force one. For resolve the problem efficiently,
     * a priority queue can be used (see for instance https://algs4.cs.princeton.edu/24pq/Taxicab.java.html).
     * The aim here is not to find an efficient solution, but just to refactor the imperative code to
     * functional style.
     * <p>
     * Tip: in order to generate the stream consider using
     * <p>
     * {@link LongStream#range(long, long)} and/or {@link LongStream#iterate(long, LongUnaryOperator)}
     */
    public List<Long> findTheFirstFiveSmallestTaxicabNumbers() {
        final List<Long> taxicabNumber = new ArrayList<>();
        for (long a = 1; ; a++) {
            final long a3 = (long) Math.pow(a, 3);
            for (long b = 1; b <= a; b++) {
                final long b3 = (long) Math.pow(b, 3);
                for (long c = 1; c < a; c++) {
                    final long c3 = (long) Math.pow(c, 3);
                    for (long d = 1; d <= c; d++) {
                        final long d3 = (long) Math.pow(d, 3);
                        if (a3 + b3 == c3 + d3) {
                            taxicabNumber.add(a3 + b3);
                            if (taxicabNumber.size() == 5) {
                                return taxicabNumber;
                            }
                        }
                    }
                }
            }
        }
    }


}
