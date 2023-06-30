package ru.vsu.cs.ereshkin_a_v.task07;

import java.util.*;

public class CombinationGenerator {
	public static Map<Integer, List<List<Integer>>> generateCombinations(int max) {
		Map<Integer, List<List<Integer>>> combinationsMap = new HashMap<>();

		for (int n = 1; n < max; n++) {
			List<List<Integer>> combinations = new ArrayList<>();
			generateCombinationsHelper(combinations, new ArrayList<>(), max, n, 0, new HashSet<>());
			combinationsMap.put(n, combinations);
		}

		return combinationsMap;
	}

	private static void generateCombinationsHelper(List<List<Integer>> combinations, List<Integer> currentCombination,
	                                               int max, int n, int start, Set<Integer> used) {
		if (currentCombination.size() == n) {
			combinations.add(new ArrayList<>(currentCombination));
			return;
		}

		for (int i = start; i < max; i++) {
			if (!used.contains(i)) {
				currentCombination.add(i);
				used.add(i);
				generateCombinationsHelper(combinations, currentCombination, max, n, i + 1, used);
				used.remove(i);
				currentCombination.remove(currentCombination.size() - 1);
			}
		}
	}
}
