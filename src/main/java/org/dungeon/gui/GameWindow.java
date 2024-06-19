package main.java.org.dungeon.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

import main.java.org.dungeon.game.Game;
import main.java.org.dungeon.game.GameData;
import main.java.org.dungeon.io.DLogger;
import main.java.org.dungeon.io.Loader;
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
		
		textField.setBackground(Color.BLACK);
		textField.setForeground(Constants.FORE_COLOR_NORMAL);
		textField.setCaretColor(Color.WHITE);
		textField.setFont(GameData.monospaced);
		textField.setFocusTraversalKeysEnabled(false);
		
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				textFieldActionPerformed();
			}
		});
		
		textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				textFieldKeyPressed(e);
			}
		});
		
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		getContentPane().add(textField, BorderLayout.SOUTH);
		
		setTitle(Constants.NAME);
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				Game.exit();
			}
		});
		
		Action save = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (idle) {
					clearTextPane();
					Loader.saveGame(Game.getGameState());
				}
			}
		};
		
		textField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "SAVE");
		textField.getActionMap().put("SAVE", save);
		
		URL icon = Thread.currentThread().getContextClassLoader().getResource("icon.png");
		
		if (icon != null) {
			setIconImage(new ImageIcon(icon).getImage());
		} else {
			DLogger.warning("Could not find the icon.");
		}
		
		setResizable(false);
		resize();
	}
	
	private void setSystemLookAndFeel() {
		try {
			String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			if (lookAndFeel.equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} else {
				UIManager.setLookAndFeel(lookAndFeel);
			}
		} catch (UnsupportedLookAndFeelException ignored) {
		} catch (ClassNotFoundException ignored) {
		} catch (InstantiationException ignored) {
		} catch (IllegalAccessException ignored) {
		}
	}
	
	public void writeToTextPane(String string, Color color, long wait) {
		
	}

}
