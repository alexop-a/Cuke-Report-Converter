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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.github.alexopa.cukereportconverter.util.Utils;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Class that models a test-run. As test-run we consider the collection of
 * {@link CukeFeature}s. Other metadata, associated with the test-run may be
 * available through this class.
 */
@Data
@Builder
@ToString(doNotUseGetters = true)
public class CukeTestRun {

	@Builder.Default
	private List<CukeFeature> features = new ArrayList<>();
	private LocalDateTime startTime;

	/**
	 * Calculates that startTime of the test-run. As startTime, we consider the
	 * minimum startTime of all {@link CukeScenario}s in the {@link CukeFeature}s.
	 */
	public void calculateStartTime() {
		if (!Utils.isListNullOrEmpty(features)) {
			startTime = features.stream().min(Comparator.comparing(CukeFeature::getScenarioMinStartTime)).get()
					.getScenarioMinStartTime();
		}
	}

}
