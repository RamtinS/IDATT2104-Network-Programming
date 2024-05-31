package edu.ntnu.idatt2104;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * The WebSocketThread class handles the communication between the server and a single client.
 * It performs the WebSocket handshake, reads frames from the client, and sends messages to
 * all connected clients.
 *
 * @author Ramtin Samavat
 * @version 1.0
 */
public class WebSocketThread extends Thread {

  private static final String GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
  private final int clientId;
  private final Socket connection;
  private final List<Socket> clients;
  private BufferedReader reader;
  private PrintWriter writer;
  private InputStream inputStream;
  private OutputStream outputStream;

  /**
   * Constructs a new WebSocketThread to handle communication with a specific client.
   *
   * @param connection the socket representing the client connection
   * @param clients    the list of all connected clients
   * @param clientId   the unique ID of the client
   */
  public WebSocketThread(Socket connection, List<Socket> clients, int clientId) {
    this.connection = connection;
    this.clients = clients;
    this.clientId = clientId;
  }

  /**
   * Runs the thread, handling the WebSocket handshake and communication with the client.
   */
  @Override
  public void run() {
    try {
      reader = new BufferedReader(
          new InputStreamReader(connection.getInputStream())); // Read data from the client.
      writer = new PrintWriter(connection.getOutputStream()); // Send data to the client.

      // Read the client's opening handshake.
      StringBuilder clientHandshake = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.isEmpty()) {
          break;
        }
        clientHandshake.append(line).append("\n");
      }

      System.out.println(
          "Opening handshake received from client " + clientId + ": \n" + clientHandshake);

      // Preform the opening handshake.
      String serverHandshake = openingHandshake(clientHandshake.toString());

      System.out.println("Opening handshake from server: \n" + serverHandshake);

      writer.println(serverHandshake);
      writer.flush();
      clients.add(connection);

      // In the WebSocket Protocol, data is transmitted using frames.
      // Read WebSocket frames from the client and send the payload to all connected clients.
      inputStream = new DataInputStream(connection.getInputStream());

      while (true) {

        byte[] buffer = new byte[1024]; // Create a buffer for input.

        int bytesRead = inputStream.read(buffer);

        if (bytesRead == -1) {
          clients.remove(connection);
          System.out.println("Client " + clientId + " disconnected.");
          break;
        }

        byte[] payload = Arrays.copyOfRange(buffer, 6, bytesRead);

        byte[] maskingKey = Arrays.copyOfRange(buffer, 2, 6);

        for (int i = 6; i < bytesRead; i++) {
          payload[i - 6] ^= maskingKey[(i - 6) % 4];
        }

        String message = new String(payload, StandardCharsets.UTF_8);
        System.out.println("Message from client " + clientId + " : " + message);

        for (Socket client : clients) {
          outputStream = new DataOutputStream(client.getOutputStream());
          sendWebSocketMessage(outputStream, message);
        }
      }

    } catch (IOException | NoSuchAlgorithmException e) {
      e.printStackTrace();
    } finally {
      try {
        // Close resources.
        reader.close();
        writer.close();
        inputStream.close();
        outputStream.close();
        connection.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Performs the WebSocket opening handshake.
   *
   * @param clientHandshake the client's handshake message
   * @return the server's handshake response
   * @throws NoSuchAlgorithmException if SHA-1 algorithm is not available
   */
  private String openingHandshake(String clientHandshake) throws NoSuchAlgorithmException {
    // Retrieve the Sec-WebSocket-Key form the client's opening handshake.
    String secWebSocketKey = clientHandshake
        .lines() // Returns a stream of lines.
        .filter(line -> line.startsWith("Sec-WebSocket-Key:")) // Filer out the line desired line.
        .map(line -> line.split(":")[1].trim()) // Split on colon and take the second part.
        .findFirst()
        .orElse("");

    // Calculate the Sec-WebSocket-Accept key
    String secWebSocketAcceptKey = Base64.getEncoder().encodeToString(
        MessageDigest.getInstance("SHA-1").digest((secWebSocketKey + GUID).getBytes()));

    // Create server handshake response.
    String serverHandshake = "HTTP/1.1 101 Switching Protocols\r\n"
        + "Upgrade: websocket\r\n"
        + "Connection: Upgrade\r\n"
        + "Sec-WebSocket-Accept: " + secWebSocketAcceptKey + "\r\n";

    return serverHandshake;
  }

  /**
   * Sends a WebSocket message to the client.
   *
   * @param stream  the output stream to the client
   * @param message the message to send
   * @throws IOException if an I/O error occurs
   */
  private void sendWebSocketMessage(OutputStream stream, String message) throws IOException {
    byte[] payload = message.getBytes();
    int len = payload.length;
    byte[] frame = new byte[2 + (len <= 125 ? 0 : 2) + len];
    int i = 0;
    frame[i++] = (byte) 0x81;
    if (len <= 125) {
      frame[i++] = (byte) len;
    } else {
      frame[i++] = (byte) 126;
      frame[i++] = (byte) (len >> 8 & 0xff);
      frame[i++] = (byte) (len & 0xff);
    }
    System.arraycopy(payload, 0, frame, i, len);
    stream.write(frame);
  }
}
