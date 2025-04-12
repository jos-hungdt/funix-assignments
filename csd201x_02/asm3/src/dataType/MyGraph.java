package dataType;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.Queue;

public class MyGraph {
    private int[][] data; 
    private int n;
    final char[] cityName = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

    public MyGraph() {
        this.data = null;
        n = 0;
    }

    public void clear() {
        this.data = null;
        n = 0;
    }

    public void setData(int[][] b) {
        this.n = b.length; 
        this.data = new int[n][n];
        int i, j;
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                this.data[i][j] = b[i][j];
            }
        }
    }

    public void displayAdj() {
        int i,j;
        
        System.out.println("\nThe adjacency matrix:"); 
        for (i = 0; i < n; i++) {
            System.out.println();
            
            for(j = 0; j < n; j++) {
                System.out.printf("%5d", this.data[i][j]);
            }
        }
        System.out.println();
    }

    public void depthFirstTraversal() {
        System.out.println("\n[INFO]: Depth-First traversal!");
        boolean[] visited = new boolean[this.n];
        int[] vertices = new int[this.n];
        for (int i = 0; i < this.n; i++) {
            vertices[i] = i;
        }

        MyStack<Integer> stack = new MyStack<Integer>(n);
        stack.push(vertices[0]);

        while (!stack.isEmpty()) {
            int current = stack.pop();

            if (!visited[current]) {
                visited[current] = true;
                System.out.print(cityName[current] + " ");
            }

            for (int i = 0; i < n; i++) {
                if (!visited[i] && this.data[current][i] != 9999 && this.data[current][i] != 0) {
                    stack.push(i);
                }
            }
        }
    }

    public void breadthFirstTraversal() {
        System.out.println("\n[INFO]: Breadth-First traversal!");

        Queue<Integer> queue = new LinkedList<>();

        // Initially mark all the vertices as not visited
        boolean[] visited = new boolean[this.n];

        visited[0] = true;
        queue.add(0);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            System.out.print(cityName[current] + " ");

            for (int i = 1; i < n; i++) {
                if (!visited[i] && this.data[current][i] != 9999 && this.data[current][i] != 0) {
                    visited[i] = true;
                    queue.add(i);
                }
            }
        }
    }

    public void dijkstra(int dest) {
        // this array is for storing the shortest distance from the source 
        // vertex to the current vertex. 
        int distance[] = new int[dest];

        // this array is used for logging the shortest path.
        String[] path = new String[dest];

        // passedVertex[i] will true if vertex i is included in
        // shortest path tree or shortest distance from src
        // to i is finalized
        boolean passedVertex[] = new boolean[dest];

        // Initialize all distances as max value and passedVertex as false
        for (int i = 0; i < dest; i++) {
            distance[i] = Integer.MAX_VALUE;
            passedVertex[i] = false;
        }

        // Distance of source vertex from itself is always 0
        distance[0] = 0;

        for (int count = 0; count < dest - 1; count++) {
            // Pick the minimum distance vertex from the set
            // of vertices not yet processed. 
            int u = getCurrentMinDistance(distance, passedVertex, dest);

            // Mark the picked vertex as processed
            passedVertex[u] = true;

            // Save the path and distance 
            path[count] = cityName[u] + " (" + distance[u] + ")";
            
            // Re-calculate the best distance to vertices.
            for (int v = 0; v < dest; v++) {
                if (!passedVertex[v] 
                    && this.data[u][v] != 0
                    && distance[u] != Integer.MAX_VALUE
                    && distance[u] + this.data[u][v] < distance[v]
                ) {
                    distance[v] = distance[u] + this.data[u][v];
                }
            }
        }

        System.out.println("\nThe shortest path from A to " + cityName[dest - 1] + " is: " );
        for (int i = 0; i < dest - 1; i++) {
            System.out.print(path[i] + " -> ");
        }
        IntSummaryStatistics stat = Arrays.stream(distance).summaryStatistics();
        System.out.println(cityName[dest - 1] + " (" + stat.getMax() + ")");
        System.out.println("The total distance is: " + stat.getMax());
    }

    int getCurrentMinDistance(int dist[], boolean passedVertex[], int dest)
    {
        int min = Integer.MAX_VALUE, min_index = -1;

        for (int v = 0; v < dest; v++)
            if (passedVertex[v] == false && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }

        return min_index;
    }
}
