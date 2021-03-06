package stream.dojo.solution;

import stream.dojo.Utils;
import stream.dojo.WordCount;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

public class StreamDojo1Solution {


    public long[] findWordsCountByType(String filePath) {
        final Path path = Paths.get(filePath);

        try (final Stream<String> lines = Files.lines(path)) {
            final Map<Boolean, Map<Boolean, Long>> partitions = lines.map(s -> s.split("\\s+"))
                    .flatMap(Arrays::stream)
                    .collect(partitioningBy(Utils::isDouble, partitioningBy(Utils::isInteger, counting())));
            return new long[] {
                    partitions.get(true).get(false),
                    partitions.get(false).get(true),
                    partitions.get(false).get(false),
            };

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Can you see the problem with this solution? Why is it not acceptable even if it makes the test pass?
     */
    public List<WordCount> findThreeMostFrequentWordsInDirWrongSolution(final String directoryPath) throws URISyntaxException {
        final Path path = Paths.get(directoryPath);
        try (
                final Stream<Path> paths = Files.list(path)
        ) {
            return paths.flatMap(StreamDojo1Solution::lines)
                    .map(s -> s.split("\\s+"))
                    .flatMap(Arrays::stream)
                    .map(Utils::sanitize)
                    .collect(groupingBy(identity(), counting()))
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.<String, Long>comparingByKey().reversed()))
                    .limit(3)
                    .map(entry -> new WordCount(entry.getKey(), entry.getValue()))
                    .collect(toList())
                    ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<WordCount> findThreeMostFrequentWordsInDir(final String directoryPath) throws URISyntaxException {
        final Path path = Paths.get(directoryPath);
        try (
                final Stream<Path> paths = Files.list(path);
                final Stream<String> lines = getLines(paths)
        ) {
            return lines
                    .map(s -> s.split("\\s+"))
                    .flatMap(Arrays::stream)
                    .map(Utils::sanitize)
                    .collect(groupingBy(identity(), counting()))
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.<String, Long>comparingByKey().reversed()))
                    .limit(3)
                    .map(entry -> new WordCount(entry.getKey(), entry.getValue()))
                    .collect(toList())
                    ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public long findTheNumberOfTripletsSum() {
        return IntStream.rangeClosed(0, 50)
                .boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100 - a)
                        .mapToObj(b -> new int[]{a, b, a + b}))
                .count();
    }

    public List<Long> findTheFirstFiveSmallestTaxicabNumbers() {
        return LongStream.iterate(1, n -> n + 1)
                .boxed()
                .flatMap(a -> LongStream.rangeClosed(1, a)
                        .boxed()
                        .flatMap(b -> LongStream.rangeClosed(1, a - 1)
                                .boxed()
                                .flatMap(c -> LongStream.rangeClosed(1, c)
                                        .mapToObj(d -> new long[]{a, b, c, d, (long) (Math.pow(a, 3) + Math.pow(b, 3)), (long) (Math.pow(c, 3) + Math.pow(d, 3))}))
                        )
                )
                .filter(x -> x[4] - x[5] == 0)
                .limit(5)
                .map(x -> x[4])
                .collect(toList());
    }

    private Stream<String> getLines(Stream<Path> paths) throws IOException {
        final List<String> lines = new ArrayList<>();
        for (Path path : paths.collect(Collectors.toSet())) {
            lines.addAll(Files.lines(path).collect(Collectors.toList()));
        }
        return lines.stream();
    }

    /**
     * Is it really safe to use this method inside a lambda?
     * You can sweep problems under the carpet, but sooner or later they'll come out to catch up with you.
     */
    private static Stream<String> lines(Path path) {
        try {
            return Files.lines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
