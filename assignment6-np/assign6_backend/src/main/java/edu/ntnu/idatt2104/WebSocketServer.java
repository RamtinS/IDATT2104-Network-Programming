package edu.ntnu.idatt2104;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The WebSocketServer class sets up a server that listens for incoming client connections
 * on a specified port and starts a new thread to handle each connection.
 *
 * <p>This server listens on port 3001 by default and maintains a list of connected clients.
 *
 * @author Ramtin Samavat
 * @version 1.0
 */
public class WebSocketServer {

  private static final int PORT = 3001;

  /**
   * The main method starts the WebSocket server, listens for client connections,
   * and creates a new thread for each connected client.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    int clientCounter = 0;

    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      System.out.println("WebSocket server listening on port " + PORT);

      List<Socket> clients = new ArrayList<>();

      while (true) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("\nClient connected.");

        int clientId = ++clientCounter;
        WebSocketThread clientThread = new WebSocketThread(clientSocket, clients, clientId);
        clientThread.start();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
