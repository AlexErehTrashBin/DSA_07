package ru.vsu.cs.ereshkin_a_v.task07;

import ru.vsu.cs.course1.graph.Graph;

import java.util.LinkedList;
import java.util.Queue;

public class ConnectionUtil {
	public static boolean isFullyConnected(Graph g, boolean[] deleted) {
		int firstNonDeletedVertex = getFirstNonDeletedVertex(deleted);
		return isFullyConnected(g, firstNonDeletedVertex, deleted);
	}

	private static int getFirstNonDeletedVertex(boolean[] deleted) {
		for (int i = 0; i < deleted.length; i++) {
			if (!deleted[i]) return i;
		}
		return -1;
	}

	private static boolean isFullyConnected(Graph graph, int vertex, boolean[] deleted) {
		boolean[] visited = bfsWithVisitedReturn(graph, vertex, deleted);
		for (int i = 0; i < visited.length; i++) {
			if (!deleted[i] && !visited[i]) return false;
		}
		return true;
	}

	/**
	 * Поиск в ширину, реализованный с помощью очереди
	 * (начальная вершина также включена)
	 *
	 * @param graph   Граф
	 * @param from    Вершина, с которой начинается поиск
	 * @param deleted Массив с указанием какие вершины удалены (true - вершина удалена)
	 */
	public static boolean[] bfsWithVisitedReturn(Graph graph, int from, boolean[] deleted) {
		boolean[] visited = new boolean[graph.vertexCount()];
		Queue<Integer> queue = new LinkedList<>();
		queue.add(from);
		visited[from] = true;
		while (!queue.isEmpty()) {
			Integer curr = queue.remove();
			for (Integer v : graph.adjacencies(curr)) {
				if (!visited[v] && !deleted[v]) {
					queue.add(v);
					visited[v] = true;
				}
			}
		}
		return visited;
	}
}
