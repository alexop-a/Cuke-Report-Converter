/*
 * (C) Copyright 2024 Andreas Alexopoulos (https://alexop-a.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.github.alexopa.cukereportconverter.model.cuke;

import java.util.List;
import java.util.stream.Collectors;

import io.github.alexopa.cukereportconverter.model.jsonreport.StepMatch;
import io.github.alexopa.cukereportconverter.util.Utils;
import lombok.Data;

/**
 * Class the model the matching location of a step in the feature file
 */
@Data
public class CukeStepMatch {

	private List<CukeMatchArgument> arguments;
	private String location;

	/**
	 * Method that creates a {@link CukeStepMatch} object from a {@link StepMatch}
	 * object
	 * 
	 * @param jsonStepMatch The {@link StepMatch} instance to convert
	 * @return A new {@link CukeStepMatch} object from the given {@link StepMatch}
	 *         object
	 */
	public static CukeStepMatch from(StepMatch jsonStepMatch) {
		CukeStepMatch stepMatch = new CukeStepMatch();
		if (!Utils.isListNullOrEmpty(jsonStepMatch.getArguments())) {
			stepMatch.setArguments(
					jsonStepMatch.getArguments().stream().map(CukeMatchArgument::from).collect(Collectors.toList()));
		}
		stepMatch.setLocation(jsonStepMatch.getLocation());

		return stepMatch;
	}
}
