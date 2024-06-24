package main.java.org.dungeon.game;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

import main.java.org.dungeon.io.DLogger;

class DBufferedReader implements Closeable {
	
	private String line;
	private boolean continued;
	
	private static final char LINE_BREAK = '\\';
	
	private final BufferedReader br;
	
	public DBufferedReader(Reader in) {
		br = new BufferedReader(in);
	}
	
	private void readLine() {
		try {
			line = br.readLine();
			if (line != null) {
				continued = isContinued();
				if (continued) {
					line = line.substring(0, line.length() - 1);
				}
				line = line.trim();
			} else {
				continued = false;
			}
		} catch (IOException e) {
			DLogger.warning(e.getMessage());
		}
	}
	
	private boolean isContinued() {
		return !line.isEmpty() && line.charAt(line.length() - 1) == LINE_BREAK;
	}
	
	public String readString() {
		readLine();
		if (continued) {
			StringBuilder sb = new StringBuilder(line);
			while (continued) {
				readLine();
				if (line != null) {
					sb.append('\n').append(line);
				}
			}
			return sb.toString();
		}
		return line;
	}

	@Override
	public void close() throws IOException {
		br.close();
	}

}
