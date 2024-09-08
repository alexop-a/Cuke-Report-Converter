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
package io.github.alexopa.cukereportconverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.alexopa.cukereportconverter.config.CukeConverterProperties;
import io.github.alexopa.cukereportconverter.config.CukeConverterPropertyHandler;
import io.github.alexopa.cukereportconverter.exception.CukeConverterException;
import io.github.alexopa.cukereportconverter.model.cuke.CukeScenarioResult;
import io.github.alexopa.cukereportconverter.model.cuke.CukeTestRun;
import io.github.alexopa.cukereportconverter.model.cuke.CukeFeature;
import io.github.alexopa.cukereportconverter.model.cuke.CukeScenario;
import io.github.alexopa.cukereportconverter.model.jsonreport.Feature;
import io.github.alexopa.cukereportconverter.service.CukeConverter;

public class CukeConverterTest {

	private CukeConverter cukeConverter;
	
	private void resetPropertiesToDefault() {
		for (CukeConverterProperties prop: CukeConverterProperties.values()) {
			System.setProperty(prop.getPropertyName(), prop.getDefaultValue());
		}
	}
	
	
	@BeforeEach
	public void test_setup() {
		resetPropertiesToDefault();
	}
	
	@Test
	public void test_convert_to_test_run() {	
		cukeConverter = new CukeConverter(new CukeConverterPropertyHandler());
		
		final ClassLoader classLoader = getClass().getClassLoader();
		final String resourceName = "reportconvertservice/report_all_pass.json";
		final File file = new File(classLoader.getResource(resourceName).getFile());	
		
		CukeTestRun testRun = cukeConverter.convertToTestRun(Arrays.asList(file));
		List<CukeFeature> features = testRun.getFeatures();
		assertThat(features).hasSize(3);
		assertThat(testRun.getStartTime()).isEqualTo("2024-08-23T14:24:54.383");
		
		CukeFeature cartF = features.stream().filter(f -> f.getName().equals("Adding Items to Cart")).findFirst().get();
		assertThat(cartF.getScenarios()).hasSize(3);
		
		CukeFeature userAuthF = features.stream().filter(f -> f.getName().equals("User Authentication")).findFirst().get();
		assertThat(userAuthF.getScenarios()).hasSize(2);
		assertThat(userAuthF.getNumOfPassedScenarios()).isEqualTo(2);
		assertThat(userAuthF.getNumOfFailedScenarios()).isEqualTo(0);
		
		CukeScenario s = userAuthF.getScenarios().stream().filter(sc -> sc.getName().equals("Successful login")).findFirst().get();
		assertThat(s.getResult()).isEqualTo(CukeScenarioResult.PASSED);
		assertThat(s.getBeforeSteps()).hasSize(1);
		assertThat(s.getBackgroundSteps()).hasSize(0);
		assertThat(s.getScenarioSteps()).hasSize(4);
		assertThat(s.getAfterSteps()).hasSize(1);
		assertThat(s.getTags()).containsExactlyInAnyOrder("@ecommerce-test-suite", "@feature-user-auth", "@tcId-ec0005");
	}
	
	@Test
	public void test_conversion() {
		cukeConverter = new CukeConverter(new CukeConverterPropertyHandler());
		
		final ClassLoader classLoader = getClass().getClassLoader();
		final String resourceName = "reportconvertservice/report_all_pass.json";
		final File file = new File(classLoader.getResource(resourceName).getFile());	
		
		List<Feature> jsonFeatures = cukeConverter.convertCucumberJsonFiles(Arrays.asList(file));
		assertThat(jsonFeatures).hasSize(3);
		
		List<CukeFeature> features = cukeConverter.convertToCukeFeature(jsonFeatures);
		assertThat(features).hasSize(3);
		
	}
	
	@Test
	public void test_convert_merge_files_no_common_features() {	
		cukeConverter = new CukeConverter(new CukeConverterPropertyHandler());
		
		final ClassLoader classLoader = getClass().getClassLoader();
		final String resourceName1 = "reportconvertservice/merge-no-common-feature/report_feature_cart.json";
		final File file1 = new File(classLoader.getResource(resourceName1).getFile());
		
		final String resourceName2 = "reportconvertservice/merge-no-common-feature/report_feature_product.json";
		final File file2 = new File(classLoader.getResource(resourceName2).getFile());	
		
		CukeTestRun testRun = cukeConverter.convertToTestRun(Arrays.asList(file1, file2));
		List<CukeFeature> features = testRun.getFeatures();
		assertThat(features).hasSize(2);

		CukeFeature cartF = features.stream().filter(f -> f.getName().equals("Adding Items to Cart")).findFirst().get();
		assertThat(cartF.getScenarios()).hasSize(3);
		assertThat(cartF.getNumOfPassedScenarios()).isEqualTo(0);
		assertThat(cartF.getNumOfFailedScenarios()).isEqualTo(3);
		
		CukeFeature productF = features.stream().filter(f -> f.getName().equals("Product Search")).findFirst().get();
		assertThat(productF.getScenarios()).hasSize(2);
		assertThat(productF.getNumOfPassedScenarios()).isEqualTo(2);
		assertThat(productF.getNumOfFailedScenarios()).isEqualTo(0);
	}
	
