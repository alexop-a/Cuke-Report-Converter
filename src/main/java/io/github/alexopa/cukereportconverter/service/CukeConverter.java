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
package io.github.alexopa.cukereportconverter.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.github.alexopa.cukereportconverter.config.CukeConverterPropertyHandler;
import io.github.alexopa.cukereportconverter.exception.CukeConverterException;
import io.github.alexopa.cukereportconverter.model.cuke.CukeEmbedding;
import io.github.alexopa.cukereportconverter.model.cuke.CukeFeature;
import io.github.alexopa.cukereportconverter.model.cuke.CukeScenario;
import io.github.alexopa.cukereportconverter.model.cuke.CukeScenarioResult;
import io.github.alexopa.cukereportconverter.model.cuke.CukeStepResult;
import io.github.alexopa.cukereportconverter.model.cuke.CukeTestRun;
import io.github.alexopa.cukereportconverter.model.cuke.CukeStepMatch;
import io.github.alexopa.cukereportconverter.model.cuke.CukeScenarioType;
import io.github.alexopa.cukereportconverter.model.cuke.CukeStep;
import io.github.alexopa.cukereportconverter.model.cuke.CukeStepSection;
import io.github.alexopa.cukereportconverter.model.jsonreport.Element;
import io.github.alexopa.cukereportconverter.model.jsonreport.Feature;
import io.github.alexopa.cukereportconverter.model.jsonreport.Step;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The converter service class, that provides methods to parse cucumber report
 * json files
 */
@Slf4j
public class CukeConverter {

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final CukeConverterPropertyHandler propHandler;

	/**
	 * Creates a new {@link CukeConverter} instance. The default
	 * {@link CukeConverterPropertyHandler} is initialized and used.
	 */
	public CukeConverter() {
		this(new CukeConverterPropertyHandler());
	}
	
	/**
	 * Creates a new {@link CukeConverter} instance.
	 * 
	 * @param propHandler The {@link CukeConverterPropertyHandler} instance
	 *                               used to initialize the class
	 */
	public CukeConverter(CukeConverterPropertyHandler propHandler) {
		objectMapper.registerModule(new JavaTimeModule());
		this.propHandler = propHandler;
	}

	/**
	 * Method that accepts a list of cucumber json files and converts them to a
	 * {@link CukeTestRun} instance
	 * 
	 * @param jsonFiles The {@link List} of cucumber json {@link File}s
	 * @return a {@link CukeTestRun} object
	 */
	public CukeTestRun convertToTestRun(List<File> jsonFiles) {
		List<Feature> jsonFeatures = convertCucumberJsonFiles(jsonFiles);
		CukeTestRun testRun = CukeTestRun.builder().features(convertToCukeFeature(jsonFeatures)).build();
		testRun.calculateStartTime();
		testRun.calculateEndTime();
		
		return testRun;
	}

	/**
	 * Method that accepts a list of cucumber json files and parses the report to
	 * the corresponding objects
	 * 
	 * @param jsonFiles The {@link List} of cucumber json {@link File}s
	 * @return A {@link List} of {@link Feature} objects
	 */
	public List<Feature> convertCucumberJsonFiles(List<File> jsonFiles) {

		List<Feature> cucumberJsonFeatures = new ArrayList<>();

		for (File jsonFile : jsonFiles) {
			log.info("Processing json file: {}", jsonFile.getName());
			List<Feature> features = null;
			try {
				features = Arrays.asList(objectMapper.readValue(jsonFile, Feature[].class));
			} catch (IOException e) {
				if (propHandler.isFailOnConvertError()) {
					String msg = String.format("Failed to process json file: %s", jsonFile);
					log.error("{}", msg);
					throw new CukeConverterException(msg, e);
				} else {
					log.warn("Failed to process json file: {}. Ignoring error", jsonFile);
				}
			}
			Optional.ofNullable(features).ifPresent(fs -> cucumberJsonFeatures.addAll(fs));
		}
		return cucumberJsonFeatures;
	}

