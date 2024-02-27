package edu.ntnu.idatt2104.compiler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Service class responsible for compiling and executing C++ code.
 *
 * @author Ramtin Samavat
 * @version 1.0
 * @since Feb 16, 2024
 */
@Service
public class CompilerService {

  private static final Logger logger = LoggerFactory.getLogger(CompilerService.class);

  /**
   * Compiles and executes the given C++ code.
   *
   * @param cppCode The C++ code to compile and execute.
   * @return The output of the compilation and execution process.
   */
  public String compileAndExecuteCppCode(String cppCode) {
    // Write the received C++ code to a file for compiling.
    String filePath = "src/main/resources/main.cpp";
    File sourceCodeFile = new File(filePath);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(sourceCodeFile.getPath()))) {
      writer.write(cppCode);
      logger.info("File with received source code successfully created.");
    } catch (IOException e) {
      logger.error("Error occurred while writing source code to file.", e);
    }

    // Build the Docker image from the Dockerfile
    String buildCommand = "docker build -t cpp-compiler . ";
    try {
      Process buildProcess = Runtime.getRuntime().exec(buildCommand);
      int exitCode = buildProcess.waitFor();
      if (exitCode == 0) {
        logger.info("Docker image successfully created.");
      } else {
        logger.error("Error occurred while building Docker image.");
        return "Syntax error.";
      }
    } catch (IOException | InterruptedException e) {
      logger.error("Error occurred while building Docker image.", e);
    }

    // Compile and execute the C++ code.
    StringBuilder output = new StringBuilder();
    String compileCommand = "docker run --rm cpp-compiler";
    try {
      Process compileProcess = Runtime.getRuntime().exec(compileCommand);
      int exitCode = compileProcess.waitFor();
      if (exitCode == 0) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
          output.append(line).append("\n");
        }
        logger.info("C++ code successfully compiled and executed.");
      } else {
        logger.error("Error occurred while compiling C++ code.");
        output.append("Syntax error.");
      }
    } catch (IOException | InterruptedException e) {
      logger.error("Error occurred while compiling C++ code.", e);
    }

    logger.info("Output result: " + output);
    return output.toString();
  }
}
