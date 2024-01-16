package edu.ntnu.idatt2104;

/**
 * The class provides methods to perform the quicksort algorithm on an array.
 *
 * @author Ramtin Samavat.
 * @version 1.0
 * @since Jan 09, 2024
 */
public class SortingAlgorithm {

  /**
   * The method sorts an integer array using the quicksort
   * algorithm aided by insertion sort.
   *
   * @param array The integer array to be sorted.
   * @param lowIndex The index of the first element in the range to be sorted.
   * @param highIndex The index of the last element in the range to be sorted.
   */
  public static void quicksort(int[] array, int lowIndex, int highIndex) {
    if (highIndex - lowIndex > 10) {
      int pivot = partition(array, lowIndex, highIndex);
      quicksort(array, lowIndex, pivot - 1);
      quicksort(array, pivot + 1, highIndex);
    } else {
      insertionSort(array, lowIndex, highIndex);
    }
  }

  /**
   * Partitions an array for the quicksort algorithm.
   * The method selects a pivot element and rearranges the elements in
   * the array so that elements less than the pivot are on the left side,
   * and elements greater than the pivot are on the right side.
   *
   * @param array The integer array to be partitioned.
   * @param lowIndex The index of the first element in the range to be partitioned.
   * @param highIndex The index of the last element in the range to be partitioned.
   * @return The index of the pivot element after partitioning.
   */
  private static int partition(int[] array, int lowIndex, int highIndex) {
    int leftIndex;
    int rightIndex;
    int median = median3sort(array, lowIndex, highIndex);
    int pivotValue = array[median];
    swap(array, median, highIndex - 1);
    for (leftIndex = lowIndex, rightIndex = highIndex - 1;;) {
      while (array[++leftIndex] < pivotValue);
      while (array[--rightIndex] > pivotValue);
      if (leftIndex >= rightIndex) break;
      swap(array, leftIndex, rightIndex);
    }
    swap(array, leftIndex, highIndex - 1);
    return leftIndex;
  }

  /**
   * This method selects the median value among three
   * values in the array to improve pivot selection for quicksort.
   *
   * @param array The integer array containing the values.
   * @param lowIndex The index of the first value.
   * @param highIndex The index of the last value.
   * @return The index of the median value among the three.
   */
  private static int median3sort(int[] array, int lowIndex, int highIndex) {
    int median = (lowIndex + highIndex) / 2;
    if (array[lowIndex] > array[median]) {
      swap(array, lowIndex, median);
    }
    if (array[median] > array[highIndex]) {
      swap(array, median, highIndex);
      if (array[lowIndex] > array[median]) {
        swap(array, lowIndex, median);
      }
    }
    return median;
  }

  /**
   * The method swaps two elements in an integer array.
   *
   * @param array The array in which elements are to be swapped.
   * @param i The index of the first element to be swapped.
   * @param j The index of the second element to be swapped.
   */
  public static void swap(int[] array, int i, int j) {
    int temp = array[j];
    array[j] = array[i];
    array[i] = temp;
  }

  /**
   * The method sorts an integer array using the insertion sort algorithm.
   *
   * @param array The integer array to be sorted.
   * @param lowIndex The index of the first element in the range to be sorted.
   * @param highIndex The index of the last element in the range to be sorted.
   */
  public static void insertionSort(int[] array, int lowIndex, int highIndex) {
    for (int i = lowIndex + 1; i <= highIndex; i++) {
      int swap = array[i];
      int j = i - 1;
      while (j >= lowIndex && array[j] > swap) {
        array[j + 1] = array[j];
        --j;
      }
      array[j + 1] = swap;
    }
  }
}
