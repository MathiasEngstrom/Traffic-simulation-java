import java.io.*;
import java.util.Locale;
import java.util.Scanner;

/**
 * Main class for running a simulation
 * 
 * This class does not need to be changed
 */

public class Simulation {
  
  private static int time = 0;
  
  private static boolean simulationRun = false;
  
  /** 
   * Returns current simulation time
   * @return current time step
   */
  public static int getTime() {
    return time;
  }
  
  /**
   * Sets the global time,
   * <p>
   * <b>Note:</b> This method should ONLY be used by JUnit test programs
   * @param time new value for global time
   * @throws RuntimeException if called during simulation runs
   */
  public static void setTime(int time) {
    if (simulationRun) {
      throw new RuntimeException("setTime may not be called in during simulation");
    }
    Simulation.time = time;
  }
  
  public static void main(String [] args) 
    throws IOException
  {
    simulationRun = true;
    Scanner sc = new Scanner(System.in);
    Locale.setDefault(Locale.US);
    TrafficSystem tf = new TrafficSystem();
    tf.printSetup();
 
    System.out.print("Press return to start simulation");
    sc.nextLine();
    try {
      while (true) {
        for (int i=0; i<100; i++) {
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) { }
          time++;
          tf.step();
          tf.print();
          System.out.println("-----------------------------------");
        }
        tf.printStatistics();
        System.out.print("Continue (y/n)? ");
        String answer = sc.nextLine();
        if (answer.equals("n"))
          break;
      }
    } catch (RuntimeException re) {  // Take care of occured exceptions
      System.out.println("******* " + re.getMessage());
      re.printStackTrace();
      System.out.println();
      tf.printStatistics();
    }
  }
}




