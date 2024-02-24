package edu.ntnu.idatt2104;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * The class represents a simple socket client that communicates
 * with a server over UDP to perform mathematical calculations.
 *
 * @author Ramtin Samavat
 * @version 1.0
 * @since Feb 03, 2024
 */
public class Client {

  private static final String SERVER_IP = "localhost";
  private static final int SERVER_PORT = 1250;

  /**
   * The main entry point for the client application.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in); // Used to read data from the user in the terminal.

    try (DatagramSocket socket = new DatagramSocket()) {
      InetAddress serverAddress = InetAddress.getByName(SERVER_IP);

      while (true) {
        System.out.println("Enter an equation (or type 'exit' to end):");
        String inputLine = scanner.nextLine(); // Reads text from terminal (user).

        if (inputLine.equalsIgnoreCase("exit")) {
          break;
        }

        // Send equation to server.
        byte[] sendDataBuf = inputLine.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendDataBuf, sendDataBuf.length, serverAddress, SERVER_PORT);
        socket.send(sendPacket); // Send the packet with data to server.

        // Receive response from server.
        byte[] receiveDataBuf = new byte[256];
        DatagramPacket receivePacket = new DatagramPacket(receiveDataBuf, receiveDataBuf.length);
        socket.receive(receivePacket);
        String solution = new String(receivePacket.getData(), 0, receivePacket.getData().length);

        // Show answer to user.
        System.out.println("Solution from server: " + solution);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
