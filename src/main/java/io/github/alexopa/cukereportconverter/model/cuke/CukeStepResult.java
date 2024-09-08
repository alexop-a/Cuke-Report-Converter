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

import java.util.stream.Stream;

/**
 * An enum that models the step result
 */
public enum CukeStepResult {

	/**
	 * Indicates that step passed
	 */
	PASSED,
	/**
	 * Indicates that step failed
	 */
	FAILED,
	/**
	 * Indicates that step was skipped
	 */
	SKIPPED,
	/**
	 * Indicates that the step is pending
	 */
	PENDING,
	/**
	 * Indicates that the step was undefined
	 */
	UNDEFINED,
	/**
	 * Indicates that the step status cannot be mapped to any of the other enum
	 * cases
	 */
	UNKNOWN_RESULT;

	/**
	 * Method to return a {@link CukeStepResult} from a text
	 * 
	 * @param text A {@link String} that should be returned as {@link CukeStepResult}
	 * @return a {@link CukeStepResult} that matches the given text, or
	 *         {@link #UNKNOWN_RESULT} if nothing matches
	 */
	public static CukeStepResult fromText(String text) {
		return Stream.of(values()).filter(v -> v.name().equalsIgnoreCase(text.trim())).findFirst()
				.orElse(CukeStepResult.UNKNOWN_RESULT);
	}
}
