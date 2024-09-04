package org.dungeon.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import org.dungeon.game.Game;
import org.dungeon.game.GameState;
import org.dungeon.game.IssuedCommand;
import org.dungeon.util.Messenger;
import org.dungeon.util.Table;
import org.dungeon.util.Utils;

public class Loader {
	
	private static final File SAVES_FOLDER = new File("saves/");
	
	private static final String SAVE_EXTENSION = ".dungeon";
	private static final String DEFAULT_SAVE_NAME = "default" + SAVE_EXTENSION;
	
	private static final String SAVE_CONFIRM = "Do you want to save the game?";
	private static final String LOAD_CONFIRM = "Do you want to load the game?";
	
	private static final SimpleDateFormat LAST_MODIFIED_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void printFilesInSavesFolder() {
		File[] files = SAVES_FOLDER.listFiles();
		if (files != null) {
			if (files.length != 0) {
				Table table = new Table("Name", "Size", "Last modified");
				int fileCount = 0;
				int byteCount = 0;
				for (File file : files) {
					fileCount += 1;
					byteCount += file.length();
					Date lastModified = new Date(file.lastModified());
					table.insertRow(file.getName(), Utils.bytesToHuman(file.length()), LAST_MODIFIED_FORMAT.format(lastModified));
				}
				if (fileCount > 1) {
					table.insertSeparator();
					table.insertRow("Sum of these " + fileCount + " files", Utils.bytesToHuman(byteCount));
				}
				table.print();
			} else {
				IO.writeString("Saves folder is empty.");
			}
		} else {
			IO.writeString("Saves folder does not exist.");
		}
	}
	
	private static boolean isSaveFile(File file) {
		return file != null && file.getName().endsWith(SAVE_EXTENSION) && file.isFile();
	}
	
	private static boolean checkForDefaultSave() {
		File defaultSave = new File(SAVES_FOLDER, DEFAULT_SAVE_NAME);
		return isSaveFile(defaultSave);
	}
	
	public static boolean checkForAnySave() {
		File[] files = SAVES_FOLDER.listFiles();
		if (files != null) {
			for (File file : files) {
				if (isSaveFile(file)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean confirmOperation(String confirmation) {
		int result = JOptionPane.showConfirmDialog(Game.getGameWindow(), confirmation, null, JOptionPane.YES_NO_OPTION);
		Game.getGameWindow().requestFocusOnTextField();
		return result == JOptionPane.YES_OPTION;
	}
	
	private static String ensureSaveEndsWithExtension(String name) {
		return name.endsWith(SAVE_EXTENSION) ? name : name + SAVE_EXTENSION;
	}
	
	public static GameState newGame() {
		IO.writeString("Created a new game.");
		return new GameState();
	}
	
	public static GameState loadGame() {
		if (checkForDefaultSave()) {
			if (confirmOperation(LOAD_CONFIRM)) {
				return loadFile(new File(SAVES_FOLDER, DEFAULT_SAVE_NAME));
			}
		} else if (checkForAnySave()) {
			if (confirmOperation(LOAD_CONFIRM)) {
				File[] files = SAVES_FOLDER.listFiles();
				if (files != null) {
					File firstListedFile = files[0];
					return loadFile(firstListedFile);
				}
			}
		}
		
		return null;
	}
	
	public static GameState loadGame(IssuedCommand issuedCommand) {
		if (issuedCommand == null) {
			DLogger.warning("Passed null to Loader.loadGame(IssuedCommand)!");
			return null;
		}
		if (issuedCommand.hasArguments()) {
			String argument = issuedCommand.getFirstArgument();
			argument = ensureSaveEndsWithExtension(argument);
			File save = new File(SAVES_FOLDER, argument);
			if (isSaveFile(save)) {
				return loadFile(save);
			} else {
				IO.writeString(save.getName() + " does not exist or is not a file.");
				return null;
			}
		} else {
			return loadGame();
		}
	}
	
	public static void saveGame(GameState gameState) {
		if (confirmOperation(SAVE_CONFIRM)) {
			saveFile(gameState, DEFAULT_SAVE_NAME);
		}
	}
	
	public static void saveGame(GameState gameState, IssuedCommand issuedCommand) {
		if (issuedCommand.hasArguments()) {
			if (confirmOperation(SAVE_CONFIRM)) {
				saveFile(gameState, issuedCommand.getFirstArgument());
			}
		} else {
			saveGame(gameState);
		}
	}
	
	private static GameState loadFile(File file) {
		FileInputStream fileInStream;
		ObjectInputStream objectInStream;
		try {
			fileInStream = new FileInputStream(file);
			objectInStream = new ObjectInputStream(fileInStream);
			GameState loadedGameState = (GameState) objectInStream.readObject();
			objectInStream.close();
			String formatString = "Successfully loaded the game (read %s from %s).";
			IO.writeString(String.format(formatString, Utils.bytesToHuman(file.length()), file.getName()));
			return loadedGameState;
		} catch (Exception bad) {
			IO.writeString("Could not load the saved game.");
			return null;
		}
	}
	
	private static void saveFile(GameState state, String name) {
		name = ensureSaveEndsWithExtension(name);
		File file = new File(SAVES_FOLDER, name);
		FileOutputStream fileOutStream;
		ObjectOutputStream objectOutStream;
		try {
			if (!SAVES_FOLDER.exists()) {
				if (!SAVES_FOLDER.mkdir()) {
					Messenger.printFailedToCreateDirectoryMessage(SAVES_FOLDER.getName());
					return;
				}
			}
			fileOutStream = new FileOutputStream(file);
			objectOutStream = new ObjectOutputStream(fileOutStream);
			objectOutStream.writeObject(state);
			objectOutStream.close();
			state.setSaved(true);
			long bytes = file.length();
			String formatString = "Successfully saved the game (wrote %s to %s).";
			IO.writeString(String.format(formatString, Utils.bytesToHuman(bytes), file.getName()));
		} catch (IOException bad) {
			IO.writeString("Could not save the game.");
		}
	}

}
