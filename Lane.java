/**
 * Represents a lane as an array of Vehicles. A position in the lane may be occupied
 * by a vehicle or free (contains null). Vehicles enter the lane at the high index of 
 * the array end exit from index 0.
 */
public class Lane {

  private Vehicle[] theLane;

    /**
     * Constructs a lane with a specified capacity.
     * @param length The length (capacity) of the lane in number of vehicles
     */
    public Lane(int length) {
    this.theLane = new Vehicle[length];
    }

    /**
     * A string representation of the lane.
     * @return The string representation
     */
    public String toString() {
      String text = "";
      for (int i = 0; i < this.theLane.length; i++){
        if (this.theLane[i] == null) {
        text = text + " ";
        }else {
        text = text + this.theLane[i];
        }
      }
      return "<" + text + ">";
    }

    /**
     * Advances all except the first vehicle one position provided the 
     * target position is free. The process starts in the low end (i. e.
     * at index 1).
     * <p>
     * <b>Example:</b> The follwing two lines shows the result of
     * a call to <code>toString</code> before and after a call to 
     * <code>step()</code>
     * <pre>
     *    &lt;XX  X   X X X  XX&gt;
     *    &lt;XX X   X X X  XX &gt;
     * </pre>
     * 
     */
    public void step() {
      for (int i = 1; i < this.theLane.length; i++) {
        if (this.theLane[i-1] == null) {
        this.theLane[i-1] = this.theLane[i];
        this.theLane[i] = null;
        }
      }
    }

    /**
     * Removes the first vehicle (index 0) from the lane and makes it empty.
     * @return The removed vehicle or <code>null</code> if the position was empty
     */ 
    public Vehicle removeFirst() {
      Vehicle firstVehicle = this.theLane[0];
      this.theLane[0] = null;
      if (firstVehicle == null) {
      return null;
      }else {
        return firstVehicle;
      }
    }

    /**
     * Access method for the vehicle in the first position.
     * @return A reference to the vehicle in the first position
     */
    public Vehicle getFirst() {
      return this.theLane[0];
    }

    /**
     * Checks if the last position is free.
     * @return <code>true</code> if the last position is free (null) 
     * else <code>false<code>.
     */
    public boolean lastFree() {
      return this.theLane[this.theLane.length - 1] == null;
    }

    /**
     * Put a vehicle in the last position.
     * @param v Vehicle to be put in the last position
     * @throws RuntimeException if the last position is not free
     */
    public void putLast(Vehicle v) {
      if (this.theLane[this.theLane.length - 1] != null) {
        throw new RuntimeException("Last position of the lane is not free");
      }
      this.theLane[this.theLane.length - 1] = v;
    }
  
  
    /**
     * Counts the number of Vehicles on the lane.
     * @return The number of Vehicles 
     */
    public int numberOfVehicles() {
      int nVehicles = 0;
      for (int i = 0; i < this.theLane.length; i++) {
        if (this.theLane[i] != null){
          nVehicles++;
        }
      }
      return nVehicles;
    }

    /**
     * Demonstrates the use of the the class and it's methods.
     */
    public static void main(String[] args) { 
      Lane aLane = new Lane(10);
      aLane.putLast(new Vehicle('a'));
      System.out.println(aLane);
      for (int i=0; i<10; i++) {
        aLane.step();
        aLane.step();
        aLane.putLast(new Vehicle('a'));
        System.out.println(aLane);
      }
    }
}

