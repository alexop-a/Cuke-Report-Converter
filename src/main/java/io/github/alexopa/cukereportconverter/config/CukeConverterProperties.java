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
package io.github.alexopa.cukereportconverter.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * An enum with the available properties that can configure the service, with
 * their default values
 */
@RequiredArgsConstructor
@Getter
public enum CukeConverterProperties {

	/**
	 * Property that defines if the execution should fail and throw an exception in
	 * case an error happens while parsing a cucumber json report file
	 */
	CONVERTER_FAIL_ON_ERROR("cuke-converter.failOnError",
			Boolean.toString(CukeConverterDefaultValues.DEFAULT_CONVERTER_FAIL_ON_ERROR)),
	/**
	 * Property that defines if features with the same name should be merged, if
	 * they appear more than one time. If they should be merged, then all scenarios
	 * (even if they are from different files/reports) are added in the same feature
	 */
	CONVERTER_MERGE_FEATURES("cuke-converter.mergeFeatures",
			Boolean.toString(CukeConverterDefaultValues.DEFAULT_CONVERTER_MERGE_FEATURES));

	private final String propertyName;
	private final String defaultValue;

}
