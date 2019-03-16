/**
 * Represents a traffic light. 
 */
public class Light {
  
  private int time;
  private int period;
  private int green; 
  /**
   * Constructs and initializes a light.
   * @param period the total period
   * @param green the number of time steps the signal is green
   */
  public Light(int period, int green) {
  this.period = period;
  this.green = green;
  }
  
  /**
   * Advances the internal clock.
   */
  public void step() {
    this.time++;
    if (this.time == this.period) {
      this.time = 0;
    }
  }
  
  
  /**
   * Checks if the light is green.
   * @return true if the light is green else false
   */
  public boolean isGreen() {
    return this.time < this.green;
  }
  
  /**
   * Returns a string representation of the signal. 
   * The representation indicates if the signal is green or red. 
   * @return a string representation
   */
  public String toString() {
    if (this.isGreen()) {
    return "G";
    }else {
      return "R";
    }
  }
  
  /**
   * Demonstrates the stepping of a signal.
   */
  public static void main(String[] args) {
    Light light = new Light(5,2);
    for (int i=0; i<15; i++) {
      System.out.format("%2d : %5s\n", i, light);
      light.step();
    }
  } 
}

