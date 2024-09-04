package org.dungeon.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

class ResourceParser implements Closeable {
	
	private final LineNumberReader reader;

	private ResourceLine line;
	
	
	public ResourceParser(Reader in) {
		reader = new LineNumberReader(in);
	}
	
	int getLineNumber() {
		return reader.getLineNumber();
	}

	private void readLine() {
		do {
			try {
				String text = reader.readLine();
				if (text == null) {
					line = null;
					return;
				}
				line = new ResourceLine(text);
			} catch (IOException e) {
				DLogger.warning(e.getMessage());
			}
		} while (!line.isValid() || line.isComment());
	}
	
	public String readString() {
		readLine();
		if (line == null) {
			return null;
		}
		if (line.isContinued()) {
			StringBuilder sb = new StringBuilder();
			sb.append(line.toString());
			while (line.isContinued()) {
				readLine();
				if (line != null) {
					sb.append(line.toString());
				} else {
					break;
				}
			}
			return sb.toString();
		}
		return line.toString();
	}

	@Override
	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
			DLogger.warning(e.getMessage());
		}
	}

}
