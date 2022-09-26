import java.util.*;


public interface multiGraphADT {

    int noOfNodes();

    int noOfEdges();

    boolean addEdge(int Node1, int Node2, String colour);

    boolean isEdge(int node1, int node2);

    ArrayList<Integer> successors(int node);

    Integer getWeight(int node1, int node2);

}

