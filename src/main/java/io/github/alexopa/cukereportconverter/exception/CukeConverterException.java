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
package io.github.alexopa.cukereportconverter.exception;

/**
 * An exception that is thrown by the cuke-report-converter-service in case an error
 * happens
 */
public class CukeConverterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@link CukeConverterException}
	 * 
	 * @param msg A {@link String} with a message to add to the exception
	 */
	public CukeConverterException(String msg) {
		super(msg);
	}

	/**
	 * Creates a new {@link CukeConverterException}
	 * 
	 * @param t A {@link Throwable} to add to the exception
	 */
	public CukeConverterException(Throwable t) {
		super(t);
	}

	/**
	 * Creates a new {@link CukeConverterException}
	 * 
	 * @param msg A {@link String} with a message to add to the exception
	 * @param t   A {@link Throwable} to add to the exception
	 */
	public CukeConverterException(String msg, Throwable t) {
		super(msg, t);
	}
}
