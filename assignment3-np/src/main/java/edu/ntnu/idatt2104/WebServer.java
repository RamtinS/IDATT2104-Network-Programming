package edu.ntnu.idatt2104;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Simple WebServer implementation that listens on port 80 and handles incoming HTTP requests.
 * It responds with a simple HTML page displaying the request headers.
 *
 * @author Ramtin Samavat
 * @version 1.0
 * @since Feb 03, 2024
 */
public class WebServer {

  /**
   * The main entry point for the WebServer application.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    final int PORT = 80;
    try (ServerSocket server = new ServerSocket(PORT)) {
      System.out.println("Webserver starting...");

      Socket connection = server.accept();

      handelClientRequest(connection);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles the client's HTTP request and sends a response.
   *
   * @param connection The socket connection to the client.
   */
  private static void handelClientRequest(Socket connection) {
    try {
      // Opens stream for communication with the client.
      InputStreamReader readConnection = new InputStreamReader(connection.getInputStream());
      BufferedReader reader = new BufferedReader(readConnection);
      PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

      // Reads HTTP-header from the client.
      StringBuilder header = new StringBuilder();
      String line;
      while (!(line = reader.readLine()).isEmpty()) {
        header.append(line).append("\n");
      }
      System.out.println("Header form client: \n" + header);

      String response = "HTTP/1.0 200 OK\n" +
              "Content-Type: text/html; charset=utf-8\n" +
              "\n\n" +
              "<HTML><BODY>" +
              "<H1>Hi. You have connected to my simple web server.</H1>" +
              "Header from client:" +
              "<UL>" +
              formatHeaderAsList(header) +
              "</UL>" +
              "</BODY></HTML>";

      // Send data to the client.
      writer.println(response);

      reader.close();
      writer.close();
      connection.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Formats the HTTP header as an HTML list.
   *
   * @param header The HTTP header received from the client.
   * @return The formatted header as an HTML list.
   */
  private static String formatHeaderAsList(StringBuilder header) {
    String[] lines = header.toString().split("\n");
    StringBuilder listItem = new StringBuilder();
    for (String line : lines) {
      listItem.append("<LI>").append(line).append("<LI>");
    }
    return listItem.toString();
  }
}
