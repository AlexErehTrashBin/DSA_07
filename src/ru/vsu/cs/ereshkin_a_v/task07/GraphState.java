package ru.vsu.cs.ereshkin_a_v.task07;

import ru.vsu.cs.course1.graph.Graph;

public class GraphState {
	private final boolean[] deleted;
	private final Graph g;

	public GraphState(Graph g, boolean[] deleted) {
		this.g = g;
		this.deleted = deleted;
	}

	public boolean isTree(){
		return !isCyclic() && isFullyConnected();
	}

	private boolean isFullyConnected() {
		return ConnectionUtil.isFullyConnected(g, deleted);
	}

	private boolean isCyclic(){
		return CycleUtil.hasCycle(g, deleted);
	}


}
