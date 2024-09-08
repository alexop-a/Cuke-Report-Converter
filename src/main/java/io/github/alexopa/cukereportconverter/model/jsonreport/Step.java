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
package io.github.alexopa.cukereportconverter.model.jsonreport;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class the models an element of the array inside the "steps" element of the
 * json cucumber report file. With the "steps" element, we refer to "before",
 * "steps" or "after" element.
 */
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Step {

	private List<Embedding> embeddings;
	private StepResult result;
	private int line;
	private String name;
	private StepMatch match;
	private String keyword;
	private List<Row> rows = new ArrayList<>();
	@JsonProperty("doc_string")
	private DocString docString;
	private List<Step> before = new ArrayList<>();
	private List<Step> after = new ArrayList<>();
}
