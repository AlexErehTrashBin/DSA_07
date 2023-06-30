package ru.vsu.cs.course1.graph;

/**
 * Интерфейс для описания неориентированного графа (н-графа)
 * с реализацией некоторых методов графа
 */
public interface Graph {
    /**
     * Кол-во вершин в графе
     */
    int vertexCount();

    /**
     * Кол-во ребер в графе
     */
    int edgeCount();

    /**
     * Добавление ребра между вершинами с номерами v1 и v2
     */
    void addEdge(int v1, int v2);

    /**
     * Удаление ребра/ребер между вершинами с номерами v1 и v2
     */
    void removeEdge(int v1, int v2);

    /**
     * @param v Номер вершины, смежные с которой необходимо найти
     * @return Объект, поддерживающий итерацию по номерам связанных с v вершин
     */
    Iterable<Integer> adjacencies(int v);

    /**
     * Проверка смежности двух вершин
     */
    default boolean isAdj(int v1, int v2) {
        for (Integer adj : adjacencies(v1)) {
            if (adj == v2) {
                return true;
            }
        }
        return false;
    }
}
