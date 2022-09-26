import org.junit.jupiter.api.Test;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class modelTest {
    private BostonMetroModel bmm = new BostonMetroModel(10);
    private BostonMetroView bmv = new BostonMetroView();
    private BostonMetroController bmc = new BostonMetroController();

    /////////////////////////////////////////////
    // MODEL                                   //
    /////////////////////////////////////////////

    @Test
    public void noOfNodesTest() {
        assertEquals(bmm.noNodes , 10);
    }

    @Test
    public void NoOfEdgeTest() {
        assertEquals(bmm.noOfEdges() , 0);
        bmm.addEdge(1, 2, "test");
        bmm.addEdge(2, 3, "test");
        bmm.addEdge(3, 1, "test");
        assertEquals(bmm.noOfEdges() , 3);
    }

    @Test
    public void addEdgeTest() {
        assertTrue(bmm.addEdge(2,3, "Colour"));
        assertTrue(bmm.addEdge(1,3, "Colour"));
        assertTrue(bmm.addEdge(3,4, "Colour"));
        assertTrue(bmm.addEdge(3,6, "Colour"));
        assertTrue(bmm.addEdge(1,5, "Colour"));
        assertTrue(bmm.addEdge(5,2, "Colour"));
        assertTrue(bmm.addEdge(4,2, "Colour"));
        assertEquals(bmm.noOfEdges() , 7);
    }

    @Test
    public void isEdgeTest() {
        assertFalse(bmm.isEdge(1,2));
        bmm.addEdge(1,2, "Colour");
        assertTrue(bmm.isEdge(1,2));
    }

    @Test
    public void successorsTest() {
        bmm.addEdge(1,2, "Colour");
        ArrayList<Integer> accOut = new ArrayList<>();
        accOut.add(2);
        assertEquals(bmm.successors(1) ,accOut );
        bmm.addEdge(1,2, "Colour");
        bmm.addEdge(1,3, "Colour");
        bmm.addEdge(1,4, "Colour");
        bmm.addEdge(3,1, "Colour");
        accOut.add(3); accOut.add(4);
        assertEquals(bmm.successors(1) ,accOut );
    }

    @Test
    public  void getWeightTest() {
        bmm.addEdge(1,2, "Colour");
        assertEquals(bmm.getWeight(1,2),10 );// 10 is default for non colours
        bmm.addEdge(2,3, "Orange");
        assertEquals(bmm.getWeight(2,3),1 );
        bmm.addEdge(3,4, "orange");
        assertEquals(bmm.getWeight(3,4),10 );
        bmm.addEdge(4,5, "Mattapan");
        assertEquals(bmm.getWeight(4,5),8 );
        assertEquals(bmm.getWeight(4,100) , null);

    }


    /////////////////////////////////////////////
    // VIEW                                    //
    /////////////////////////////////////////////


    @Test
    public void testId() {
        assertEquals("NorthStation", bmv.getName("20"));
        assertEquals("Braintree" , bmv.getName("124"));
        assertEquals(null , bmv.getName("0")); //edge case
        assertEquals(null , bmv.getName("125")); //edge case
    }

    @Test
    public void emptyNode() {
        assertEquals(0, bmv.node1);
        assertEquals(0, bmv.node2);
    }

    @Test
    public void simpleButtonTest() {
        bmv.updateOutput("1");
        assertEquals(bmv.node1 , 1);
        assertEquals(bmv.stationFrom.getText() , " From: OakGrove");
    }

    @Test
    public void twoStationsEnteredTest() {
        bmv.updateOutput("1");
        bmv.updateOutput("2");
        assertEquals(bmv.stationFrom.getText() , " From: OakGrove");
        assertEquals(bmv.stationTo.getText() , " To: Malden");

    }

    @Test
    public void GuiDirectionButtonTests() {

        // ALSO TEST FOR loadDirectionButton

        bmv.loadDirectionButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int [] nodes = bmv.getNodes();

                if (nodes[0] ==0 ||  nodes[1] ==0 || ( nodes[0] == nodes[1]) ) {
                    if (nodes[0] ==0 ||  nodes[1] ==0){
                        bmv.errorOut.setVisible(true);
                        bmv.errorOut.setText(" Please enter both a To and From station location.");

                    } else if (nodes[0] == nodes[1]){
                        bmv.errorOut.setVisible(true);
                        bmv.errorOut.setText(" Can't get directions between the same place!");
                    }

                } else {
                    bmv.clearError();
                    bmv.clearDirections();


                }
            }
        });

        // TEST CASE FOR WHEN NO STATIONS ARE SELECTED AND THE BUTTON IS PRESSED

        bmv.getDirection.doClick();
        assertEquals(bmv.errorOut.getText() , " Please enter both a To and From station location.");

        // TEST CASE FOR WHEN ONLY ONE STATION IS SELECTED AND BUTTON IS PRESSED

        bmv.updateOutput("1");
        bmv.getDirection.doClick();
        assertEquals(bmv.errorOut.getText() , " Please enter both a To and From station location.");

        // TEST CASE FOR WHEN BOTH STATIONS ENTERED ARE THE SAME

        bmv.updateOutput("1");
        bmv.updateOutput("1");
        bmv.getDirection.doClick();

        assertEquals(bmv.errorOut.getText() , " Can't get directions between the same place!");

        bmv.updateOutput("1");
        bmv.updateOutput("2");
        bmv.getDirection.doClick();

        assertEquals(bmv.errorOut.getText() , " Error: ");
    }

    @Test
    public void ClearErrorTest() {
        bmv.errorOut.setText("Test value which is not empty");
        bmv.clearError();
        assertEquals(bmv.errorOut.getText() , " Error: ");
    }

    @Test
    public void ClearDirectionTest() {
        bmv.output.setText("Test value which is not empty");
        bmv.clearDirections();
        assertEquals(bmv.output.getText() , " Directions: ");
    }

    @Test
    public void getNodesTest() {
        bmv.node1 = 1;
        bmv.node2 = 2;

        assertEquals(bmv.getNodes()[0], 1 );
        assertEquals(bmv.getNodes()[1], 2 );

    }

    @Test
    public void addButtonTest() {
        bmv.addButton(0, 100 ,100);
        assertEquals(bmv.panel.getComponent(4).getBounds().toString() ,
                "java.awt.Rectangle[x=100,y=100,width=8,height=8]");
        // 3 components are prebuilt, so this has ID 4
    }

    @Test
    public void readInTest() {
        bmv.readIn();
        assertEquals( bmv.nameLookup.get("1") , "OakGrove");
        assertEquals( bmv.nameLookup.get("124") , "Braintree");
        assertEquals( bmv.nameLookup.get("125") , null);
        // If test passes read in must have worked
    }

    @Test
    public void getPanelTest() {

        assertEquals(bmv.getPanel() , bmv.panel);
    }

    @Test
    public void initGuiTest() {
        assertEquals(bmv.panel.getComponent(0).getBounds().toString() ,
                "java.awt.Rectangle[x=5,y=5,width=200,height=30]");
        assertEquals(bmv.panel.getComponent(1).getBounds().toString() ,
                "java.awt.Rectangle[x=205,y=5,width=200,height=30]");
        assertEquals(bmv.panel.getComponent(2).getBounds().toString() ,
                "java.awt.Rectangle[x=505,y=5,width=400,height=30]");
        assertEquals(bmv.panel.getComponent(3).getBounds().toString() ,
                "java.awt.Rectangle[x=760,y=520,width=240,height=180]");
    }


    /////////////////////////////////////////////
    // Controller                              //
    /////////////////////////////////////////////


    @Test
    public void DataReadTest() {
        bmc.readIn();
        assertTrue(bmc.graph.isEdge(1,2));
        assertTrue(bmc.graph.isEdge(123,124));
    }

    @Test
    public void loadCordsTest() {
        bmc.loadCords();
        assertEquals(bmc.getView().getPanel().getComponent(128).getBounds().toString() ,
                "java.awt.Rectangle[x=836,y=836,width=8,height=8]");
        //128 represents station 124, just 4 default components
        // other testing can be seen in the manual testing section of report
    }

    @Test
    public void loadMainButtonTest() {
        bmc.loadMainButton();
        assertEquals(bmc.getView().getPanel().getComponent(5).getBounds().toString() ,
                "java.awt.Rectangle[x=655,y=93,width=8,height=8]");
        // other testing can be seen in the manual testing section of report
    }

    @Test
    public void buildGraphTest() {
        bmc.readIn();
        int [][] test = bmc.buildgraph();
        assertEquals(test[1][2]  ,1);
        assertEquals(test[123][124]  ,7);
        assertEquals(test[1][1]  ,0);
        assertEquals(test[0][1]  ,0);

    }

    @Test
    public void PathFindTest() {

        bmc.pathFind(bmc.buildgraph() , 1, 5); // simple path find
        assertEquals(bmc.getView().output.getText()  , "Directions: \n" + "OakGrove , \n" + " Malden , \n" + " Wellington , \n" + " ");

        bmc.pathFind(bmc.buildgraph() , 0, 5); //lower bound
        assertEquals(bmc.getView().errorOut.getText()  , " Error: station is out of bounds");

        bmc.pathFind(bmc.buildgraph() , 0, 126); //upper bound
        assertEquals(bmc.getView().errorOut.getText()  , " Error: station is out of bounds");

        bmc.pathFind(bmc.buildgraph() , 1, 124); // simple path find
        assertEquals(bmc.getView().output.getText()  , "Directions: \n" + "OakGrove , \n" + " Malden , \n" + " Wellington , \n" + " SullivanSquare , \n" +
                " CommunityCollege , \n" + " NorthStation , \n" + " Haymarket , \n" + " State , \n" + " DowntownCrossing , \n" + " SouthStation , \n" + " Broadway , \n" +
                " Andrew , \n" + " JFK/UMass , \n" + " NorthQuincy , \n" + " Wollaston , \n" + " QuincyCenter , \n" + " QuincyAdams , \n" + " Braintree , \n" + " ");
    }

}
