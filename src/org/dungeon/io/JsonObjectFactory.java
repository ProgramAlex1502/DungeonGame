package org.dungeon.io;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.eclipsesource.json.JsonObject;

public class JsonObjectFactory {
	
	private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	private static final String JSON_EXTENSION = ".json";
	
	@SuppressWarnings("deprecation")
	public static JsonObject makeJsonObject(String filename) {
		if (!filename.endsWith(JSON_EXTENSION)) {
			throw new IllegalFilenameExtensionException("filename must end with " + JSON_EXTENSION + ".");
		}
		Reader reader = new InputStreamReader(classLoader.getResourceAsStream(filename));
		try {
			return JsonObject.readFrom(reader);
		} catch (IOException fatal) {
			throw new RuntimeException(fatal);
		}
	}
	
	public static class IllegalFilenameExtensionException extends IllegalArgumentException {
		private static final long serialVersionUID = 1L;

		public IllegalFilenameExtensionException(String s) {
			super(s);
		}
	}

}
