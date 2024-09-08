package io.github.alexopa.cukereportconverter.model.jsonreport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class the models an element of the array inside the "tags" element of the json
 * cucumber report file.
 */
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tag {

	private String name;
	private String type;
	private TagLocation location;
}
