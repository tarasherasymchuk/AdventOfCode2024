package ua.aoc.day3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ua.aoc.day1.Day1;

public class Day3 {

  public static void main(final String[] args) throws Exception {
    final var inputData = parseInputData();
    final var partOneTotal = partOne(inputData);
    System.out.println(partOneTotal);
    final var partTwoTotal = partTwo(inputData);
    System.out.println(partTwoTotal);
  }

  private static int partOne(final String inputData) {
    final Pattern funcCallPattern = Pattern.compile("(mul)\\((\\d+),(\\d+)\\)");
    final Matcher matcher = funcCallPattern.matcher(inputData);
    int total = 0;
    while (matcher.find()) {
      final int firstNumber = Integer.parseInt(matcher.group(2));
      final int secondNumber = Integer.parseInt(matcher.group(3));
      total += (firstNumber * secondNumber);
    }
    return total;
  }

  private static int partTwo(final String inputData) {
    int total = 0;

    final Pattern mulPattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
    final Pattern doPattern = Pattern.compile("do\\(\\)");
    final Pattern dontPattern = Pattern.compile("don't\\(\\)");

    boolean mulEnabled = true;

    final Matcher matcher = Pattern.compile("mul\\(\\d+,\\d+\\)|do\\(\\)|don't\\(\\)").matcher(inputData);
    while (matcher.find()) {
      final String match = matcher.group();

      if (doPattern.matcher(match).matches()) {
        mulEnabled = true;
      } else if (dontPattern.matcher(match).matches()) {
        mulEnabled = false;
      } else if (mulPattern.matcher(match).matches() && mulEnabled) {
        final Matcher mulMatcher = mulPattern.matcher(match);
        if (mulMatcher.matches()) {
          final int firstNumber = Integer.parseInt(mulMatcher.group(1));
          final int secondNumber = Integer.parseInt(mulMatcher.group(2));
          total += (firstNumber * secondNumber);
        }
      }
    }
    return total;
  }

  private static String parseInputData() throws Exception {
    try (final var inputStream = Day1.class.getResourceAsStream("/day3/input.txt")) {
      if (inputStream == null) {
        throw new RuntimeException();
      }
      final var reader = new BufferedReader(new InputStreamReader(inputStream));
      return reader.lines().collect(Collectors.joining());
    }
  }
}