	@Test
	public void test_convert_common_features_merge() {
		System.setProperty(CukeConverterProperties.CONVERTER_MERGE_FEATURES.getPropertyName(), "false");
		cukeConverter = new CukeConverter(new CukeConverterPropertyHandler());
		
		final ClassLoader classLoader = getClass().getClassLoader();
		final String resourceName1 = "reportconvertservice/merge-common-feature/report_feature_product_api.json";
		final File file1 = new File(classLoader.getResource(resourceName1).getFile());
		
		final String resourceName2 = "reportconvertservice/merge-common-feature/report_feature_product_ui.json";
		final File file2 = new File(classLoader.getResource(resourceName2).getFile());	
		
		CukeTestRun testRun = cukeConverter.convertToTestRun(Arrays.asList(file1, file2));
		List<CukeFeature> features = testRun.getFeatures();
		assertThat(features).hasSize(2);

		CukeFeature productF = features.stream().filter(f -> f.getName().equals("Product Search"))
				.filter(f -> f.getTags().contains("@api-test"))
				.findFirst().get();
		assertThat(productF.getScenarios()).hasSize(1);
		assertThat(productF.getNumOfPassedScenarios()).isEqualTo(1);
		assertThat(productF.getNumOfFailedScenarios()).isEqualTo(0);
		
		productF = features.stream().filter(f -> f.getName().equals("Product Search"))
				.filter(f -> f.getTags().contains("@ui-test"))
				.findFirst().get();
		assertThat(productF.getScenarios()).hasSize(2);
		assertThat(productF.getNumOfPassedScenarios()).isEqualTo(2);
		assertThat(productF.getNumOfFailedScenarios()).isEqualTo(0);
		
	}
	
	@Test
	public void test_convert_report_with_wrong_format_fail_on_error_false() {	
		System.setProperty(CukeConverterProperties.CONVERTER_FAIL_ON_ERROR.getPropertyName(), "false");
		cukeConverter = new CukeConverter(new CukeConverterPropertyHandler());

		
		final ClassLoader classLoader = getClass().getClassLoader();
		final String resourceName1 = "reportconvertservice/report_with_wrong_format.json";
		final File file1 = new File(classLoader.getResource(resourceName1).getFile());
		
		CukeTestRun testRun = cukeConverter.convertToTestRun(Arrays.asList(file1));
		List<CukeFeature> features = testRun.getFeatures();
		assertThat(features).hasSize(0);

	}

	@Test
	public void test_convert_report_with_wrong_format_fail_on_error_true() {
		System.setProperty(CukeConverterProperties.CONVERTER_FAIL_ON_ERROR.getPropertyName(), "true");
		cukeConverter = new CukeConverter(new CukeConverterPropertyHandler());

		final ClassLoader classLoader = getClass().getClassLoader();
		final String resourceName1 = "reportconvertservice/report_with_wrong_format.json";
		final File file1 = new File(classLoader.getResource(resourceName1).getFile());

		assertThatThrownBy(() -> {
			cukeConverter.convertToTestRun(Arrays.asList(file1));
		}).isInstanceOf(CukeConverterException.class);
	}
	
	@Test
	public void test_convert_report_with_wrong_feature_element_fail_on_error_false() {	
		System.setProperty(CukeConverterProperties.CONVERTER_FAIL_ON_ERROR.getPropertyName(), "false");
		cukeConverter = new CukeConverter(new CukeConverterPropertyHandler());

		
		final ClassLoader classLoader = getClass().getClassLoader();
		final String resourceName1 = "reportconvertservice/report_with_wrong_feature_element.json";
		final File file1 = new File(classLoader.getResource(resourceName1).getFile());
		
		CukeTestRun testRun = cukeConverter.convertToTestRun(Arrays.asList(file1));
		List<CukeFeature> features = testRun.getFeatures();
		assertThat(features).hasSize(2);
	}
	
	@Test
	public void test_convert_report_with_wrong_feature_element_fail_on_error_true() {	
		System.setProperty(CukeConverterProperties.CONVERTER_FAIL_ON_ERROR.getPropertyName(), "true");
		cukeConverter = new CukeConverter(new CukeConverterPropertyHandler());

		
		final ClassLoader classLoader = getClass().getClassLoader();
		final String resourceName1 = "reportconvertservice/report_with_wrong_feature_element.json";
		final File file1 = new File(classLoader.getResource(resourceName1).getFile());
		
		assertThatThrownBy(() -> {
			cukeConverter.convertToTestRun(Arrays.asList(file1));
		}).isInstanceOf(CukeConverterException.class).hasMessage("Failed to convert feature. Name is not present");		

	}
}
