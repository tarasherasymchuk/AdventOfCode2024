package ua.aoc.day2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import ua.aoc.day1.Day1;

public class Day2 {

  public static void main(final String[] args) throws Exception {
    final List<List<Integer>> reports = parseInputData();
    final var count = reports.stream().filter(report -> {
      final var preValidatedReports = IntStream
              .iterate(report.size() - 1, i -> i - 1).limit(report.size())
              .mapToObj(index -> validateReport(index, report))
              .toList();
      final var secondValidation = IntStream
              .iterate(report.size() - 1, i -> i - 1)
              .limit(report.size()).anyMatch(indexToRemove -> {
                final List<Integer> modifiedReport = IntStream
                        .range(0, report.size())
                        .filter(i -> i != indexToRemove)
                        .mapToObj(report::get).toList();
                return IntStream.iterate(modifiedReport.size() - 1, i -> i - 1)
                        .limit(modifiedReport.size())
                        .mapToObj(index -> validateReport(index, modifiedReport))
                        .allMatch(Pair::result);
              });
      return preValidatedReports.stream().allMatch(Pair::result) || secondValidation;
    }).count();
    System.out.println(count);
  }

  private static Pair validateReport(final int index, final List<Integer> r) {
    final var current = r.get(index);
    if (index >= 1 && index < r.size() - 1) {
      final var next = r.get(index - 1);
      final var prev = r.get(index + 1);
      // validate asc/desc sequence
      final var prevFaulty = current < prev && current < next;
      final var nextFaulty = current > prev && current > next;
      if (prevFaulty || nextFaulty) {
        return new Pair(nextFaulty ? index + 1 : index - 1, false);
      }
    }
    final Integer previous;
    if (index == 0) {
      previous = r.get(index + 1);
    } else {
      previous = r.get(index - 1);
    }
    // validate max diff between adjacent reports
    return new Pair(index, (current > previous && (current - previous >= 1 && current - previous <= 3)) || (current < previous && (previous - current >= 1 && previous - current <= 3)));
  }

  private static List<List<Integer>> parseInputData() throws Exception {
    final String separatorRegex = "\\s+";
    final List<List<Integer>> reports = new ArrayList<>();
    try (final var inputStream = Day1.class.getResourceAsStream("/day2/input.txt")) {
      if (inputStream == null) {
        throw new RuntimeException();
      }
      final var reader = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      while ((line = reader.readLine()) != null) {
        final var split = line.split(separatorRegex);
        if (split.length == 0) {
          throw new RuntimeException();
        }
        reports.add(Arrays.stream(split).map(Integer::valueOf).toList());
      }
      return reports;
    }
  }

  record Pair(int index, boolean result) {
  }
}
