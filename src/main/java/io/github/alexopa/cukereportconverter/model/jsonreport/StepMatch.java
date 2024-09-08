package io.github.alexopa.cukereportconverter.model.jsonreport;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class the models the "match" element inside a "step" element of the json
 * cucumber report file.
 */
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StepMatch {

	private List<MatchArgument> arguments = new ArrayList<>();
	private String location;
}
