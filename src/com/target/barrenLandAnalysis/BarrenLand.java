package com.target.barrenLandAnalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.*;

public class BarrenLand {

    private int DEFAULT_FARM_LENGTH = 400;
    private int DEFAULT_FARM_BREADTH = 600;
    private int farmLength;
    private int farmBreadth;
    private HashMap<Integer, Integer> sectionMap = new HashMap<Integer, Integer>();

    int Farm[][];

    LinkedList<Integer[]> allBarrenLand = new LinkedList<Integer[]>();
    Queue<Integer[]> queue = new LinkedList<Integer[]>();

    /**
     * Default constructor to initialize the farm.
     * Sets the length and breadth of the farm to the default values.
     * Allocates memory of the default size to the farm.
     * We also initialize the farm once we have the size.
     */
    public BarrenLand() {
        this.farmLength = DEFAULT_FARM_LENGTH;
        this.farmBreadth = DEFAULT_FARM_BREADTH;
        Farm = new int[this.farmLength][this.farmBreadth];
        initializeFarm();
    }

    /**
     * Parametrized constructor to create custom sized Farm as needed.
     * It has checks to ensure that the farm created is logical.
     * We also initialize the farm once we have the size.
     * @param length This parameter is used to set the farm's length
     * @param breadth This parameter is used to set the farm's breadth
     */
    public BarrenLand(int length, int breadth) {
        if(length < 0 || breadth < 0){
            throw new InvalidParameterException("Cannot initialize farm with negative sizes");
        }

        if(length == 0 || breadth == 0) {
            throw new InvalidParameterException("Length or Breadth of farm cannot be 0");
        }

        this.farmLength = length;
        this.farmBreadth = breadth;
        Farm = new int[this.farmLength][this.farmBreadth];
        initializeFarm();
    }

    /**
     * This method initializes the farm with all sections as fertile.
     * We indicate fertility by setting all the nodes (x,y coordinates)
     * to 0.
     */
    private void initializeFarm() {
        for(int i = 0; i< farmLength; i++){
            for(int j = 0; j < farmBreadth; j++){
                Farm[i][j] = 0;
            }
        }
    }

    /**
     * This method will change all the nodes (x,y coordinates) to 0 for
     * those nodes which are barren.
     * To do this we iterate through the barren area coordinates provided
     * as input and then for each node, we set the value to -1 indicating
     * that node is barren.
     */
    public void fillBarrenArea() {
        ListIterator<Integer[]> iterator = allBarrenLand.listIterator();
        while(iterator.hasNext()){

            Integer[] barrenLand = iterator.next();

            for(int i = barrenLand[0]; i<= barrenLand[2]; i++){
                for(int j = barrenLand[1]; j <= barrenLand[3]; j++){
                    Farm[i][j] = -1;
                }
            }

        }
    }

    /**
     * This method is a helper method to visualize the farm at any
     * given point of time.
     */
    public void printFarm() {
        for (int j = 0; j < farmBreadth; j++) {
            String output = "";
            for (int i = 0; i < farmLength; i++) {
                output += " " + Farm[i][j];
            }
            System.out.println(output);
        }
    }

    /**
     * This method adds a node to Queue for inspection. It does so only
     * if the node passed in is a fertile node.
     * @param x x coordinate of the node
     * @param y y coordinate of the node
     */
    private void addNodeToQueue(int x, int y){
        //Add the node to the queue only if it is not barren.

        if (Farm[x][y] == 0){
            queue.add(new Integer[] {x,y});
        }
    }

    /**
     * This method adds the surrounding neighbors of the passed node
     * to the queue. It has checks to see if the neighbors are in the
     * bounds of the Farm.
     * @param node This is first param to addNeighborsToQueue method
     */
    private void addNeighborsToQueue(Integer [] node) {
        int x = node[0];
        int y = node[1];

        //Get left node
        if (x > 0){
            addNodeToQueue(x-1, y);
        }

        //Get right node
        if (x < farmLength - 1){
            addNodeToQueue(x+1, y);
        }

        //Get bottom node
        if (y > 0){
            addNodeToQueue(x, y-1);
        }

        //Get top node
        if (y < farmBreadth - 1){
            addNodeToQueue(x, y+1);
        }

    }

