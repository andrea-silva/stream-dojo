package stream.dojo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("Convert2Lambda")
class StreamDojo1 {


    /**
     * Given a directory containing only text files (only ASCII chars allowed) and no sub folder,
     * find the 3 most frequent words and their count. In case of ties, favor
     * words that comes last in alphabet order.
     *
     * Words have to be sanitized using {@link Utils#sanitize(String)}
     *
     * The clumsy imperative-style solution below, has to be refactored using Java 8 features
     *
     * Tip: java.nio is more stream-friendly than java.io.
     * Consider using {@link Files#list(Path)} and {@link Files#lines(Path)}, but be careful about not leaving file handles
     * open in case of exception.
     *
     * Extra challenge: how about using parallel streams for this problem? Is it possible? Would it help?
     *
     * Have fun ;-)
     */
    List<WordCount> findThreeMostFrequentWordsInDir(final String directoryPath) throws URISyntaxException {
        //Put all the words in a map
        final Map<String, Long> wordCountMap = new HashMap<>();
        final File mainDir = new File(directoryPath);
        final File[] files = mainDir.listFiles();
        assert files != null;
        for (File file : files) {
            try(final BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    final String[] words = line.split("\\s+");
                    for (String word : words) {
                        String sanitizedWord = Utils.sanitize(word);
                        final Long count = wordCountMap.get(sanitizedWord);
                        if(count == null) {
                            wordCountMap.put(sanitizedWord, 1L);
                        } else {
                            wordCountMap.put(sanitizedWord, count+1);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //Transform the map in an array on entries and sort it
        @SuppressWarnings("unchecked")
        final Map.Entry<String, Long>[] wordsCounts = new ArrayList<>(wordCountMap.entrySet()).toArray(new Map.Entry[wordCountMap.size()]);
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

}
