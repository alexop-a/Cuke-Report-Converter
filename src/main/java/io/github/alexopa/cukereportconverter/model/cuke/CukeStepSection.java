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

/**
 * An enum to model the section of a scenario that a step belongs to
 */
public enum CukeStepSection {
	/**
	 * Indicates a step that belongs to a before scenario hook
	 */
	BEFORE_SCENARIO,
	/**
	 * Indicates a step that belongs to an after scenario hook
	 */
	AFTER_SCENARIO,
	/**
	 * Indicates a step that belongs to a background step
	 */
	BACKGROUND,
	/**
	 * Indicates a step that belongs to a before step hook
	 */
	BEFORE_STEP,
	/**
	 * Indicates a step that belongs to an after step hook
	 */
	AFTER_STEP,
	/**
	 * Indicates a step that belongs to the scenario itself
	 */
	SCENARIO;
}
