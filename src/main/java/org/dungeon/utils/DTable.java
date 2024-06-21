package main.java.org.dungeon.utils;

import java.util.ArrayList;

import main.java.org.dungeon.io.DLogger;

public class DTable {
	
	private ArrayList<Column> columns;
	
	public DTable(String... headers) {
		columns = new ArrayList<Column>(headers.length);
		for (String header: headers) {
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
	
	private class Column {
		String header;
		int widestValue;
		ArrayList<String> rows;
		
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
