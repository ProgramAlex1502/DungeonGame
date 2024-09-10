package org.dungeon.commands;

import java.io.Serializable;

import org.dungeon.util.CircularList;
import org.dungeon.util.Utils;

public class CommandHistory implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final int HISTORY_MAXIMUM_SIZE = 200;
	private final CircularList<String> commands;
	private transient Cursor cursor;
	
	public CommandHistory() {
		commands = new CircularList<String>(HISTORY_MAXIMUM_SIZE);
		cursor = new Cursor(this);
	}
	
	public Cursor getCursor() {
		if (cursor == null) {
			cursor = new Cursor(this);
		}
		return cursor;
	}
	
	private int size() {
		return commands.size();
	}
	
	private boolean isEmpty() {
		return commands.isEmpty();
	}
	
	public void addCommand(IssuedCommand issuedCommand) {
		commands.add(issuedCommand.getStringRepresentation());
		getCursor().moveToEnd();
	}
	
	public String getLastSimilarCommand(String command) {
		for (int i = commands.size() - 1; i >= 0; i--) {
			if (Utils.startsWithIgnoreCase(commands.get(i), command)) {
				return commands.get(i);
			}
		}
		return null;
	}
	
	public class Cursor implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private final CommandHistory history;
		private int index;
		
		Cursor(CommandHistory history) {
			this.history = history;
			moveToEnd();
		}
		
		public String getSelectedCommand() {
			if (!history.isEmpty() && index < history.size()) {
				return history.commands.get(index);
			} else {
				return null;
			}
		}
		
		public Cursor moveUp() {
			if (index != 0) {
				index--;
			}
			return this;
		}
		
		public Cursor moveDown() {
			if (index < history.size()) {
				index++;
			}
			return this;
		}
		
		public Cursor moveToEnd() {
			index = history.size();
			return this;
		}
		
	}

}
