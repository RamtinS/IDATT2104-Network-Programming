package edu.ntnu.idatt2104.compiler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main application class for the compiler.
 *
 * @author Ramtin Samavat
 * @version 1.0
 * @since Feb 16, 2024
 */
@SpringBootApplication
public class CompilerApplication {

  /**
   * Main method to run the Spring Boot application.
   *
   * @param args Command-line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(CompilerApplication.class, args);
  }
}
