package main.java.org.dungeon.io;

final class ResourceLine {
	
	private static final char LINE_BREAK = '\\';
	private static final String[] COMMENT_ESCAPES = {"//", "#"};
	
	private final String text;
	private final boolean valid;
	
	private String returnText;
	
	public ResourceLine(String line) {
		if (line == null) {
			DLogger.warning("Tried to create a ResourceLine with a null String.");
			this.text = null;
			valid = false;
		} else {
			this.text = line.trim();
			valid = !this.text.isEmpty();
		}
	}
	
	boolean isValid() {
		return valid;
	}
	
	boolean isContinued() {
		return valid && text.charAt(text.length() - 1) == LINE_BREAK;
	}
	
	int countLineBreaks() {
		int count = 0;
		for (int i = text.length() - 1; i >= 0; i--) {
			if (text.charAt(i) == LINE_BREAK) {
				count++;
			} else {
				break;
			}
		}
		
		return count;
	}
	
	boolean isComment() {
		if (valid) {
			for (String escape : COMMENT_ESCAPES) {
				if (text.startsWith(escape)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public String toString() {
		if (isComment()) {
			return null;
		}
		if (returnText == null) {
			makeReturnText();
		}
		return returnText;
		
	}
	
	private void makeReturnText() {
		returnText = text;
		int lineBreakCount = countLineBreaks();
		
		if (isContinued()) {
			returnText = text.substring(0, text.length() - lineBreakCount);
		}
		
		if (!returnText.isEmpty() && Character.isWhitespace(returnText.charAt(returnText.length() - 1))) {
			int indexOfFirstTrailingWhitespace = returnText.length() - 1;
			
			while (indexOfFirstTrailingWhitespace > 0) {
				if (Character.isWhitespace(returnText.charAt(indexOfFirstTrailingWhitespace - 1))) {
					indexOfFirstTrailingWhitespace--;
				} else {
					break;
				}
			}
			
			returnText = returnText.substring(0, indexOfFirstTrailingWhitespace);
		}
		
		if (lineBreakCount == 1) {
			returnText += ' ';
		} else if (lineBreakCount > 1) {
			returnText += '\n';
		}
	}

}
