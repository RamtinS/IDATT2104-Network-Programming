package edu.ntnu.idatt2104;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The PrimeNumberFinder class is responsible for finding prime numbers within
 * a specified range using a multithreaded approach. It divides the given range
 * into several threads and utilizes the PrimeFinder class to concurrently search
 * for prime numbers in each subrange.
 *
 * @author Ramtin Samavat
 * @version 1.0
 * @since Jan 09, 2024
 */
public class PrimeNumberFinder {

  /**
   * The main entry point for lunching the algorithm for finding prime numbers.
   *
   * @param args An array of command-line arguments.
   */
  public static void main(String[] args) {
    int startNumber = 10;
    int endNumber = 50;

    List<Integer> primNumbersList = findPrimeNumbers(startNumber, endNumber);

    // Convert from list to array.
    int[] primeNumbersArray = primNumbersList.stream().mapToInt(Integer::intValue).toArray();

    // Sort array with quicksort.
    SortingAlgorithm.quicksort(primeNumbersArray, 0, primeNumbersArray.length - 1);

    System.out.println("Prime numbers between " + startNumber + " and "
            + endNumber + ": " + Arrays.toString(primeNumbersArray));
  }

  /**
   * Finds prime numbers in the specified range using a multithreaded approach.
   *
   * @param startNumber The starting number of the range.
   * @param endNumber The ending number of the range.
   * @return A list of prime numbers within the specified range.
   */
  private static List<Integer> findPrimeNumbers(int startNumber, int endNumber) {
    int numberOfThreads = 4;

    // Adding 1 ensures that the upper end of the range is included.
    int rangePerThread = (endNumber - startNumber + 1) / numberOfThreads;

    List<Integer> primNumbersList = new ArrayList<>();

    PrimeFinder[] threads = new PrimeFinder[numberOfThreads];

    for (int i = 0; i < numberOfThreads; i++) {
      int threadStart = startNumber + i * rangePerThread;
      // If the current thread is the last thread use the endNumber, otherwise calculate the end of the range.
      // Removes 1 in end range calculation to prevent overlap between threads.
      int threadEnd = (i == numberOfThreads - 1) ? endNumber : threadStart + rangePerThread - 1;

      PrimeFinder thread = new PrimeFinder(threadStart, threadEnd, primNumbersList);
      threads[i] = thread;
      thread.start(); // Starts the thread and runs the run() function.
    }

    // Waits for every thread to finish before continuing.
    for (PrimeFinder thread : threads) {
      try {
        thread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    return primNumbersList;
  }
}