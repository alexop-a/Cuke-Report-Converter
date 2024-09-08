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

import java.util.Base64;

import io.github.alexopa.cukereportconverter.model.jsonreport.Embedding;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class that models an embedded element to a cucumber report
 */
@Data
@NoArgsConstructor
public class CukeEmbedding {

	private String data;
	private String mimeType;
	private String name;

	/**
	 * Method that gets the element's data as a decoded {@link String}
	 * 
	 * @return A {@link String} with the element's data decoded
	 */
	public String getDataAsString() {
		return new String(Base64.getDecoder().decode(data.getBytes()));
	}

	/**
	 * Method that creates an {@link CukeEmbedding} object from an {@link Embedding}
	 * object
	 * 
	 * @param jsonEmbedding The {@link Embedding} instance to convert
	 * @return A new {@link CukeEmbedding} object from the given {@link Embedding}
	 *         object
	 */
	public static CukeEmbedding from(Embedding jsonEmbedding) {
		CukeEmbedding embedding = new CukeEmbedding();
		embedding.setData(jsonEmbedding.getData());
		embedding.setMimeType(jsonEmbedding.getMimeType());
		embedding.setName(jsonEmbedding.getName());

		return embedding;
	}

	@Override
	public String toString() {
		return "Embedding [mimeType=" + mimeType + ", name=" + name + "]";
	}

}
