package ru.vsu.cs.ereshkin_a_v.task07;

import ru.vsu.cs.course1.graph.Graph;

import java.util.*;

public class Task {
    public static List<Integer> getVerticesToDelete(Graph g){
        Map<Integer, List<List<Integer>>> combinations = CombinationGenerator.generateCombinations(g.vertexCount());
        for (int i = 0; i < g.vertexCount() - 1; i++) {
            List<List<Integer>> combinationForI = combinations.get(i + 1);
            for (List<Integer> list : combinationForI) {
                boolean[] deleted = getDeletedArrFromList(g, list);
                GraphState state = new GraphState(g, deleted);
                if (state.isTree()) return list;
            }
        }
        return new ArrayList<>();
    }
    private static boolean[] getDeletedArrFromList(Graph g, List<Integer> list) {
        boolean[] result = new boolean[g.vertexCount()];
        Arrays.fill(result, false);
        for (Integer i : list) {
            result[i] = true;
        }
        return result;
    }
}
