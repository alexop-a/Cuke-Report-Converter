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

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Class that models a cucumber step of a scenario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(doNotUseGetters = true, exclude = { "parent" })
public class CukeStep {
	
	private CukeScenario parent;
	
	private CukeStepSection stepSection;
	private String keyword;
	private String name;
	private int line;
	private CukeStepResult result;
	private long duration;
	private String errorMessage;
	private CukeStepMatch match;
	private List<CukeEmbedding> embeddings;
	private List<List<String>> tableData;
	private String docString;
	@Builder.Default
	private List<CukeStep> beforeSteps = new ArrayList<>();
	@Builder.Default
	private List<CukeStep> afterSteps = new ArrayList<>();

}
