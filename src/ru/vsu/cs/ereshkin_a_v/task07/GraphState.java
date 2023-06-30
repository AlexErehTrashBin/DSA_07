package ru.vsu.cs.ereshkin_a_v.task07;

import ru.vsu.cs.course1.graph.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphState {
	private final boolean[] deleted;
	private final Graph g;

	public GraphState(Graph g, boolean[] deleted) {
		this.g = g;
		this.deleted = deleted;
	}

	public boolean[] getDeletedArr() {
		return Arrays.copyOf(deleted, deleted.length);
	}

	public int getDeletedVerticesCount(){
		int count = 0;
		for (boolean b : deleted) {
			if (b) count++;
		}
		return count;
	}
	public List<Integer> getDeletedVertices(){
		List<Integer> result = new ArrayList<>();
		for (int i = 0; i < deleted.length; i++) {
			if (deleted[i]) result.add(i);
		}
		return result;
	}

	public boolean isTree(){
		return !isCyclic() && !isFullyConnected();
	}

	private boolean isFullyConnected() {
		return !ConnectionUtil.isFullyConnected(g, deleted);
	}

	private boolean isCyclic(){
		return CycleUtil.hasCycle(g, deleted);
	}


	public int getDeletedCount(){
		return deleted.length;
	}
}
