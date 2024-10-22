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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import io.github.alexopa.cukereportconverter.exception.CukeConverterException;
import lombok.extern.slf4j.Slf4j;

/**
 * Class that is responsible to initialize the service properties. It also
 * provides methods to access the properties.
 */
@Slf4j
public class CukeConverterPropertyHandler {

	private static final String PROPERTIES_FILE = "cuke-report-converter.properties";

	private Properties props = new Properties();

	/**
	 * Creates a new {@link CukeConverterPropertyHandler} instance. The default
	 * properties file is used for initialization.
	 */
	public CukeConverterPropertyHandler() {
		initProperties(PROPERTIES_FILE);
	}

	/**
	 * Creates a new {@link CukeConverterPropertyHandler} instance.
	 * 
	 * @param propsFile A {@link String} with the properties file to be used for
	 *                  initialization
	 */
	public CukeConverterPropertyHandler(String propsFile) {
		initProperties(propsFile);
	}

	private void initProperties(String propsFile) {
		Properties propsFromFile = initPropsFromFile(propsFile);
		Properties propsFromSystemProperties = initPropsFromSystem();

		props = mergeProperties(propsFromFile, propsFromSystemProperties);
	}

	private Properties mergeProperties(Properties... properties) {
		return Stream.of(properties).collect(Properties::new, Map::putAll, Map::putAll);
	}

	private Properties initPropsFromSystem() {
		Properties systemProperties = System.getProperties();

		Properties overridesFromSystem = new Properties();
		for (CukeConverterProperties prop : CukeConverterProperties.values()) {
			Optional.ofNullable(systemProperties.getProperty(prop.getPropertyName()))
					.ifPresent(v -> overridesFromSystem.setProperty(prop.getPropertyName(), v));
		}

		return overridesFromSystem;
	}

	private Properties initPropsFromFile(String f) {

		Properties propsFromFile = new Properties();

		ClassLoader classLoader = getClass().getClassLoader();

		InputStream inStream = null;
		inStream = classLoader.getResourceAsStream(f);
		if (inStream == null) {
			log.warn("{} file is missing.", f);
			return propsFromFile;
		}

		URL url = classLoader.getResource(f);
		if (url != null) {
			File file = new File(url.getFile());
			FileInputStream propsInput = null;
			try {
				propsInput = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				throw new CukeConverterException(String.format("Failed to read properties file %s", f));
			}
			try {
				propsFromFile.load(propsInput);
			} catch (IOException e) {
				throw new CukeConverterException(String.format("Failed to process properties file %s", f));
			}
		}

		return propsFromFile;
	}

	/**
	 * Returns value of {@link CukeConverterProperties#CONVERTER_FAIL_ON_ERROR} property
	 * 
	 * @return a <code>boolean</code> with the value of
	 *         {@link CukeConverterProperties#CONVERTER_FAIL_ON_ERROR} property
	 */
	public boolean isFailOnConvertError() {
		return getPropertyAsBoolean(CukeConverterProperties.CONVERTER_FAIL_ON_ERROR);
	}

	/**
	 * Returns value of {@link CukeConverterProperties#CONVERTER_MERGE_FEATURES} property
	 * 
	 * @return a <code>boolean</code> with the value of
	 *         {@link CukeConverterProperties#CONVERTER_MERGE_FEATURES} property
	 */
	public boolean isMergeFeatures() {
		return getPropertyAsBoolean(CukeConverterProperties.CONVERTER_MERGE_FEATURES);
	}

	private boolean getPropertyAsBoolean(CukeConverterProperties prop) {
		String value = props.getProperty(prop.getPropertyName());
		return null != value ? Boolean.parseBoolean(value) : Boolean.parseBoolean(prop.getDefaultValue());
	}

}
