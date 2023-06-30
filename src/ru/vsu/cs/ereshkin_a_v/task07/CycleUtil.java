package ru.vsu.cs.ereshkin_a_v.task07;

import ru.vsu.cs.course1.graph.Graph;

import java.util.LinkedList;
import java.util.Queue;

public class CycleUtil {
	public static boolean hasCycle(Graph g, boolean[] deleted) {
		boolean[] visited = new boolean[g.vertexCount()];
		boolean[] inProcess = new boolean[g.vertexCount()];

		for (int i = 0; i < g.vertexCount(); i++) {
			if (!visited[i] && !deleted[i]) {
				if (bfsHasCycle(g, i, visited, inProcess, deleted)) {
					return true;
				}
			}
		}

		return false;
	}

	private static boolean bfsHasCycle(Graph g, int startVertex, boolean[] visited,
	                                   boolean[] inProcess, boolean[] deleted) {
		Queue<Integer> queue = new LinkedList<>();
		queue.offer(startVertex);
		visited[startVertex] = true;
		inProcess[startVertex] = true;

		while (!queue.isEmpty()) {
			int currentVertex = queue.poll();
			inProcess[currentVertex] = false;

			for (int neighbor : g.adjacencies(currentVertex)) {
				if (!visited[neighbor] && !deleted[neighbor]) {
					queue.offer(neighbor);
					visited[neighbor] = true;
					inProcess[neighbor] = true;
				} else if (inProcess[neighbor]) {
					// Обнаружен цикл
					return true;
				}
			}
		}

		return false;
	}
}
