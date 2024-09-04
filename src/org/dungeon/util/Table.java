package org.dungeon.util;

import java.util.ArrayList;
import java.util.Arrays;

import org.dungeon.io.DLogger;
import org.dungeon.io.IO;

public class Table {
	
	private static final char HORIZONTAL_BAR = '-';
	private static final char VERTICAL_BAR = '|';
	
	private final ArrayList<Column> columns;
	
	private CounterMap<Integer> separators;
	
	public Table(String... headers) {
		columns = new ArrayList<Column>(headers.length);
		for (String header : headers) {
			columns.add(new Column(header));
		}
	}
	
	public void insertRow(String... values) {
		int columnCount = columns.size();
		if (values.length <= columnCount) {
			for (int i = 0; i < columnCount; i++) {
				if (i < values.length) {
					columns.get(i).insertValue(values[i]);
				} else {
					columns.get(i).insertValue("");
				}
			}
		} else {
			DLogger.warning("Tried to insert more values than columns!");
		}
	}
	
	public void insertSeparator() {
		if (separators == null) {
			separators = new CounterMap<Integer>();
		}
		separators.incrementCounter(getDimensions().get(0));
	}
	
	public boolean contains(String value) {
		for (Column column : columns) {
			if (column.contains(value)) {
				return true;
			}
		}
		return false;
	}
	
	public Dimensions getDimensions() {
		int columnCount = columns.size();
		if (columnCount != 0) {
			return new Dimensions(columns.get(0).size(), columnCount);
		} else {
			return new Dimensions(0, 0);
		}
	}
	
	public void print() {
		if (columns.size() == 0) {
			DLogger.warning("Tried to print an empty Table.");
			return;
		}
		
		int columnCount = columns.size();
		int columnWidth = (Constants.COLS - columnCount + 1) / columnCount;
		
		int rowCount = columns.get(0).rows.size();
		
		StringBuilder builder = new StringBuilder(Constants.COLS * rowCount + 16);
		String[] currentRow = new String[columnCount];
		
		for (int i = 0; i < columnCount; i++) {
			currentRow[i] = columns.get(i).header;
		}
		appendRow(builder, columnWidth, currentRow);
		
		appendHorizontalSeparator(builder, columnWidth, columnCount);

		for (int rowIndex = 0; rowIndex < rowCount + 1; rowIndex++) {
			if (separators != null) {
				for (int remaining = separators.getCounter(rowIndex); remaining > 0; remaining--) {
					appendHorizontalSeparator(builder, columnWidth, columnCount);
				}
			}
			if (rowIndex != rowCount) {
				for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
					currentRow[columnIndex] = columns.get(columnIndex).rows.get(rowIndex);
				}				
				appendRow(builder, columnWidth, currentRow);
			}
		}
		
		IO.writeString(builder.toString());
	}
	
	private void appendRow(StringBuilder stringBuilder, int columnWidth, String... values) {
		String currentValue;
		for (int i = 0; i < values.length; i++) {
			currentValue = values[i];
			if (currentValue.length() > columnWidth) {
				stringBuilder.append(currentValue.substring(0, columnWidth - 3)).append("...");
			} else {
				stringBuilder.append(currentValue);
				int extraSpaces = columnWidth - currentValue.length();
				for (int j = 0; j < extraSpaces; j++) {
					stringBuilder.append(" ");
				}
			}
			if (i < values.length - 1) {
				stringBuilder.append(VERTICAL_BAR);
			}
		}
		stringBuilder.append('\n');
	}
	
	private void appendHorizontalSeparator(StringBuilder stringBuilder, int columnWidth, int columnCount) {
		String pseudoValue = Utils.makeRepeatedCharacterString(columnWidth, HORIZONTAL_BAR);
		String[] pseudoRow = new String[columnCount];
		Arrays.fill(pseudoRow, pseudoValue);
		appendRow(stringBuilder, columnWidth, pseudoRow);
	}
	
	private class Column {
		final String header;
		final ArrayList<String> rows;
		int widestValue;
		
		public Column(String header) {
			rows = new ArrayList<String>();
			this.header = header;
			widestValue = header.length();
		}
		
		void insertValue(String value) {
			if (value == null) {
				rows.add("");
			} else {
				rows.add(value);
				int length = value.length();
				if (length > widestValue) {
					widestValue = length;
				}
			}
		}
		
		boolean contains(String value) {
			return rows.contains(value);
		}
		
		int size() {
			return rows.size();
		}
	}

}
