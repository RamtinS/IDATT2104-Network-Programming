package edu.ntnu.idatt2104;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * The class represents a simple socket server that listens for client
 * connections over UDP.
 *
 * @author Ramtin Samavat
 * @version 1.0
 * @since Feb 06, 2024
 */
public class Server {

  private static final int PORT = 1250;

  /**
   * The main entry point for the UDPServer application.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    try(DatagramSocket socket = new DatagramSocket(PORT)) {
      System.out.println("Server started. Listening on port " + PORT + " for client requests.");

      while (true) {
        byte[] receiveDataBuf = new byte[256];
        DatagramPacket receivePacket = new DatagramPacket(receiveDataBuf, receiveDataBuf.length); // Create a packet.
        socket.receive(receivePacket); // Wait for a client packet.
        handleClientRequest(socket, receivePacket);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles the client request received via UDP.
   *
   * @param socket The DatagramSocket instance.
   * @param receivePacket The DatagramPacket received from the client.
   * @throws IOException If an I/O error occurs while sending the response.
   */
  private static void handleClientRequest(DatagramSocket socket, DatagramPacket receivePacket) throws IOException {

    // First parameter: Byte array with data from the client.
    // Second parameter: Index to begin reading the array.
    // Third parameter: Index to stop reading the array.
    String equation = new String(receivePacket.getData(), 0, receivePacket.getLength());
    System.out.println("Equation from client: " + equation);

    InetAddress inetAddress = receivePacket.getAddress(); // The IP-address of the client.
    int port = receivePacket.getPort(); // The port number the client used to send the packet.

    try {
      double solution = calculateSolution(equation);
      sendResponse(socket, String.valueOf(solution), inetAddress, port);
    } catch (IllegalArgumentException e) {
      String errorMessage = "Invalid format. Please enter a valid mathematical equation with only two numbers.";
      sendResponse(socket, errorMessage, inetAddress, port);
      e.printStackTrace();
    }
  }

  /**
   * Sends a response message back to the client.
   *
   * @param socket  The DatagramSocket instance.
   * @param message The message to be sent.
   * @param address The InetAddress of the client.
   * @param port The port number of the client.
   * @throws IOException If an I/O error occurs while sending the response.
   */
  private static void sendResponse(DatagramSocket socket, String message, InetAddress address, int port) throws IOException {
    byte[] sendData = message.getBytes(); // Encodes the string into bytes.
    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
    socket.send(sendPacket); // Send response back to the client.
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
    if (inputComponents.length != 3) {
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
