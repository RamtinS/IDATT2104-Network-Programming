package edu.ntnu.idatt2104;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The class represents a simple socket server that listens for client
 * connections over TCP and handles them using separate threads.
 *
 * @author Ramtin Samavat
 * @version 1.0
 * @since Feb 03, 2024
 */
public class SocketServer {

  /**
   * The main entry point for the SocketServer application.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {

    final int PORT = 1250;
    int clientCounter = 0;

    try (ServerSocket socket = new ServerSocket(PORT)) {
      System.out.println("Waiting for clients... \n");

      while (true) {
        Socket connection = socket.accept(); // Waits for a client.
        System.out.println("New client connected.");

        // Create new Thread for each client.
        int clientId = ++clientCounter;
        ClientHandler clientThread = new ClientHandler(connection, clientId);
        clientThread.start();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
