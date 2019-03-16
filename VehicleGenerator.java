import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/**
 * Generates vehicles with varying probability.
 */

public class VehicleGenerator {
  private ArrayList<Integer> periods = new ArrayList<Integer>();;
  private ArrayList<Double> arrivalProbabilities = new ArrayList<Double>();
  private ArrayList<Double> turningProbabilities = new ArrayList<Double>();
  private ArrayList<String> comments = new ArrayList<String>();
  
  private int time;       // internal clock
  private int period;     // current period
  private int totalCycle; // total time period
  
  /**
   * Hard coded probabilities for test purposes only.
   * The used probabilities will most certainly produce traffic jam.
   */
  public VehicleGenerator() {
    
    periods.add(30);         // Time step 0 - 29
    arrivalProbabilities.add(0.7);
    turningProbabilities.add(0.5); 
    comments.add("First interval");
    
    periods.add(30 + 20);    // Time step 30 - 49
    arrivalProbabilities.add(0.8);
    turningProbabilities.add(0.7);
    comments.add("Second intervall");
    
    totalCycle = periods.get(periods.size()-1);
    time = 0;
    period = 0;
  }
  
  /**
   * This constructor reads the arrival and turning probabilities for different time 
   * periods from a file.
   * <p>
   * Each line of input defines the probabilities for a specific period with the 
   * following contents <br>
   * <ol>
   *   <li> Number of time steps (int).</li>
   *   <li> Arrival probability (double) [0, 1].</li>
   *   <li> Turning probability (double) [0, 1].</li>
   *   <li> Optional comment. Ignored. </li>
   * </ol>
   * <p>
   * <b>Example:</b> The following five lines defines five periods of 
   * length 100, 20, 60, 30 and 50, respectively with
   * different arrival and turning probabilities.
   *
   * <pre>
   *      100   0.2   0.3   Night
   *       20   0.8   0.8   Morning rush rush
   *       60   0.5   0.5   Day
   *       30   0.7   0.6   Afternoon rush
   *       50   0.8   0.4   Evening
   * </pre>
   *
   * @param filename name of file with probabilities (US conventions)
   */
  public VehicleGenerator(String filename) 
  {
    Scanner fscan = null;
    try {
      fscan = new Scanner(new FileReader(filename));
    } catch (FileNotFoundException e) {
      System.out.println(e.toString());
      System.exit(0);
    }
    int limit = 0;
    while (fscan.hasNext()) {
      Scanner lineScanner = new Scanner(fscan.nextLine());
      lineScanner.useLocale(Locale.US);
      limit = limit + lineScanner.nextInt();
      periods.add(limit);   
      arrivalProbabilities.add(lineScanner.nextDouble());
      turningProbabilities.add(lineScanner.nextDouble()); 
      comments.add(lineScanner.nextLine());
    }
    totalCycle = periods.get(periods.size()-1);
    time = 0;
    period = 0;
  }
  
  /**
   * Generate a vehicle according to the probabilities for this time step
   * @return A vehicle or null
   */
  public Vehicle step() {
    time = time+1;
    if (time >= totalCycle) {
      time = 0;
      period = 0;
    } 
    
    if (time>=periods.get(period)) {
      period++;
    }
    
    Vehicle retur = null;
    double prob = arrivalProbabilities.get(period);
    
    if (Math.random() < prob) {
      if (Math.random() < turningProbabilities.get(period)) {
        retur = new Vehicle('S');
      } else {
        retur = new Vehicle('W');
      }
    }
    return retur;
  }
  
  /**
   * Returns the current state
   */
  public String toString() {
    return String.format(Locale.US, "%4d: <%1d, %.2f, %.2f>",
                         time, period, 
                         arrivalProbabilities.get(period),
                         turningProbabilities.get(period));  
  }
  
  /**
   * Prints the current setup of the generator
   */
  public void print() {
    int nsteps = 0;
    for (int i= 0; i<periods.size(); i++) {
      System.out.format(Locale.US, "\t%4d, %4.2f, %4.2f  %s\n", 
                       periods.get(i) - nsteps, 
                       arrivalProbabilities.get(i),
                       turningProbabilities.get(i),
                       comments.get(i)); 
      nsteps = periods.get(i);
    }
  }
  
  /**
   * Demonstration program for the VehicleGenerator
   */
  public static void main(String[] args) {
    VehicleGenerator vg = new VehicleGenerator("probabilities.txt");
    //VehicleGenerator vg = new VehicleGenerator();
    System.out.println("VehicleGenerator setup:");
    vg.print();
    System.out.println("\nStepping the generator:");
    for (int i= 0; i<300; i++) {
      System.out.print(vg);
      Vehicle v = vg.step();
      if (v!=null) {
        System.out.print("  Vehicle out: <" + 
                         v.getDestination() + ", " + v.getTime() + ">" );
      }
      System.out.println();
    }
    
  }
}