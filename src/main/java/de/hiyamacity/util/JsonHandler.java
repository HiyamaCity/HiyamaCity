package de.hiyamacity.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

public class JsonHandler {

	@Getter
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static String getObjectAsJson(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T getJsonAsObject(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
