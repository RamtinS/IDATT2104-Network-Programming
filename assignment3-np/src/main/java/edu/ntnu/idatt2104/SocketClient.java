package edu.ntnu.idatt2104;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * The claas represents a simple socket client that communicates
 * with a server over TCP to perform mathematical calculations.
 *
 * @author Ramtin Samavat
 * @version 1.0
 * @since Feb 03, 2024
 */
public class SocketClient {

  private static final String HOST = "localhost";
  private static final int SERVER_PORT = 1250;

  /**
   * The main entry point for the SocketClient application.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    try {
      // Read from the terminal.
      Scanner readFromTerminal = new Scanner(System.in);

      // Creates a connection between the host server and the client.
      Socket connection = new Socket(HOST, SERVER_PORT);
      System.out.println("Connection established between server and client.");

      // Opens stream for communication with server.
      InputStreamReader readConnection = new InputStreamReader(connection.getInputStream());
      BufferedReader reader = new BufferedReader(readConnection);
      PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

      // Read info from server.
      String connectionConformation = reader.readLine();
      String taskDescription = reader.readLine();
      System.out.println("\n" + connectionConformation + "\n" + taskDescription);

      while (true) {
        System.out.println("Enter an equation (or type 'exit' to end):");
        String inputLine = readFromTerminal.nextLine(); // Reads text from terminal (user).

        if (inputLine.equalsIgnoreCase("exit")) {
          break; // Exit the loop if the user wants to terminate the connection.
        }

        writer.println(inputLine); // Sends text to server.

        String response = reader.readLine(); // Receives response from server.
        System.out.println("Response from server: " + response); // Prints out response in terminal.
      }

      // Close resources.
      reader.close();
      writer.close();
      connection.close();
      readFromTerminal.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
