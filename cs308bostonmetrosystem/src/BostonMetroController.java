import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import java.util.*;
import java.util.List;



/*
This class is the controller as part of a MVC design pattern.
It will allow us to interact with the gui(view) and the data/computation (model) without interfering with any of these classes separately
 */
public class BostonMetroController {

    private BostonMetroView BMV;
    multiGraphADT graph;
    private static final int sizeOfGraph = 125;

    public BostonMetroController(){

        this.graph = new BostonMetroModel(sizeOfGraph);
        readIn();

        this.BMV = new BostonMetroView();

        loadMainButton();
        loadCords();

    }


    //returns the BostonMetroView Class
    public BostonMetroView getView() {
        return this.BMV;
    }

    //used to read in the provided data file, and build the graph
    public void readIn() {
        String path = "resources/bostonmetro.txt";

        try {
            Scanner sc = new Scanner(new File(path)); //scanner to read in the bostonmetro file that includes station names and edges

            while (sc.hasNextLine()) {

                String str = sc.nextLine();
                Scanner line = new Scanner(str);

                String id = line.next();
                String stationName = line.next();

                while(line.hasNext()){

                    String colour = line.next();
                    String prev = line.next();
                    String next = line.next();

                    int n1 = Integer.parseInt(id);
                    int n2 = Integer.parseInt(next);
                    int n3 = Integer.parseInt(prev);
                    graph.addEdge(n2,n1, colour );
                    graph.addEdge(n1,n2, colour );
                    graph.addEdge(n1,n3, colour );
                    graph.addEdge(n3,n1, colour );
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        }
    }


    //loading in coordinates from CSV file for the GUI
    public void loadCords()  {
        String path = "resources//stationMapPos.csv";

        try {
            Scanner sc = new Scanner(new File(path));
            sc.useDelimiter(",");
            while (sc.hasNextLine()) {

                String s = sc.nextLine();
                String[] splitStrings = s.split(",");
                int index, x, y;

                try {
                    index = Integer.parseInt(splitStrings[0]);

                } catch (NumberFormatException e) {
                    index = 1;
                    System.out.println("problem with reading index at pos" + index);
                }

                try {
                    x = Integer.parseInt(splitStrings[1]);
                } catch (NumberFormatException e) {
                    x = 999;
                    System.out.println("problem with reading x");
                }

                try {
                    y = Integer.parseInt(splitStrings[2]);
                } catch (NumberFormatException e) {
                    y = 999;
                    System.out.println("problem with reading y");
                }
                BMV.addButton(index,x,y);
            }

        } catch ( FileNotFoundException e) {
            System.out.println("file not found");
        }
    }


    //loading in and receiving the station buttons(nodes) from the BostonMetroView class
    public void loadMainButton() {
        BMV.loadDirectionButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int [] nodes = BMV.getNodes();

                if (nodes[0] ==0 ||  nodes[1] ==0 || ( nodes[0] == nodes[1]) ) {
                    if (nodes[0] ==0 ||  nodes[1] ==0){
                        BMV.errorOut.setVisible(true);
                        BMV.errorOut.setText(" Please enter both a From and To station.");

                    } else if (nodes[0] == nodes[1]){
                        BMV.errorOut.setVisible(true);
                        BMV.errorOut.setText(" Can't get directions between the same place!");
                    }

                } else {
                    BMV.clearError();
                    BMV.clearDirections();
                    pathFind(buildgraph(), nodes[0], nodes[1]);

                }
            }
        });
    }


    //Generating a weighted graph
    public int[][] buildgraph(){

        int[][] weightedGraph = new int[sizeOfGraph][sizeOfGraph];
        for( int i = 1; i< sizeOfGraph; i++){
            for (int j =0; j < graph.successors(i).size() ; j++){
                weightedGraph[i][graph.successors(i).get(j)] = graph.getWeight(i,graph.successors(i).get(j));
            }
        }
        return weightedGraph;

    }


    //finding the shortest path using the ID's of the stations
    public void pathFind(int[][] weightedGraph, int stationFrom, int stationTo) {

        if (stationFrom < 1 || stationTo <1  || stationTo > 125 || stationFrom > 125) {

            BMV.errorOut.setText(" Error: station is out of bounds");
            BMV.errorOut.setVisible(true);
            return;
        }

        int wgLength = sizeOfGraph;

        int[] smallestPathLen = new int[wgLength];
        boolean[] visited = new boolean[wgLength];


        for (int i = 0; i < wgLength;  i++) {

            visited[i] = false;
            smallestPathLen[i] = Integer.MAX_VALUE;

        }

        smallestPathLen[stationFrom] = 1;
        int[] paths = new int[wgLength];
        paths[stationFrom] = 1;


        for (int i = 1; i < wgLength; i++) {

            int nextStation = 1;
            int shortestDistance = Integer.MAX_VALUE;

            for (int j = 1; j < wgLength; j++) {

                if (!visited[j]) {
                    if (smallestPathLen[j] < shortestDistance) {

                        nextStation = j;
                        shortestDistance = smallestPathLen[j];
                    }
                }
            }

            visited[nextStation] = true;


            for (int e = 0; e < wgLength; e++)
            {
                int edgeDistance = weightedGraph[nextStation][e];

                if (edgeDistance > 0) {
                    if (((shortestDistance + edgeDistance) < smallestPathLen[e])) {

                        paths[e] = nextStation;
                        smallestPathLen[e] = shortestDistance + edgeDistance;
                    }
                }
            }
        }

        int temp = stationTo;

        List<Integer> reverseMe = new ArrayList<Integer>();
        if (stationTo == 1){
            temp =2;
        }

        while (temp != 1) {
            reverseMe.add(temp);
            temp = paths[temp];

        }
        if (stationFrom == 1){
            reverseMe.add(stationFrom);
        }

        Collections.reverse(reverseMe);

        if (stationTo == 1){
            reverseMe.add(1);
        }
        BostonMetroView bmv = getView();
        bmv.updateUserDirections(reverseMe);
    }

}


