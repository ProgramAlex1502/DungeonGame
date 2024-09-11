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

import org.dungeon.commands.IssuedCommand;
import org.dungeon.game.Game;
import org.dungeon.game.GameState;
import org.dungeon.util.Messenger;
import org.dungeon.util.StopWatch;
import org.dungeon.util.Table;

public final class Loader {
	
	private static final File SAVES_FOLDER = new File("saves/");
	
	private static final String SAVE_EXTENSION = ".dungeon";
	private static final String DEFAULT_SAVE_NAME = "default" + SAVE_EXTENSION;
	
	private static final String SAVE_CONFIRM = "Do you want to save the game?";
	private static final String LOAD_CONFIRM = "Do you want to load the game?";
	
	private static final SimpleDateFormat LAST_MODIFIED_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Loader() {
		throw new AssertionError();
	}
	
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
					table.insertRow(file.getName(), bytesToHuman(file.length()), LAST_MODIFIED_FORMAT.format(lastModified));
				}
				if (fileCount > 1) {
					table.insertSeparator();
					table.insertRow("Sum of these " + fileCount + " files", bytesToHuman(byteCount));
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
		File defaultSave = createFileFromName(DEFAULT_SAVE_NAME);
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
		GameState gameState = new GameState();
		IO.writeString("Created a new game.");
		IO.writeNewLine();
		IO.writeString(gameState.getPreface());
		Game.getGameWindow().requestFocusOnTextField();
		return gameState;
	}
	
	public static GameState loadGame() {
		if (checkForDefaultSave()) {
			if (confirmOperation(LOAD_CONFIRM)) {
				return loadFile(createFileFromName(DEFAULT_SAVE_NAME));
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
			File save = createFileFromName(argument);
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
		saveGame(gameState, null);
	}
	
	public static void saveGame(GameState gameState, IssuedCommand issuedCommand) {
		String saveName = DEFAULT_SAVE_NAME;
		if (issuedCommand != null && issuedCommand.hasArguments()) {
			saveName = issuedCommand.getFirstArgument();
		}
		if (saveFileDoesNotExist(saveName) || confirmOperation(SAVE_CONFIRM)) {
			saveFile(gameState, saveName);
		}
	}
	
	private static GameState loadFile(File file) {
		StopWatch stopWatch = new StopWatch();
		FileInputStream fileInStream;
		ObjectInputStream objectInStream;
		try {
			fileInStream = new FileInputStream(file);
			objectInStream = new ObjectInputStream(fileInStream);
			GameState loadedGameState = (GameState) objectInStream.readObject();
			objectInStream.close();
			loadedGameState.setSaved(true);
			String sizeString = bytesToHuman(file.length());
			DLogger.info(String.format("Loaded %s in %s.", sizeString, stopWatch.toString()));
			IO.writeString(String.format("Successfully loaded the game (read %s from %s).", sizeString, file.getName()));
			return loadedGameState;
		} catch (Exception bad) {
			IO.writeString("Could not load the saved game.");
			return null;
		}
	}
	
	private static boolean saveFileDoesNotExist(String name) {
		return !createFileFromName(name).exists();
	}
	
	private static File createFileFromName(String name) {
		return new File(SAVES_FOLDER, ensureSaveEndsWithExtension(name));
	}
	
	private static void saveFile(GameState state, String name) {
		StopWatch stopWatch = new StopWatch();
		File file = createFileFromName(name);
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
			String sizeString = bytesToHuman(file.length());
			DLogger.info(String.format("Saved %s in %s.", sizeString, stopWatch.toString()));
			IO.writeString(String.format("Successfully saved the game (wrote %s to %s).", sizeString, file.getName()));
		} catch (IOException bad) {
			IO.writeString("Could not save the game.");
		}
	}
	
	private static String bytesToHuman(long bytes) {
		if (bytes < 1024) {
			return bytes + " B";
		}
		
		int bitsUsed = 63 - Long.numberOfLeadingZeros(bytes);
		double significand = (double) bytes / (1L << (bitsUsed - bitsUsed % 10));
		char prefix = "kMGTPE".charAt(bitsUsed / 10 - 1);
		return String.format("%.1f %sB", significand, prefix);
	}

}
