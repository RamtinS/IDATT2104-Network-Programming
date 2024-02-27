package edu.ntnu.idatt2104.compiler.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) class for representing a request to compile code.
 *
 * @author Ramtin Samavat
 * @version 1.0
 * @since Feb 16, 2024
 */
@Getter
@Setter
public class CompilerRequest {
  private String code;
}
