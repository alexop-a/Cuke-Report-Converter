package io.github.alexopa.cukereportconverter.model.jsonreport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class the models the "result" element inside a "step" element of the json
 * cucumber report file.
 */
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StepResult {

	private long duration;
	private String status;
	@JsonProperty("error_message")
	private String errorMessage;
}
