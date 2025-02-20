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

import lombok.experimental.UtilityClass;

/**
 * Class that defines the default values of the service properties
 */
@UtilityClass
public class CukeConverterDefaultValues {

	/**
	 * The default value of {@link CukeConverterProperties#CONVERTER_FAIL_ON_ERROR} property
	 */
	protected static final boolean DEFAULT_CONVERTER_FAIL_ON_ERROR = false;
	
	/**
	 * The default value of {@link CukeConverterProperties#CONVERTER_MERGE_FEATURES} property
	 */
	protected static final boolean DEFAULT_CONVERTER_MERGE_FEATURES = true;

}
