package org.dungeon.map;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.dungeon.game.Game;
import org.dungeon.gui.SharedConstants;

public final class WorldMapWriter {
	
	private WorldMapWriter() {
		throw new AssertionError();
	}
	
	public static void writeMap(WorldMap map) {
		Document document = new DefaultStyledDocument();
		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
		StyleConstants.setBackground(attributeSet, SharedConstants.getInsideColor());
		for (WorldMapSymbol[] line : map.getSymbolMatrix()) {
			for (WorldMapSymbol symbol : line) {
				StyleConstants.setForeground(attributeSet, symbol.getColor());
				appendToDocument(document, symbol.getCharacterAsString(), attributeSet);
			}
			appendToDocument(document, "\n", attributeSet);
		}
		Game.getGameWindow().writeMapToTextPane(document);
	}
	
	private static void appendToDocument(Document document, String string, AttributeSet attributeSet) {
		try {
			document.insertString(document.getLength(), string, attributeSet);
		} catch (BadLocationException fatal) {
			throw new RuntimeException(fatal);
		}
	}

}
