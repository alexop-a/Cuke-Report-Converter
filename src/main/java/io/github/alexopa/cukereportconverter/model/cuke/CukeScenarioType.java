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

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * An enum that models the scenario type
 */
@RequiredArgsConstructor
@Getter
public enum CukeScenarioType {
	/**
	 * Indicates the "Scenario" type
	 */
	SCENARIO("Scenario"),
	/**
	 * Indicates the "Scenario Outline" type
	 */
	SCENARIO_OUTLINE("Scenario Outline");

	private final String text;
}
