/**
 * Represents a series of measurements stored in an array
 */
public class Measurements {
  
  private int nvalues;
  private double[] theArray;
  
  /**
   * Constructs an empty array with a specified size
   * @param max maximum index (size of array)
   */
  public Measurements(int max) {
    this.theArray = new double[max];
  }
  
  public String toString() {
    double copy[] = new double [this.nvalues];
    for (int i = 0; i < this.nvalues; i++) {
      copy[i] = this.theArray[i];
    }
    return java.util.Arrays.toString(copy);
  }                                       
  
  
  /**
   * Adds a specified value to the array at the next position, 
   * if the number of values added exceeds the original size
   * of the array, the array length is doubled and the old values 
   * plus the new one are added.
   * @param value desired value to be added
   */
  public void add(double value) {
    this.nvalues++;
    if (this.nvalues > this.theArray.length) {
      double copy[] = new double [2*this.theArray.length];
      for (int i = 0; i < this.nvalues-1; i++) {
        copy[i] = this.theArray[i];
      }
      theArray = copy;
    }
    this.theArray[this.nvalues - 1] = value;
  }
  
  /**
   * Returns the number of values stored
   */
  public int stored() {
    return this.nvalues;
  }
  
  /**
   * Returns value at specified index
   * @param index index of desired value
   */
  public double get(int index) {
    return this.theArray[index];
  }
  
  /**
   * Returns the mean of the stored values
   */
  public double mean(){
    double sum = 0;
    for(int i = 0; i < this.nvalues; i++) {
      sum = sum + this.theArray[i];
    }
    return sum/this.nvalues;
  }
  
  /**
   * returns the smallest value of the array
   */
  public double min() {
    double min = this.theArray[0];
    for(int i = 0; i < this.nvalues; i++) {
      min = Math.min(min,this.theArray[i]);
    }
    return min;
  }
  
  /**
   * reurns the greatest value of the array
   */
  public double max() {
    double max = this.theArray[0];
    for(int i = 0; i < this.nvalues; i++) {
      max = Math.max(max,this.theArray[i]);
    }
    return max;
  }
  
  /**
   * returns the standard deviation of the values in the array
   */
  public double stdDev() {
    double sqdiff = 0;
    double mean = this.mean();
    for (int i = 0; i < nvalues; i++) {
      sqdiff = sqdiff + Math.pow(this.theArray[i] - mean, 2);
    }
    return Math.sqrt(sqdiff/this.nvalues);
  }
  
  /**
   * Constructs an array of length and values of
   * specified array
   * @param values desired array of values
   */
  public Measurements(double[] values) {
    this.theArray = new double[values.length];
    for(int i = 0; i < values.length; i++) {
      this.add(values[i]);
    }
  }
  
  /**
   * Returns the inserted values of the array
   */
  public double[] get() {
    double copy[] = new double[this.nvalues];
    for (int i = 0; i < this.nvalues; i++) {
      copy[i] = this.theArray[i];
    }
    return copy;
  }
  
  /**
   * Constructs a new array based on the present one, with values on 
   * index i is the mean of the values at i-1, i and i+1.
   * Values in the first and last place remains the same
   */
  public Measurements smooth() {
    Measurements smooth = new Measurements (this.nvalues);
    smooth.add(this.theArray[0]);
    for (int i = 1; i < this.nvalues-1; i++) {
      smooth.add((this.theArray[i-1] + this.theArray[i] + this.theArray[i+1])/3);
    }
    smooth.add(this.theArray[this.nvalues-1]);
    return smooth;
  }
}
