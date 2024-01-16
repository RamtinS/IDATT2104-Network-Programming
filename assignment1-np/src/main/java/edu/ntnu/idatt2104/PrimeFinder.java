package edu.ntnu.idatt2104;

import java.util.List;

/**
 * The PrimeFinder class extends the Thread class and is designed to find prime numbers
 * within a specified range and store them in a provided List.
 *
 * @author Ramtin Samavat
 * @version 1.0
 * @since Jan 09, 2024
 */
public class PrimeFinder extends Thread {
  private final int start;
  private final int end;
  private final List<Integer> primeNumbers;

  /**
   * Constructs a PrimeFinder object with the specified start and end values for the
   * range of numbers to search and a list to store the prime numbers found.
   *
   * @param start the starting value of the range.
   * @param end the ending value of the range.
   * @param primeNumbers the list to store the prime numbers found.
   */
  public PrimeFinder(int start, int end, List<Integer> primeNumbers) {
    this.start = start;
    this.end = end;
    this.primeNumbers = primeNumbers;
  }

  /**
   * Checks if a given number is a prime number.
   *
   * @param number the number to be checked.
   * @return true if the number is prime, false otherwise
   */
  private boolean isPrime(int number) {
    if (number <= 1) {
      return false;
    }
    for (int i = 2; i <= number/2; i++) {
      if(number % i == 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Overrides the run method of the Thread class to find and add prime numbers
   * within the specified range to the List.
   */
  @Override
  public void run() {
    for (int i = start; i <= end; i++) {
      if (isPrime(i)) {
        primeNumbers.add(i);
      }
    }
  }
}
