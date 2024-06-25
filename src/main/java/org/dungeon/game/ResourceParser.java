package main.java.org.dungeon.game;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

import main.java.org.dungeon.io.DLogger;

class ResourceParser implements Closeable {
	
	private final BufferedReader br;
	private ResourceLine line;

	public ResourceParser(Reader in) {
		br = new BufferedReader(in);
	}
	
	private void readLine() {
		do {
			try {
				String text = br.readLine();
				
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
			sb.append(line.toString()).append('\n');
			while (line.isContinued()) {
				if (line != null) {
					sb.append(line.toString()).append('\n');
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
			br.close();			
		} catch (IOException e) {
			DLogger.warning(e.getMessage());
		}
	}

}
