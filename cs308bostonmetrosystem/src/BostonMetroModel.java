import java.util.*;

public class BostonMetroModel implements multiGraphADT{

    int noNodes;

    LinkedList<Integer>[] adj;

    HashMap< ArrayList<Integer> , Integer>  edgeWeight = new HashMap<ArrayList<Integer>, Integer>();

    public BostonMetroModel(int n){

        this.noNodes = n;

        adj = new LinkedList[noNodes];
        for (int i = 0; i < noNodes ; i++) {
            adj[i] = new LinkedList<>();
        }

    }

    public int noOfNodes(){
        return noNodes;
    }

    public int noOfEdges(){
        int count=0;

        for (int i = 0 ; i<noOfNodes() ; i++) {
            for (int j = 0 ; j < adj[i].size() ; j++) {
                if ( adj[i].get(j) != null) {
                    count +=1;
                }
            }
        }
        return count;
    };

    public boolean addEdge(int node1, int node2, String colour){

        if(isEdge(node1, node2) || node1 ==0 || node2 ==0) {
            return false;
        }


        adj[node1].add(node2);
        ArrayList<Integer> add = new ArrayList<>();
        add.add(node1);
        add.add(node2);
        switch(colour){
            case "Orange":
                edgeWeight.put( add, 1);
                break;
            case "Blue":
                edgeWeight.put( add, 2);
                break;
            case "Green":
                edgeWeight.put( add, 3);
                break;
            case "Red":
                edgeWeight.put( add, 4);
                break;
            case "GreenB":
            case "GreenC":
            case "GreenD":
            case "GreenE":
                edgeWeight.put( add, 5);
                break;
            case "RedA":
                edgeWeight.put( add, 6);
                break;
            case "RedB":
                edgeWeight.put( add, 7);
                break;
            case "Mattapan":
                edgeWeight.put( add, 8);
                break;
            default:
                edgeWeight.put( add, 10);
                break;
        }


        return true;
    }

    public boolean isEdge(int node1, int node2){

        boolean found = false;

        for (int i = 0; i< adj[node1].size(); i++){
            if ( adj[node1].get(i) == node2) {
                found = true;
            }
        }

        return found;

    }

    public ArrayList<Integer> successors(int node){
        ArrayList<Integer> successorNodes = new ArrayList<Integer>();

        for (int i = 0 ; i<adj[node].size() ; i++) {
            successorNodes.add(adj[node].get(i));
        }

        return successorNodes;
    }

    public Integer getWeight(int node1, int node2){

        ArrayList<Integer> add = new ArrayList<>();
        add.add(node1);
        add.add(node2);

        return edgeWeight.get(add);

    }
}
