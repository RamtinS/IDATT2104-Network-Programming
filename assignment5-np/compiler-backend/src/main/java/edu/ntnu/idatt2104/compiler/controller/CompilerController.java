package edu.ntnu.idatt2104.compiler.controller;

import edu.ntnu.idatt2104.compiler.dto.CompilerRequest;
import edu.ntnu.idatt2104.compiler.dto.CompilerResponse;
import edu.ntnu.idatt2104.compiler.service.CompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for handling HTTP requests related to the compiler.
 *
 * @author Ramtin Samavat
 * @version 1.0
 * @since Feb 16, 2024
 */
@RestController
@RequestMapping(path = "/compiler")
public class CompilerController {

  private final CompilerService compilerService;

  /**
   * Constructor for CompilerController.
   *
   * @param compilerService The service responsible for compiling code.
   */
  @Autowired
  public CompilerController(CompilerService compilerService) {
    this.compilerService = compilerService;
  }

  /**
   * Endpoint for compiling C++ code.
   *
   * @param compilerRequest The request containing the C++ code to compile.
   * @return The response containing the compiled code.
   */
  @PostMapping("/cpp")
  @CrossOrigin(origins = "http://localhost:5173")
  public CompilerResponse compileCode(@RequestBody CompilerRequest compilerRequest) {
    String compiledCode = compilerService.compileAndExecuteCppCode(compilerRequest.getCode());
    return new CompilerResponse(compiledCode);
  }
}
