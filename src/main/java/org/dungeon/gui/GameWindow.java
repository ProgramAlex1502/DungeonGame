package main.java.org.dungeon.gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

import main.java.org.dungeon.game.GameData;
import main.java.org.dungeon.utils.Constants;

public class GameWindow extends JFrame{
	private static final long serialVersionUID = 1L;

	//TODO: finish GameWindow class
	
	private final SimpleAttributeSet attributeSet = new SimpleAttributeSet();
	private StyledDocument document;
	
	private JTextField textField;
	private JTextPane textPane;
	
	private int rows = Constants.ROWS;
	
	private boolean idle;
	
	public GameWindow() {
		initComponents();
		document = textPane.getStyledDocument();
		setVisible(true);
		setIdle(true);
	}
	
	private void initComponents() {
		setSystemLookAndFeel();
		
		textPane = new JTextPane();
		textField = new JTextField();
		
		JScrollPane scrollPane = new JScrollPane();
		
		textPane.setEditable(false);
		textPane.setBackground(Constants.DEFAULT_BACK_COLOR);
		textPane.setFont(GameData.monospaced);
		
		scrollPane.setViewportView(textPane);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	}
	
	public void writeToTextPane(String string, Color color, long wait) {
		
	}

}
