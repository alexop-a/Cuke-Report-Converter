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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import io.github.alexopa.cukereportconverter.util.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Class that models a cucumber scenario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(doNotUseGetters = true, exclude = { "parent" })
public class CukeScenario {

	private CukeFeature parent;
	private LocalDateTime startTimestamp;
	private LocalDateTime endTimestamp;
	private String name;
	private CukeScenarioType type;
	private String description;
	
	@Builder.Default
	private List<String> tags = new ArrayList<>();

	@Builder.Default
	private CukeScenarioResult result = CukeScenarioResult.FAILED;

	private int line;
	
	@Builder.Default
	private List<CukeStep> beforeSteps = new ArrayList<>();
	@Builder.Default
	private List<CukeStep> afterSteps = new ArrayList<>();
	@Builder.Default
	private List<CukeStep> backgroundSteps = new ArrayList<>();
	@Builder.Default
	private List<CukeStep> scenarioSteps = new ArrayList<>();

	@Builder.Default
	private long beforeStepsDuration = 0;
	@Builder.Default
	private long afterStepsDuration = 0;
	@Builder.Default
	private long backgroundStepsDuration = 0;
	@Builder.Default
	private long scenarioStepsDuration = 0;
	@Builder.Default
	private long totalDuration = 0;
	
	@Builder.Default
	private Map<CukeStepResult, Integer> stepResultCounter = new HashMap<>(); 

	/**
	 * Returns a boolean indicating if scenario has before hook steps
	 * 
	 * @return <code>true</code> if the scenario has before hook steps
	 */
	public boolean hasBeforeSteps() {
		return !Utils.isListNullOrEmpty(beforeSteps);
	}

	/**
	 * Returns a boolean indicating if scenario has after hook steps
	 * 
	 * @return <code>true</code> if the scenario has after hook steps
	 */
	public boolean hasAfterSteps() {
		return !Utils.isListNullOrEmpty(afterSteps);
	}

	/**
	 * Returns a boolean indicating if scenario has background steps
	 * 
	 * @return <code>true</code> if the scenario has background steps
	 */
	public boolean hasBackground() {
		return !Utils.isListNullOrEmpty(backgroundSteps);
	}

	/**
	 * Returns a boolean indicating if this is a scenario outline
	 * 
	 * @return <code>true</code> if this is a scenario outline
	 */
	public boolean isOutline() {
		return CukeScenarioType.SCENARIO_OUTLINE.equals(type);
	}

	/**
	 * Returns a boolean indication if the scenario passed
	 * 
	 * @return <code>true</code> if the scenario passed
	 */
	public boolean isSuccess() {
		return CukeScenarioResult.PASSED.equals(result);
	}
	
	/**
	 * Method that initializes {@link #stepResultCounter}. It can be called once to
	 * initialize this map that contains a counter for each {@link CukeStepResult} for
	 * this scenario.
	 */
	public void countStepResults() {
		if (hasBeforeSteps()) {
			beforeSteps.stream().forEach(countStepResults::accept);
		}
		if (hasAfterSteps()) {
			afterSteps.stream().forEach(countStepResults::accept);
		}
		if (hasBackground()) {
			backgroundSteps.stream().forEach(countStepResults::accept);
		}
		scenarioSteps.stream().forEach(countStepResults::accept);
	}
	
	private final Consumer<CukeStep> countStepResults = (CukeStep s) -> {
		CukeStepResult stepResult = s.getResult();
		int counter = stepResultCounter.getOrDefault(stepResult, 0);
		stepResultCounter.put(stepResult, counter + 1);
	};
	
}
