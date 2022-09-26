import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class BostonMetroView {

    /*

    The view class is responsible with handling all things related to the GUI and how users interact with it.

     */

    // initialise all basic components for the GUI
    ImagePanel panel;
    JLabel stationFrom = new JLabel();
    JLabel stationTo = new JLabel();
    JLabel errorOut = new JLabel();

    JTextArea output = new JTextArea();

    JButton getDirection = new JButton();
    int node1,node2;
    HashMap<String, String> nameLookup = new HashMap<String, String>(); // declare a dictionary for easy Name Lookup

    // init gui
    public BostonMetroView() {

        // load the background image
        this.panel = new ImagePanel(new ImageIcon("resources/mapFinal.png").getImage());
        readIn();
        initOutput();

        //set up the frame which is the panel is added too
        JFrame frame = new JFrame();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    private void initOutput() {

        Border blackline = BorderFactory.createLineBorder(Color.black); // create a border
        Font  font  = new Font(Font.SANS_SERIF,  Font.BOLD, 10);

        // code block the left-hand text box, at the top of the page
        stationFrom.setBounds(5, 5, 200, 30);
        stationFrom.setText(" From: ");
        stationFrom.setBorder(blackline);
        stationFrom.setVisible(true);

        // code block the right-hand text box, at the top of the page
        stationTo.setBounds(205, 5, 200, 30);
        stationTo.setText(" To: ");
        stationTo.setBorder(blackline);
        stationTo.setVisible(true);

        // code block for error displaying, at the top of the page
        errorOut.setBounds(505, 5, 400, 30);
        errorOut.setText(" Error: ");
        errorOut.setBorder(blackline);
        errorOut.setVisible(false);


        // code for directions output, as output is long output needs to be scrollable
        JScrollPane scroll = new JScrollPane(output);

        output.setText(" Directions: ");
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        output.setEditable(false);
        output.setFont(font);

        scroll.setBounds(760, 520, 240, 180);
        scroll.setBackground(Color.white);
        scroll.setOpaque(true);
        scroll.setBorder(blackline);
        scroll.setVisible(true);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setVisible(true);

        // add all components to the panel
        panel.add(stationFrom);
        panel.add(stationTo);
        panel.add(errorOut);
        panel.add(scroll);

    }


    static class ImagePanel extends JPanel {

        private Image img;

        public ImagePanel(String img) {
            this(new ImageIcon(img).getImage());
        }

        // used to display the image as the panel's background
        public ImagePanel(Image img) {
            this.img = img;
            Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
            Dimension newSize = new Dimension(1024, 1024);
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(newSize);
            setSize(size);
            setLayout(null);
        }

        public void paintComponent(Graphics g) {
            g.drawImage(img, 0, 0, null);
        }

    }


    static class roundButton implements Border {

        int rad;

        // set the radius of the buttons used for the stations
        roundButton() {
            this.rad = 8;
        }

        public Insets getBorderInsets(Component component) {
            return new Insets(rad, rad, rad, rad);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            g.drawRoundRect(x, y, w, h, rad, rad);
        }

    }


    public void updateOutput(String newStationId) {

        // set the new station to 'from' as it is empty
        if (stationFrom.getText() == " From: ") {
            stationFrom.setText(" From: " + getName(newStationId));
            this.node1 = Integer.parseInt(newStationId);

        // set the new station to 'To' as 'from' is full but 'to' is empty
        } else if ((stationTo.getText() == " To: ")) {
            stationTo.setText(" To: " + getName(newStationId) );
            this.node2 = Integer.parseInt(newStationId);

        } else {
            // set the old to the old station to be 'from' and the new station to 'to'
            stationFrom.setText(" From: " + stationTo.getText().substring(5));
            this.node1 = node2;

            stationTo.setText(" To: " + getName(newStationId));
            this.node2 = Integer.parseInt(newStationId);
        }
    }


    public void updateUserDirections(List<Integer> stationList) {

        // add all stations to a the scrollable output box, with a newline for each station
        String myString = "Directions: \n" ;

        for (int x = 0; x< stationList.size() ; x++) {
            myString += getName(stationList.get(x).toString())  + " , \n " ;
        }

        output.setText(myString);

    }


    public void clearDirections() {
        output.setText(" Directions: "); // clear the directions box
    }


    public void clearError() {
        errorOut.setText(" Error: "); // clear the error box
        errorOut.setVisible(false); // hide the error box
    }


    public void addButton(int stationID, int x, int y) {

        // set up the panel and the button
        ImagePanel panel = getPanel();
        JButton button = new JButton("" + stationID);
        button.setText(""+ stationID);

        // add the action listen for what the button should do when it's clicked
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateOutput(e.getActionCommand());
            }
        });

        // various setup for the button
        button.setBounds(x, y, 8, 8);
        button.setBorder(new roundButton());
        button.setForeground(Color.black);
        button.setBorderPainted(true);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setVisible(true);
        button.revalidate();
        panel.add(button);

    }


    public ImagePanel getPanel() {
        return this.panel; // returns the panel
    }


    public void readIn() {
        String path = "resources/stationNames.csv"; // path of the file

        // try reading in the file
        try {
            Scanner sc = new Scanner(new File(path));
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                String[] splitStrings = s.split(",");

                nameLookup.put(splitStrings[0], splitStrings[1]); // add the id and the stations name to a dictionary
            }
        } catch (FileNotFoundException e) {

            System.out.println("file not found"); // print error if the file is not found
        }
    }


    public String getName(String station){
        return nameLookup.get(station); // return the stations name based on its ID using the dictionary
    }


    public void loadDirectionButton(ActionListener a){

        // general button setup
        Border blackline = BorderFactory.createLineBorder(Color.black);
        getDirection.setBounds(405,5, 100, 30);
        getDirection.setText("Get Directions!");
        getDirection.setBorder(blackline);
        getDirection.setVisible(true);

        getDirection.addActionListener(a); // use the passed in action listener
        panel.add(getDirection);
        getDirection.revalidate();

    }


    public int[] getNodes() {

        // return a list of numbers that represent the id of the To and From nodes
        int[] l = new int[]{0,0};
        l[0] = node1;
        l[1] = node2;
        return l;
    }


}
