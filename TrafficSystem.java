import java.util.Properties;
import java.io.*;
import java.util.ArrayList;
import java.text.DecimalFormat;
/**
 * Defines the components and behaviour of s specific traffic system
 */
public class TrafficSystem {
  // These instance variables are set by a call to the given loadProperties method.
  // Use the given file "properties.txt" as parameter.
  private int laneLength;      // Length of the first lane
  private int laneWSLength;    // Length of the lanes in front of the signals
  private int lightPeriod;     // Period for the signals
  private int lightWestGreen;  // Green period for the westbound light
  private int lightSouthGreen; // Green period for the southbaoun light
  
  // Instance variable for the different lanes, lights and the queue
  private Lane lane;
  private VehicleGenerator vg;
  private ArrayList<Vehicle> queue;
  private Lane laneWest;
  private Lane laneSouth;
  private Light lightWest;
  private Light lightSouth;
  // Instance variables for statistics
  private Measurements exitWest;
  private Measurements exitSouth;
  private int queueTimeWS;
  private int queueTimeQueue;
  
  // Constructor
  
  public TrafficSystem() {
    loadProperties("properties.txt");
    lane = new Lane(laneLength);
    vg = new VehicleGenerator("probabilities.txt");
    queue = new ArrayList<Vehicle>();
    laneWest = new Lane(laneWSLength);
    laneSouth = new Lane(laneWSLength);
    lightWest = new Light(lightPeriod, lightWestGreen);
    lightSouth = new Light(lightPeriod, lightSouthGreen);
    exitWest = new Measurements(1);
    exitSouth = new Measurements(1);
  }
  
  
  // Methods
  
  /**
   * Advances the whole traffic system one timestep. Makes use
   * of components step methods
   */
  public void step() {
    if (lightWest.isGreen()) {
      if (laneWest.getFirst() != null) {
        exitWest.add(Simulation.getTime() - laneWest.removeFirst().getTime());
      }
    }
    if (lightSouth.isGreen()) {
      if (laneSouth.getFirst() != null) {
        exitSouth.add(Simulation.getTime() - laneSouth.removeFirst().getTime());
      }
    }
    laneWest.step();
    laneSouth.step();
    boolean blockWS = true;
    if (lane.getFirst() != null) {
      if (lane.getFirst().toString().equals("W") && laneWest.lastFree()) {
        laneWest.putLast(lane.removeFirst());
        blockWS = false;
      }
    }
    if (lane.getFirst() != null) {
      if (lane.getFirst().toString().equals("S") && laneSouth.lastFree()) {
        laneSouth.putLast(lane.removeFirst());
        blockWS = false;
      }
    }
    if (blockWS && lane.getFirst() != null) {
      queueTimeWS++;
    }
    lane.step();
    Vehicle v = vg.step();
    if (v!=null) {
      queue.add(v);
    }
    if (queue.size() > 0 && lane.lastFree()) {
      lane.putLast(queue.remove(0));
    }
    if (queue.size() > 0) {
      queueTimeQueue++;
    }
    lightWest.step();
    lightSouth.step();
  }
  
  /**
   * Compute the number of vehicles in the system
   * @return The number of vehicles in the system
   */ 
  public int numberInSystem() {
    int numberInSystem = laneWest.numberOfVehicles() + laneSouth.numberOfVehicles() + lane.numberOfVehicles() + queue.size();
//    for (int i = 0; i < queue.size(); i++) {
//      if (queue.get(i) != null) {
//        numberInSystem++;
//      }
//    }
    return numberInSystem;
  }
  
  
  /**
   * Prints currently collected statistics
   */  
  public void printStatistics() {
    DecimalFormat numberFormat = new DecimalFormat("0.0");
    System.out.println("Statistics after " + Simulation.getTime() + " timesteps:");
    System.out.println("Number of arrived: " + "\t" + (numberInSystem() + exitWest.stored() + exitSouth.stored()));
    System.out.println("Number of left: " + "\t" + (exitWest.stored() + exitSouth.stored()));
    System.out.println("Number in the system:" + numberInSystem());
    
    System.out.println("Exit west");
    System.out.println("\t" + "Number:" + "\t" + exitWest.stored());
    System.out.println("\t" + "Mean:" + "\t" + numberFormat.format(exitWest.mean()));
    System.out.println("\t" + "Min:" + "\t" + exitWest.min());
    System.out.println("\t" + "Max:" + "\t" + exitWest.max() + "\n");
    
    System.out.println("Exit South");                   
    System.out.println("\t" + "Number:" + "\t" + exitSouth.stored());
    System.out.println("\t" + "Mean:" + "\t" + numberFormat.format(exitSouth.mean()));
    System.out.println("\t" + "Min:" + "\t" + exitSouth.min());
    System.out.println("\t" + "Max:" + "\t" + exitSouth.max() + "\n");
    
    System.out.println("Percent time step with block:" + "\t" + 
                       (numberFormat.format(((double)queueTimeWS/Simulation.getTime())*100)));
    System.out.println("Percent time step with queue:" + "\t" + 
                       (numberFormat.format(((double)queueTimeQueue/Simulation.getTime())*100)));

    
  }
  
  
  /**
   * Prints the current situation using toString-methods in 
   * lights and lanes
   */
  public void print() {
    System.out.print("(" + lightWest + ")" + laneWest);
    System.out.print(lane);
    System.out.println(queue);
    System.out.println("(" + lightSouth + ")" + laneSouth);
    //System.out.println("Number of vehicles in system: " + numberInSystem());
    //System.out.println();
  }
  
  
  /**
   * Prints the simulation parameters and arrival probabilities used in 
   * this run
   */
  public void printSetup() {
    System.out.println("\t" + " Simulation parameters:");
    System.out.println("\t" + " laneLength :"+ "\t" + laneLength);
    System.out.println("\t" + " laneWSlength:" + "\t" + laneWSLength);
    System.out.println("\t" + " lightPeriod:" + "\t" + lightPeriod);
    System.out.println("\t" + " lightSouthGreen:" + "\t" + lightSouthGreen);
    System.out.println("\t" + " lightWestGreen:" + "\t" +lightWestGreen);
    
    System.out.println();
    System.out.println("\t" + " Traffic periods and probabilities:");
    vg.print();
    System.out.println();
  }
  
  
  /** 
   * Reads the lane lengths and the properties of the lights from a 
   * property file
   * 
   * @param filename The file containing the properties
   * * 
   * The the property file should define 
   * <ul>
   * <li> length of the first lane</li>
   * <li> length of the lanes in front of the traffic lights</li>
   * <li> traffic light period (same for both lights)</li>
   * <li> green light period for each of the lights</li> 
   * </ul>
   * <p>
   * <b>Example of file contents:</b>
   * <pre>
   *    laneLength      : 10
   *    laneWSLength    :  8
   *    lightPeriod     : 14
   *    lightWestGreen  :  6
   *    lightSouthGreen :  4
   * </pre>
   * 
   */
  public void loadProperties(String filename) {
    Properties prop = new Properties();
    try {
      prop.load(new FileReader(filename));
      laneLength = Integer.parseInt(prop.getProperty("laneLength"));
      laneWSLength = Integer.parseInt(prop.getProperty("laneWSLength"));      
      lightPeriod = Integer.parseInt(prop.getProperty("lightPeriod"));
      lightWestGreen = Integer.parseInt(prop.getProperty("lightWestGreen"));
      lightSouthGreen = Integer.parseInt(prop.getProperty("lightSouthGreen"));     
    } catch (IOException ioe) {
      System.out.println("*** File " + filename + " could not be loaded");
      System.exit(0);
    }
  }
}