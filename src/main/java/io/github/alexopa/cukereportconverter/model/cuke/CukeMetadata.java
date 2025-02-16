package io.github.alexopa.cukereportconverter.model.cuke;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A class to hold some metadata for the cuke report model. They can be used to
 * hold some extra custom information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(doNotUseGetters = true)
public class CukeMetadata {

	private String name;
	private String id;
	private String status;
}