	/**
	 * Method that a accepts a list of cucumber report feature objects ( as they
	 * have been parsed by {@link #convertCucumberJsonFiles(List)} method, and
	 * converts them to a list of {@link CukeFeature} objects
	 * 
	 * @param jsonFeatures A {@link List} of {@link Feature} objects
	 * @return A {@link List} of {@link CukeFeature} objects
	 */
	public List<CukeFeature> convertToCukeFeature(List<Feature> jsonFeatures) {
		log.info("Starting transformation of features. Size: {}", jsonFeatures.size());

		List<CukeFeature> features = new ArrayList<>();
		for (Feature jsonFeature : jsonFeatures) {

			if (!isFeatureConvertable(jsonFeature)) {
				log.debug("Cannot convert feature. Skipping...");
				continue;
			}
			
			boolean mergeFeaturesWithSameName = propHandler.isMergeFeatures();

			Optional<CukeFeature> optionalFeature = features.stream()
					.filter(f -> f.getName().equals(jsonFeature.getName()))
					.findFirst();
			boolean createNewFeature = false;
			if (optionalFeature.isEmpty()) {
				createNewFeature = true;
			} else {
				if (!mergeFeaturesWithSameName) {
					createNewFeature = true;
				}
			}

			CukeFeature feature = null;
			if (createNewFeature) {
				feature = CukeFeature.builder().name(jsonFeature.getName()).description(jsonFeature.getDescription())
						.scenarios(new ArrayList<>())
						.tags(jsonFeature.getTags().stream().map(t -> t.getName()).collect(Collectors.toList()))
						.codeRef(jsonFeature.getUri()).build();
				features.add(feature);
			} else {
				feature = optionalFeature.get();
			}

			List<Element> featureElements = jsonFeature.getElements();
			for (int i = 0; i < featureElements.size();) {
				boolean isBackground = featureElements.get(i).getType().equalsIgnoreCase("background");
				boolean isPassed = true;

				CukeScenario scenario = new CukeScenario();
				scenario.setParent(feature);

				Element backgroundElement = null;
				if (isBackground) {
					backgroundElement = featureElements.get(i++);
				}
				Element scenarioElement = featureElements.get(i++);

				scenario.setStartTimestamp(scenarioElement.getStartTimestamp());
				scenario.setName(scenarioElement.getName());
				scenario.setLine(scenarioElement.getLine());

				scenario.setTags(scenarioElement.getTags().stream().map(t -> t.getName()).collect(Collectors.toList()));
				feature.getTotalTags().addAll(scenario.getTags());

				scenario.setType(scenarioElement.getKeyword().equalsIgnoreCase("scenario") ? CukeScenarioType.SCENARIO
						: CukeScenarioType.SCENARIO_OUTLINE);
				scenario.setDescription(scenarioElement.getDescription());

				List<CukeStep> beforeSteps = scenarioElement.getBefore().stream()
						.map(s -> convertStepFunction
								.apply(new ConvertStepContext(s, CukeStepSection.BEFORE_SCENARIO, scenario)))
						.collect(Collectors.toList());
				scenario.setBeforeSteps(beforeSteps);
				scenario.setBeforeStepsDuration(beforeSteps.stream().mapToLong(CukeStep::getDuration).sum());
				isPassed = isPassed && beforeSteps.stream().allMatch(s -> CukeStepResult.PASSED.equals(s.getResult()));

				if (isBackground) {
					List<CukeStep> backgroundSteps = backgroundElement.getSteps().stream()
							.map(s -> convertStepFunction
									.apply(new ConvertStepContext(s, CukeStepSection.BACKGROUND, scenario)))
							.collect(Collectors.toList());
					scenario.setBackgroundSteps(backgroundSteps);
					scenario.setBackgroundStepsDuration(backgroundSteps.stream().mapToLong(CukeStep::getDuration).sum());
					isPassed = isPassed
							&& backgroundSteps.stream().allMatch(s -> CukeStepResult.PASSED.equals(s.getResult()));
				}

				List<CukeStep> scenarioSteps = scenarioElement.getSteps().stream().map(
						s -> convertStepFunction.apply(new ConvertStepContext(s, CukeStepSection.SCENARIO, scenario)))
						.collect(Collectors.toList());
				scenario.setScenarioSteps(scenarioSteps);
				scenario.setScenarioStepsDuration(scenarioSteps.stream().mapToLong(CukeStep::getDuration).sum());

				isPassed = isPassed && scenarioSteps.stream().allMatch(s -> CukeStepResult.PASSED.equals(s.getResult()));

				List<CukeStep> afterSteps = scenarioElement.getAfter().stream()
						.map(s -> convertStepFunction
								.apply(new ConvertStepContext(s, CukeStepSection.AFTER_SCENARIO, scenario)))
						.collect(Collectors.toList());
				scenario.setAfterSteps(afterSteps);
				scenario.setAfterStepsDuration(afterSteps.stream().mapToLong(CukeStep::getDuration).sum());
				isPassed = isPassed && afterSteps.stream().allMatch(s -> CukeStepResult.PASSED.equals(s.getResult()));

				long totalDuration = scenario.getBeforeStepsDuration() + scenario.getAfterStepsDuration()
						+ scenario.getBackgroundStepsDuration() + scenario.getScenarioStepsDuration();
				scenario.setTotalDuration(totalDuration);
				scenario.setEndTimestamp(scenario.getStartTimestamp().plusNanos(totalDuration));
				if (isPassed) {
					scenario.setResult(CukeScenarioResult.PASSED);
					feature.increaseNumOfPassedScenarios();
				} else {
					scenario.setResult(CukeScenarioResult.FAILED);
					feature.increaseNumOfFailedScenarios();
				}
				scenario.countStepResults();

				feature.getScenarios().add(scenario);
				feature.setTotalDuration(feature.getTotalDuration() + scenario.getTotalDuration());
			}

			feature.increaseStepResultCounter();
		}

		log.debug("total number of features: {}", features.size());
		return features;
	}

