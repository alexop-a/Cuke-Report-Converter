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

import io.github.alexopa.cukereportconverter.model.jsonreport.MatchArgument;
import lombok.Data;

/**
 * Class that models the match location of an argument in a step
 */
@Data
public class CukeMatchArgument {

	private String val;
	private int offset;

	/**
	 * Method that creates a {@link CukeMatchArgument} object from a
	 * {@link MatchArgument} object
	 * 
	 * @param jsonMatchArgument The {@link MatchArgument} instance to convert
	 * @return A new {@link CukeMatchArgument} object from the given
	 *         {@link CukeMatchArgument} object
	 */
	public static CukeMatchArgument from(MatchArgument jsonMatchArgument) {
		CukeMatchArgument matchArgument = new CukeMatchArgument();
		matchArgument.setVal(jsonMatchArgument.getVal());
		matchArgument.setOffset(jsonMatchArgument.getOffset());

		return matchArgument;
	}
}