    /**
     * Method to check if a node was visited. If yes then returns True.
     * @param x x coordinate of the node
     * @param y y coordinate of the node
     * @return True if node is visited, False otherwise
     */
    public boolean visitedNode(int x, int y){
        return Farm[x][y] != 0;
    }

    /**
     * This is essentially Breadth First Search traversal through a disconnected
     * graph. The idea here is to go through the entire farm and touch each node.
     * At each node check if queue is empty or full. Then add a nodes neighbors
     * to the queue if it is part of fertile land while marking the node as visited.
     * The area will also be incremented for each fertile node.
     */
    public void processFertileLand() {
        int fertileLandId = 0;
        int i = 0;
        int j = 0;

        // Traverse whole farm
        while(i < farmLength && j < farmBreadth) {

            Integer node[];

            // If empty, it means start of new section.
            // Initialize area to 0 for this section and add node to queue.
            if (queue.isEmpty()) {
                node = new Integer[]{i, j};
                if (!visitedNode(i,j)) {
                    fertileLandId++;
                    sectionMap.put(fertileLandId,0);
                    queue.add(node);
                }

                // Ensure we are going to each node
                if (i == farmLength - 1) {
                    i = 0;
                    j++;
                } else {
                    i++;
                }
            }
            // Pop the node. Check if visited. Add neighbors to the queue.
            // Increment area as well.
            else {
                node = queue.poll();
                int x = node[0];
                int y = node[1];

                if (!visitedNode(x,y)) {
                    addNeighborsToQueue(new Integer[] {x,y});

                    Farm[x][y] = fertileLandId;
                    sectionMap.put(fertileLandId, (sectionMap.get(fertileLandId) + 1));

                }
            }
        }

    }

    /**
     * Return the output sorted by area in form of String.
     * If there is no fertile land, return 0.
     * Else sort the HashMap and return the sorted values
     * using String
     * @return String containing sorted areas
     */
    public String printOutput(){

        // If all the area is barren
        if (sectionMap.isEmpty()) {
            return "0";
        }
        // Get all the areas from the section Map and return sorted values
        // in form of String.
        else {
            ArrayList<Integer> sortedSectionMap = new ArrayList<Integer>();
            for (Map.Entry<Integer, Integer> entry : sectionMap.entrySet()) {
                sortedSectionMap.add(entry.getValue());
            }
            Collections.sort(sortedSectionMap);
            return (sortedSectionMap.toString()).replaceAll("\\[|\\]|,", "");
        }
    }

    /**
     * This method is added for the code to read input from STDIN.
     * The method uses BufferedReader and passes the input to the
     * input formatter as needed by the code.
     * @exception IOException on input error.
     * @see IOException
     */
    public void readBarrenLandFromSTDIN() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        readBarrenLandsInput(s);
    }

    /**
     * This method gathers all the coordinates and sets up the list of
     * areas which are barren.
     * Throws appropriate exception if inout is not correct.
     * @param input passed in from STDIN
     */
    public void readBarrenLandsInput(String input) {

        //Split on ','
        String[] areas = input.split(",");

        //Now remove unnecessary characters from each set of coordinates
        for(String a:areas){
            a = a.replace("\"", "");
            a = a.replaceAll("\\{|\\}", "");
            a = a.replaceAll("^ ", "");

            //If not Empty then set the coordinates of the barren area in a array
            //and store it in the Linkedlist.
            if(!a.isEmpty()){
                String[] coordinate = a.split(" ");
                try{

                    Integer[] barrenArea = {Integer.parseInt(coordinate[0]),
                            Integer.parseInt(coordinate[1]),
                            Integer.parseInt(coordinate[2]),
                            Integer.parseInt(coordinate[3])};

                    allBarrenLand.add(barrenArea);
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Invalid input for Barren Areas");
                } catch (Exception e) {
                    throw new RuntimeException("Exception occurred parsing input");
                }
            }
        }
    }

}
