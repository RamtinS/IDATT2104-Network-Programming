package edu.ntnu.idatt2104;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The class handles communication with a client in a separate thread.
 *
 * @author Ramtin Samavat
 * @version 1.0
 * @since Feb 03, 2024
 */
public class ClientHandler extends Thread {
  private final Socket connection;
  private final int clientId;

  /**
   * Constructs a new instance of ClientHandler.
   *
   * @param connection The socket connection to the client.
   * @param clientId   The unique identifier for the client.
   */
  public ClientHandler(Socket connection, int clientId) {
    this.connection = connection;
    this.clientId = clientId;
  }

  /**
   * Runs the thread to handle communication with the client.
   */
  @Override
  public void run() {
    try {
      // Opens stream for communication with the client.
      InputStreamReader readConnection = new InputStreamReader(connection.getInputStream());
      BufferedReader reader = new BufferedReader(readConnection); // Read data from the client.
      PrintWriter writer = new PrintWriter(connection.getOutputStream(), true); // Write data to the client.

      // Informs client.
      writer.println("Hi, you have connected with the server.");
      writer.println("It is now available to calculate your mathematical equations.");

      // Receives data from client.
      String inputLine = reader.readLine(); // Receives a line with text from the client.

      while (inputLine != null && !inputLine.equalsIgnoreCase("exit")) {
        try {
          double solution = calculateSolution(inputLine);
          System.out.println("Equation from client " + clientId + ": " + inputLine);
          writer.println(solution); // Converts the solution to a string and sends it to the client.
        } catch (IllegalArgumentException e) {
          writer.println("Invalid format. Please enter a valid mathematical equation with only two numbers.");
          System.err.println("Error processing equation from client " + clientId + ": " + inputLine);
          e.printStackTrace();
        }
        inputLine = reader.readLine();
      }

      System.out.println("Client " + clientId + " disconnected.");

      // Close resources.
      reader.close();
      writer.close();
      connection.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Calculates the solution for a given mathematical equation.
   *
   * @param equation The mathematical equation in string format.
   * @return The calculated solution.
   * @throws IllegalArgumentException If the equation format is invalid.
   */
  private static double calculateSolution(String equation) throws IllegalArgumentException {
    String[] inputComponents = equation.replaceAll(" ", "").split("(?=[+\\-])|(?<=[+\\-])");
    if (inputComponents.length > 3) {
      throw new IllegalArgumentException();
    }
    double firstNumber = Double.parseDouble(inputComponents[0]);
    double lastNumber = Double.parseDouble(inputComponents[2]);

    if (inputComponents[1].equals("+")) {
      return firstNumber + lastNumber;
    } else if (inputComponents[1].equals("-")) {
      return firstNumber - lastNumber;
    } else {
      throw new IllegalArgumentException();
    }
  }
}
