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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Class that models a cucumber feature.
 */
@Data
@Builder
@ToString(doNotUseGetters = true)
public class CukeFeature {

	private String name;
	private String description;
	@Builder.Default
	private List<String> tags = new ArrayList<>();
	@Builder.Default
	private Set<String> totalTags = new HashSet<>();
	@Builder.Default
	private List<CukeScenario> scenarios = new ArrayList<>();
	@Builder.Default
	private int numOfPassedScenarios = 0;
	@Builder.Default
	private int numOfFailedScenarios = 0;
	@Builder.Default
	private Map<CukeStepResult, Integer> stepResultCounter = new HashMap<>();
	@Builder.Default
	private LocalDateTime minScenarioStartTime = null;
	@Builder.Default
	private LocalDateTime maxScenarioEndTime = null;
	@Builder.Default
	private long totalDuration = 0;
	private String codeRef;

	/**
	 * Calculates and returns the minimum startTime of the {@link CukeScenario}s that
	 * belong to this {@link CukeFeature}
	 * 
	 * @return A {@link LocalDateTime} with the minimum startTime of the
	 *         {@link CukeScenario}s that belong to this {@link CukeFeature}
	 */
	public LocalDateTime getScenarioMinStartTime() {
		if (minScenarioStartTime == null) {
			minScenarioStartTime = scenarios.stream().min(Comparator.comparing(CukeScenario::getStartTimestamp)).get()
					.getStartTimestamp();

		}
		return minScenarioStartTime;
	}

	/**
	 * Calculates and returns the maximum endTime of the {@link CukeScenario}s that
	 * belong to this {@link CukeFeature}
	 * 
	 * @return A {@link LocalDateTime} with the maximum endTime of the
	 *         {@link CukeScenario}s that belong to this {@link CukeFeature}
	 */
	public LocalDateTime getScenarioMaxEndTime() {
		if (maxScenarioEndTime == null) {
			maxScenarioEndTime = scenarios.stream().max(Comparator.comparing(CukeScenario::getEndTimestamp)).get()
					.getEndTimestamp();

		}
		return maxScenarioEndTime;
	}

	/**
	 * Returns the number of scenarios that belongs to this {@link CukeFeature}
	 * 
	 * @return An <code>int</code> with the number of scenarios that belongs to this
	 *         {@link CukeFeature}
	 */
	public int getNumOfScenarios() {
		return scenarios.size();
	}

	/**
	 * Increases the counter of the passed scenarios for this {@link CukeFeature}
	 */
	public void increaseNumOfPassedScenarios() {
		++numOfPassedScenarios;
	}

	/**
	 * Increases the counter of the failed scenarios for this {@link CukeFeature}
	 */

	public void increaseNumOfFailedScenarios() {
		++numOfFailedScenarios;
	}

	/**
	 * Method that calculates and increases the counters for each step of the
	 * scenarios that belong to this feature. Those counters will count each
	 * {@link CukeStepResult} separately.
	 */
	public void increaseStepResultCounter() {
		scenarios.stream().forEach(s -> {
			Map<CukeStepResult, Integer> scenarioStepResultCounter = s.getStepResultCounter();
			for (Map.Entry<CukeStepResult, Integer> entry : scenarioStepResultCounter.entrySet()) {
				CukeStepResult stepResult = entry.getKey();
				int count = Optional.ofNullable(entry.getValue()).orElse(0);
				int currentValue = stepResultCounter.getOrDefault(stepResult, 0);
				stepResultCounter.put(stepResult, currentValue + count);
			}
		});
	}

	/**
	 * Returns the total number of passed steps for this feature
	 * 
	 * @return An <code>int</code> with the total number of passed steps for this
	 *         feature
	 */
	public int getTotalPassedSteps() {
		return stepResultCounter.getOrDefault(CukeStepResult.PASSED, 0);
	}

	/**
	 * Returns the total number of failed steps for this feature
	 * 
	 * @return An <code>int</code> with the total number of failed steps for this
	 *         feature
	 */
	public int getTotalFailedSteps() {
		return stepResultCounter.getOrDefault(CukeStepResult.FAILED, 0);
	}

	/**
	 * Returns the total number of skipped steps for this feature
	 * 
	 * @return An <code>int</code> with the total number of skipped steps for this
	 *         feature
	 */
	public int getTotalSkippedSteps() {
		return stepResultCounter.getOrDefault(CukeStepResult.SKIPPED, 0);
	}

	/**
	 * Returns the total number of pending steps for this feature
	 * 
	 * @return An <code>int</code> with the total number of pending steps for this
	 *         feature
	 */
	public int getTotalPendingSteps() {
		return stepResultCounter.getOrDefault(CukeStepResult.PENDING, 0);
	}

	/**
	 * Returns the total number of undefined steps for this feature
	 * 
	 * @return An <code>int</code> with the total number of undefined steps for this
	 *         feature
	 */
	public int getTotalUndefinedSteps() {
		return stepResultCounter.getOrDefault(CukeStepResult.UNDEFINED, 0);
	}

	/**
	 * Static method that creates a {@link CukeFeature} object from another object.
	 * 
	 * @param other A {@link CukeFeature} instance to copy
	 * @return A new {@link CukeFeature} object from another {@link CukeFeature} object
	 */
	public static CukeFeature from(CukeFeature other) {
		return CukeFeature.builder()
				.name(other.getName())
				.description(other.getDescription())
				.tags(other.getTags())
				.totalTags(other.getTotalTags())
				.scenarios(other.getScenarios())
				.numOfPassedScenarios(other.getNumOfPassedScenarios())
				.numOfFailedScenarios(other.getNumOfFailedScenarios())
				.stepResultCounter(other.getStepResultCounter())
				.minScenarioStartTime(other.getMinScenarioStartTime())
				.totalDuration(other.totalDuration)
				.codeRef(other.getCodeRef())
				.build();
	}

}
