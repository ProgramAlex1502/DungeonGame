package main.java.org.dungeon.help;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import main.java.org.dungeon.game.IssuedCommand;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.utils.Utils;

public class Help {
	
	private static final List<CommandHelp> COMMANDS = new ArrayList<CommandHelp>();
	private static final List<AspectHelp> ASPECTS = new ArrayList<AspectHelp>();
	private static boolean initialized;
	
	public static void initialize() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		initCommandHelp(classLoader);
		initAspectHelp(classLoader);
		initialized = true;
	}
	
	public static boolean isInitialized() {
		return initialized;
	}
	
	private static void initCommandHelp(ClassLoader classLoader) {
		BufferedReader bufferedReader;
		
		try {
			InputStream inputStream = classLoader.getResourceAsStream(HelpConstants.COMMAND_TXT_PATH);
			System.out.print(inputStream);
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			
			CommandHelpBuilder commandBuilder = null;
			String line;
			
			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				if(Utils.isNotBlankString(line)) {
					if (line.charAt(0) == '#') {
						continue;
					}
					
					if (line.startsWith("COMMAND:")) {
						line = line.substring(line.indexOf(':') + 1);
						line = line.trim().replace("\"", "");
						if (!line.isEmpty()) {
							if (commandBuilder != null) {
								COMMANDS.add(commandBuilder.createCommandHelp());
							}
							commandBuilder = new CommandHelpBuilder();
							commandBuilder.setName(line);
						} else {
							throw new IllegalHelpFormatException("Missing string after ':' operand.");
						}
					} else if (line.startsWith("INFO:")) {
						line = line.substring(line.indexOf(':') + 1);
						line = line.trim().replace("\"", "");
						if (!line.isEmpty()) {
							if (commandBuilder != null) {
								commandBuilder.setInfo(line);
							}
						} else {
							throw new IllegalHelpFormatException("Missing string after ':' operand.");
						}
					}  else if (line.startsWith("ALIASES:")) {
                        line = line.substring(line.indexOf(':') + 1);
                        if (commandBuilder != null) {
                            if (!line.isEmpty()) {
                                commandBuilder.setAliases(parseStringArray(line));
                            } else {
                                throw new IllegalHelpFormatException("Missing string after ':' operand.");
                            }
                        }
                    } else if (line.startsWith("ARGUMENTS:")) {
                        line = line.substring(line.indexOf(':') + 1);
                        if (commandBuilder != null) {
                            if (!line.isEmpty()) {
                                commandBuilder.setArguments(parseStringArray(line));
                            } else {
                                throw new IllegalHelpFormatException("Missing string after ':' operand.");
                            }
                        }
                    }
				}
			}
			
			if (commandBuilder != null) {
				COMMANDS.add(commandBuilder.createCommandHelp());
			}
		} catch (IOException ignored) {
		}
	}
	
	private static void initAspectHelp(ClassLoader classLoader) {
        BufferedReader bufferedReader;
        try {
            InputStream inputStream = classLoader.getResourceAsStream(HelpConstants.ASPECT_TXT_PATH);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            AspectHelpBuilder aspectHelpBuilder = null;
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (Utils.isNotBlankString(line)) {
                    if (line.charAt(0) == '#') {
                        continue;
                    }
                    if (line.startsWith("ASPECT:")) {
                        line = line.substring(line.indexOf(':') + 1);
                        line = line.trim().replace("\"", "");
                        if (!line.isEmpty()) {
                            if (aspectHelpBuilder != null) {
                                ASPECTS.add(aspectHelpBuilder.createAspectHelp());
                            }
                            aspectHelpBuilder = new AspectHelpBuilder();
                            aspectHelpBuilder.setName(line);
                        } else {
                            throw new IllegalHelpFormatException("Missing string after ':' operand.");
                        }
                    } else if (line.startsWith("INFO:")) {
                        line = line.substring(line.indexOf(':') + 1);
                        line = line.trim().replace("\"", "");
                        if (!line.isEmpty()) {
                            if (aspectHelpBuilder != null) {
                                aspectHelpBuilder.setInfo(line);
                            }
                        } else {
                            throw new IllegalHelpFormatException("Missing string after ':' operand.");
                        }
                    } else if (line.startsWith("ALIASES:")) {
                        line = line.substring(line.indexOf(':') + 1);
                        if (aspectHelpBuilder != null) {
                            if (!line.isEmpty()) {
                                aspectHelpBuilder.setAliases(parseStringArray(line));
                            } else {
                                throw new IllegalHelpFormatException("Missing string after ':' operand.");
                            }
                        }
                    }
                }
            }
            if (aspectHelpBuilder != null) {
                ASPECTS.add(aspectHelpBuilder.createAspectHelp());
            }
        } catch (IOException ignored) {
        }
    }
	
	private static String[] parseStringArray(String line) {
		List<String> innerStrings = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		boolean insideString = false;
		
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == '"') {
				insideString = !insideString;
				
				if (!insideString) {
					innerStrings.add(builder.toString());
					builder.setLength(0);
				}
			} else {
				if (insideString) {
					builder.append(line.charAt(i));
				}
			}
		}
		
		String[] aliasArray = new String[innerStrings.size()];
		innerStrings.toArray(aliasArray);
		return aliasArray;
	}
	
	public static void printHelp(IssuedCommand issuedCommand) {
		if (!initialized) {
			IO.writeString(HelpConstants.NOT_INITIALIZED);
		} else if (issuedCommand.hasArguments()) {
			String argument = issuedCommand.getFirstArgument();
			if (!printCommandHelp(argument) && !printAspectHelp(argument)) {
				IO.writeString(String.format("No help text for '%s' could be found.", argument));
			}
		} else {
			Utils.printMissingArgumentsMessage();
		}
	}
	
	private static boolean printAspectHelp(String command) {
		for (AspectHelp aspectHelp : ASPECTS) {
			if (aspectHelp.equalsIgnoreCase(command)) {
				IO.writeString(aspectHelp.toString());
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean printCommandHelp(String command) {
		for (CommandHelp commandHelp : COMMANDS) {
			if (commandHelp.equalsIgnoreCase(command)) {
				IO.writeString(commandHelp.toString());
				return true;
			}
		}
		
		return false;
	}
	
	public static void printCommandList(IssuedCommand issuedCommand) {
		StringBuilder builder = new StringBuilder();
		if (issuedCommand.hasArguments()) {
			for (CommandHelp commandHelp : COMMANDS) {
				String[] nameAndAliases = commandHelp.getAliases();
				for (String alias : nameAndAliases) {
					if (issuedCommand.getFirstArgument().length() <= alias.length()) {
						if (Utils.startsWithIgnoreCase(alias, issuedCommand.getFirstArgument())) {
							builder.append(commandHelp.toOneLineString(alias)).append('\n');
							break;
						}
					}
				}
			}
			if (builder.length() == 0) {
				builder.append("No command starts with '").append(issuedCommand.getFirstArgument()).append("'.");
			}
		} else {
			for (CommandHelp commandHelp : COMMANDS) {
				builder.append(commandHelp.toOneLineString()).append('\n');
			}
		}
		IO.writeString(builder.toString());
	}
	
	private static class IllegalHelpFormatException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		private IllegalHelpFormatException(String message) {
			super (message);
		}
	}

}
