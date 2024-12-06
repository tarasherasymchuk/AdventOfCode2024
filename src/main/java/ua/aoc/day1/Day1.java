package ua.aoc.day1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.counting;

public class Day1 {

  private final InputData inputData;

  public Day1(final InputData inputData) {
    this.inputData = inputData;
  }

  public static void main(final String[] args) throws Exception {
    final var inputData = parseInputData();
    final var day1 = new Day1(inputData);
    final var distance = day1.calculateDistance();
    System.out.println("The distance between two lists is " + distance);
    final var similarityScore = day1.calculateSimilarityScore();
    System.out.println("The similarity score is " + similarityScore);
  }

  private static InputData parseInputData() throws Exception {
    final String separatorRegex = "\\s+";
    final List<String> left = new ArrayList<>();
    final List<String> right = new ArrayList<>();
    try (final var inputStream = Day1.class.getResourceAsStream("/day1/input.txt")) {
      if (inputStream == null) {
        throw new RuntimeException();
      }
      final var reader = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      while ((line = reader.readLine()) != null) {
        final var split = line.split(separatorRegex);
        if (split.length != 2) {
          throw new RuntimeException();
        }
        left.add(split[0]);
        right.add(split[1]);
      }
      final var leftInts = left.stream().map(Long::valueOf).toList();
      final var rightInts = right.stream().map(Long::valueOf).toList();
      return new InputData(leftInts, rightInts);
    }
  }

  private Long calculateDistance() {
    final var left = this.inputData.left().stream().sorted().toList();
    final var right = this.inputData.right().stream().sorted().toList();
    return IntStream.range(0, left.size())
            .mapToLong(index -> Math.absExact(left.get(index) - right.get(index)))
            .sum();
  }

  private Long calculateSimilarityScore() {
    final var rightNumbersToAppearences = this.inputData.right().stream().collect(Collectors.groupingBy(identity(), counting()));
    return IntStream.range(0, this.inputData.left().size()).mapToLong(index -> {
      final var left = this.inputData.left();
      final var leftNumber = left.get(index);
      return leftNumber * rightNumbersToAppearences.getOrDefault(leftNumber, 0L);
    }).sum();
  }

  public record InputData(List<Long> left, List<Long> right) {
  }
}
