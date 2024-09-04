package org.dungeon.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.dungeon.game.Game;
import org.dungeon.game.GameData;
import org.dungeon.game.GameState;
import org.dungeon.game.IssuedCommand;
import org.dungeon.io.Loader;
import org.dungeon.util.CommandHistory;
import org.dungeon.util.Constants;

public class GameWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static final int ROWS = 30;

	private static final int MARGIN = 5;
	private final SimpleAttributeSet attributeSet = new SimpleAttributeSet();
	private final StyledDocument document;
	
	private JTextField textField;
	private JTextPane textPane;
	
	private boolean idle;
	
	public GameWindow() {
		initComponents();
		document = textPane.getStyledDocument();
		setVisible(true);
		setIdle(true);
	}
	
	private void initComponents() {
		setSystemLookAndFeel();
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(SharedConstants.MARGIN_COLOR);
		
		textPane = new JTextPane();
		textField = new JTextField();
		
		JScrollPane scrollPane = new JScrollPane();
		
		textPane.setEditable(false);
		textPane.setBackground(SharedConstants.INSIDE_COLOR);
		textPane.setFont(GameData.FONT);
		
		scrollPane.setViewportView(textPane);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getVerticalScrollBar().setUI(new DScrollBarUI());
		
		textField.setBackground(SharedConstants.INSIDE_COLOR);
		textField.setForeground(Constants.FORE_COLOR_NORMAL);
		textField.setCaretColor(Color.WHITE);
		textField.setFont(GameData.FONT);
		textField.setFocusTraversalKeysEnabled(false);
		textField.setBorder(BorderFactory.createEmptyBorder());
		
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				textFieldActionPerformed();
			}
		});
		
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				textFieldKeyPressed(e);
			}
		});
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(MARGIN, MARGIN, MARGIN, MARGIN);
		panel.add(scrollPane, c);
		
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, MARGIN, MARGIN, MARGIN);
		panel.add(textField, c);
		
		setTitle(Constants.NAME);
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				Game.exit();
			}
		});
		
		Action save = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (idle) {
					clearTextPane();
					Loader.saveGame(Game.getGameState());
				}
			}
		};
		
		textField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "SAVE");
		textField.getActionMap().put("SAVE", save);
		
		add(panel);
		
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
	
	private void resize() {
		textPane.setPreferredSize(calculateTextPaneSize());
		pack();
		setLocationRelativeTo(null);
	}
	
	private Dimension calculateTextPaneSize() {
		FontMetrics fontMetrics = getFontMetrics(GameData.FONT);
		int width = fontMetrics.charWidth(' ') * (Constants.COLS + 1);
		int height = fontMetrics.getHeight() * ROWS;
		return new Dimension(width, height);
	}
	
	private void textFieldActionPerformed() {
		final String text = getTrimmedTextFieldText();
		if (!text.isEmpty()) {
			clearTextField();
			setIdle(false);
			@SuppressWarnings("rawtypes")
			SwingWorker inputRenderer = new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() {
					Game.renderTurn(new IssuedCommand(text));
					Game.getGameState().getCommandHistory().getCursor().moveToEnd();
					return null;
				}
				
				@Override
				protected void done() {
					textField.requestFocusInWindow();
					setIdle(true);
				}
			};
			inputRenderer.execute();
		}
	}
	
	private void setIdle(boolean idle) {
		this.idle = idle;
		textField.setEnabled(idle);
	}
	
	private void textFieldKeyPressed(KeyEvent e) {
		if (idle) {
			GameState gameState = Game.getGameState();
			if (gameState != null) {
				CommandHistory commandHistory = Game.getGameState().getCommandHistory();
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					textField.setText(commandHistory.getCursor().moveUp().getSelectedCommand());
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					textField.setText(commandHistory.getCursor().moveDown().getSelectedCommand());
				} else if (e.getKeyCode() == KeyEvent.VK_TAB) {
					String lastSimilarCommand = commandHistory.getLastSimilarCommand(getTrimmedTextFieldText());
					if (lastSimilarCommand != null) {
						textField.setText(lastSimilarCommand);
					}
				}
			}
		}
	}
	
	private String getTrimmedTextFieldText() {
		return textField.getText().trim();
	}
	
	public void writeToTextPane(String string, Color color, boolean scrollDown) {
		writeToTextPane(string, color, textPane.getBackground(), scrollDown);
	}
	
	void writeToTextPane(String string, Color fore, Color back, boolean scrollDown) {
		StyleConstants.setForeground(attributeSet, fore);
		StyleConstants.setBackground(attributeSet, back);
		
		try {
			document.insertString(document.getLength(), string, attributeSet);
			if (scrollDown) {
				textPane.setCaretPosition(document.getLength());
			} else {
				textPane.setCaretPosition(0);
			}
		} catch (BadLocationException ignored) {
		}
	}
	
	public void clearTextPane() {
		try {
			document.remove(0, document.getLength());
		} catch (BadLocationException ignored) {
		}
	}
	
	public void requestFocusOnTextField()  {
		textField.requestFocusInWindow();
	}
	
	private void clearTextField() {
		textField.setText(null);
	}

}
