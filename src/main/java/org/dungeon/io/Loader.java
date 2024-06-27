package main.java.org.dungeon.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

import main.java.org.dungeon.game.Game;
import main.java.org.dungeon.game.GameState;
import main.java.org.dungeon.game.IssuedCommand;
import main.java.org.dungeon.util.Constants;
import main.java.org.dungeon.util.Table;
import main.java.org.dungeon.util.Utils;

public class Loader {
	
	private static final String DEFAULT_SAVE_PATH = "saves/";
	
	private static final File SAVES_FOLDER = new File(DEFAULT_SAVE_PATH);
	private static final String DEFAULT_SAVE_NAME = "default";
	private static final String SAVE_EXTENSION = ".dungeon";
	
	private static final String SAVE_ERROR = "Could not save the game.";
	private static final String SAVE_SUCCESS = "Successfully saved the game.";
	private static final String SAVE_CONFIRM = "Do you want to save the game?";
	
	private static final String LOAD_ERROR = "Could not load the saved game.";
	private static final String LOAD_SUCCESS = "Successfully loaded the game.";
	private static final String LOAD_CONFIRM = "Do you want to load the game?";
		
	private static boolean checkForDefaultSave() {
		File savedCampaign = new File(SAVES_FOLDER, DEFAULT_SAVE_NAME + SAVE_EXTENSION);
		return savedCampaign.exists() && savedCampaign.isFile();
	}
	
	public static void printFilesInSavesFolder() {
		File[] files = SAVES_FOLDER.listFiles();
		
		if (files != null) {
			if (files.length != 0) {
				Table table = new Table("Name", "Size");
				int fileCount = 0;
				int byteCount = 0;
				for (File file : files) {
					fileCount += 1;
					byteCount += file.length();
					table.insertRow(file.getName(), Utils.bytesToHuman(file.length()));
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
	
	public static GameState loadGame(IssuedCommand issuedCommand) {
		if (issuedCommand != null && issuedCommand.hasArguments()) {
			File save;
			
			if (issuedCommand.getFirstArgument().contains(SAVE_EXTENSION)) {
				save = new File(SAVES_FOLDER, issuedCommand.getFirstArgument());
			} else {
				save = new File(SAVES_FOLDER, issuedCommand.getFirstArgument() + SAVE_EXTENSION);
			}
			
			if (save.exists() && save.isFile()) {
				return loadFile(save);
			} else {
				IO.writeString(save.getName() + " does not exist or is not a file.");
				return null;
			}
		} else {
			if (checkForDefaultSave()) {
				IO.writeString(Constants.FILE_FOUND);
				if (confirmOperation(LOAD_CONFIRM)) {
					return loadFile(new File(SAVES_FOLDER, DEFAULT_SAVE_NAME + SAVE_EXTENSION));
				}
				
				return new GameState();
			} else {
				GameState newGameState = new GameState();
				saveFile(newGameState, DEFAULT_SAVE_NAME, true);
				return newGameState;
			}
		}
	}
	
	public static void saveGame(GameState gameState) {
		if (confirmOperation(SAVE_CONFIRM)) {
			saveFile(gameState, DEFAULT_SAVE_NAME, false);
		}
	}
	
	public static void saveGame(GameState gameState, IssuedCommand issuedCommand) {
		if (issuedCommand.hasArguments()) {
			if (confirmOperation(SAVE_CONFIRM)) {
				saveFile(gameState, issuedCommand.getFirstArgument(), false);
			}
		} else {
			saveGame(gameState);
		}
	}
	
	private static boolean confirmOperation(String confirmation) {
		int result = JOptionPane.showConfirmDialog(Game.getGameWindow(), confirmation, null, JOptionPane.YES_NO_OPTION);
		Game.getGameWindow().requestFocusOnTextField();
		return result == JOptionPane.YES_OPTION;
	}
	
	@SuppressWarnings("resource")
	private static GameState loadFile(File file) {
		FileInputStream fileInStream;
		ObjectInputStream objectInStream;
		
		try {
			fileInStream = new FileInputStream(file);
			objectInStream = new ObjectInputStream(fileInStream);
			GameState loadedGameState = (GameState) objectInStream.readObject();
			objectInStream.close();
			IO.writeString(LOAD_SUCCESS);
			IO.writeString("Read " + Utils.bytesToHuman(file.length()) + " from " + file.getName());
			loadedGameState.setSaved(true);
			return loadedGameState;
		} catch (IOException ex) {
			IO.writeString(LOAD_ERROR);
			IO.writeString(ex.toString());
			return new GameState();
		} catch (ClassNotFoundException ex) {
			IO.writeString(LOAD_ERROR);
			IO.writeString(ex.toString());
			return new GameState();
		}
	}
	
	private static void saveFile(GameState state, String name, boolean quiet) {
		File file = new File(SAVES_FOLDER, name + SAVE_EXTENSION);
		FileOutputStream fileOutStream;
		ObjectOutputStream objectOutStream;
		
		try {
			if (!SAVES_FOLDER.exists()) {
				if (!SAVES_FOLDER.mkdir()) {
					Utils.printFailedToCreateDirectoryMessage(DEFAULT_SAVE_PATH);
					return;
				}
			}
			
			fileOutStream = new FileOutputStream(file);
			objectOutStream = new ObjectOutputStream(fileOutStream);
			objectOutStream.writeObject(state);
			objectOutStream.close();
			state.setSaved(true);
			
			if (!quiet) {
				IO.writeString(SAVE_SUCCESS);
				long bytes = file.length();
				IO.writeString("Wrote " + Utils.bytesToHuman(bytes) + " to " + file.getName());
			}
		} catch(IOException ex) {
			if (!quiet) {
				IO.writeString(SAVE_ERROR);
				IO.writeString(ex.toString());
				
			}
		}
	}

}