	private Function<ConvertStepContext, CukeStep> convertStepFunction = (ConvertStepContext ctx) -> {
		Step s = ctx.getStep();
		CukeStepSection sSection = ctx.getCukeStepSection();
		CukeScenario parent = ctx.getCukeScenario();

		CukeStep step = new CukeStep();
		step.setParent(parent);
		step.setKeyword(s.getKeyword());
		step.setName(s.getName());
		step.setLine(s.getLine());
		step.setStepSection(sSection);
		Optional.ofNullable(s.getResult()).ifPresent(r -> step.setResult(CukeStepResult.fromText(r.getStatus())));
		Optional.ofNullable(s.getResult()).ifPresent(r -> step.setDuration(r.getDuration()));
		Optional.ofNullable(s.getResult()).ifPresent(r -> step.setErrorMessage(r.getErrorMessage()));
		Optional.ofNullable(s.getMatch()).ifPresent(m -> step.setMatch(CukeStepMatch.from(m)));
		if (s.getEmbeddings() != null && !s.getEmbeddings().isEmpty()) {
			step.setEmbeddings(s.getEmbeddings().stream().map(CukeEmbedding::from).collect(Collectors.toList()));
		}
		if (s.getRows() != null && !s.getRows().isEmpty()) {
			step.setTableData(s.getRows().stream().map(row -> {
				List<String> r = new ArrayList<>();
				r.addAll(row.getCells());
				return r;
			}).collect(Collectors.toList()));
		}
		Optional.ofNullable(s.getDocString()).ifPresent(docS -> step.setDocString(docS.getValue()));

		if (s.getBefore() != null) {
			List<CukeStep> beforeSteps = s.getBefore().stream()
					.map(ss -> this.convertStepFunction
							.apply(new ConvertStepContext(ss, CukeStepSection.BEFORE_STEP, parent)))
					.collect(Collectors.toList());
			step.setBeforeSteps(beforeSteps);
		}
		if (s.getAfter() != null) {
			List<CukeStep> afterSteps = s.getAfter().stream()
					.map(ss -> this.convertStepFunction
							.apply(new ConvertStepContext(ss, CukeStepSection.AFTER_STEP, parent)))
					.collect(Collectors.toList());
			step.setAfterSteps(afterSteps);
		}

		return step;
	};

	private boolean isFeatureConvertable(Feature jsonFeature) {
		boolean hasError = StringUtils.isBlank(jsonFeature.getName());
		
		if (hasError) {
			if (propHandler.isFailOnConvertError()) {
				String msg = String.format("Failed to convert feature. Name is not present");
				log.error("{}", msg);
				throw new CukeConverterException(msg);
			} else {
				return false;
			}
		}
		return true;
		
	}
	
	@RequiredArgsConstructor
	@Getter
	private class ConvertStepContext {
		private final Step step;
		private final CukeStepSection cukeStepSection;
		private final CukeScenario cukeScenario;
	}
}
