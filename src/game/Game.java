package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
	private Boolean running;
	private UserInterface ui;
	private Path beginning;
	private Logger logger;
	private String gameFile;
	
	public Game() {
		running = true;
		ui = new CommandLineUserInterface();
		ui.addOption("Start new game.", Option.START_GAME);
		ui.addOption("Load saved game.", Option.LOAD_SAVE);
		ui.addOption("Exit program.", Option.EXIT);
		ui.addOption("Create new gamefile.", Option.NEW_GAMEFILE);
		ui.addOption("Load gamefile.", Option.LOAD_GAMEFILE);
		ui.addOption("Return to main menu.", Option.MAIN_MENU);
		ui.addOption("Create new path.", Option.CREATE_NEW_PATH);
		ui.addOption("Make this path a 'you lose' node for the player.", Option.MAKE_DEATH_NODE);
		ui.addOption("Make this path a 'you win!' node for the player.", Option.MAKE_WIN_NODE);
		ui.addOption("Restart this game from the beginning.", Option.RESTART);
		ui.addOption("Yes.", Option.YES);
		ui.addOption("No.", Option.NO);
	}
	
	private void mainMenu() {
		Option[] mainMenuOptions = {Option.START_GAME, Option.LOAD_SAVE, Option.EXIT};
		UIResponse response = ui.prompt(mainMenuOptions);
		Option option = response.getOption();	
		if (option == Option.START_GAME) {
			createNewGameFile();
		} else if (option == Option.LOAD_SAVE) {
			loadSaveFile();
		} else if (option == Option.EXIT) {
			quit();
		}
	}

	private void createNewGameFile() {
		Option[] gameFileOptions = {Option.NEW_GAMEFILE, Option.MAIN_MENU};
		Boolean needInput = true;
		while (needInput) {
			UIResponse response = ui.prompt(gameFileOptions);
			Option option = response.getOption();
			if (option == Option.NEW_GAMEFILE) {
				String gameFile = response.getArgs().get(0);
				if (alreadyExists(gameFile)) {
					ui.inform("Sorry, a gamefile with this name already exists.\n");
				} else {
					needInput = false;
					this.gameFile = gameFile;
					create(gameFile);
					beginning = new Path();
					start(beginning);
				}
			} else if (option == Option.MAIN_MENU) {
				// TODO: return to previous menu?
				mainMenu();
				needInput = false;
			} else {
				// TODO: Error logging
				throw new IllegalArgumentException("Unlisted option passed to createNewGameFile.");
			}
		}
	}
	
	// TODO: Abstract as FileReader or factory. Needs more modularity.
	private void loadSaveFile() {
		Option[] loadOptions = {Option.LOAD_GAMEFILE, Option.MAIN_MENU};
		Boolean needInput = true;
		while (needInput) {
			UIResponse response = ui.prompt(loadOptions);
			Option option = response.getOption();
			if (option == Option.LOAD_GAMEFILE) {
				String loadFile = response.getArgs().get(0);
				if (!alreadyExists(loadFile)) {
					ui.inform("No such file with tha name exists.\n");
				} else {
					String current_dir = null;
					try {
						current_dir = new java.io.File(".").getCanonicalPath();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					File file = new File(current_dir + "\\src\\" + loadFile);
					needInput = false;
					this.gameFile = loadFile;
					beginning = readGame(file);
					start(beginning);
				}
			} else if (option == Option.MAIN_MENU) {
				mainMenu();
				needInput = false;
			} else {
					// TODO: Error logging
					throw new IllegalArgumentException("Unlisted option passed to loadGameFile.");
			}
		}
	}
	
	// TODO: Same as loadFile, abstract this into a FileReader class.
	private Path readGame(File file) {
		List<String> lines = new ArrayList<String>();
		Scanner in = null;
		try {
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (in.hasNextLine()) {
			String s = in.nextLine();
			System.out.println(s);
			lines.add(s);
			// TODO: Parse
		}
		
		// Lines has all the lines, but nothing in order
		Path[] loadPaths = new Path[6];
		for (String line : lines) {
			String[] subStrings = line.split(",");
			String[] first = subStrings[0].split(" ");
			int index = Integer.parseInt(first[first.length-1]);
			String pathText = subStrings[1];
			Path p = new Path();
			p.modify(pathText);
			loadPaths[index] = p;
		}
		
		// So now have an array of all the paths, but no linkages between them
		// Want to go over each line, figure out the path choices for each path
		// And attach them to the proper path which should now exist
		// hopefully
		
		for (String line: lines) {
			String[] subStrings =  line.split(",");
			String[] first = subStrings[0].split(" ");
			int index = Integer.parseInt(first[first.length-1]);
			Path current = loadPaths[index];
			
			System.out.println("Working with path with index: " + index + " and text: " + current.read());
			if (subStrings.length > 3) { //choices
				for (int i=2; i < subStrings.length-1; i+=2) {
					PathChoice choice = new PathChoice(subStrings[i]);
					current.add(choice);
					Path next = current.getNextPaths().get(choice);
					System.out.println("Trying to attach choice with text: " + choice.getText() + " to path with text " + loadPaths[Integer.parseInt(subStrings[i+1])].read());;
					next = loadPaths[Integer.parseInt(subStrings[i+1])];
					current.getNextPaths().remove(choice);
					current.getNextPaths().put(choice, next);
					System.out.println("After setting next, next has text : " + next.read());
				}
			} else if (subStrings.length == 3) { // win or lose node
				if (subStrings[2].equals("win")) {
					current.makeWinNode();
				} else if (subStrings[2].equals("lose")) {
					current.makeDeathNode();
				} else {
					throw new IllegalStateException("Should not hit this condition.");
				}
			} else {
				// Throw away, should just be empty path
			}
		}
		ui.inform("Game successfully loaded!\n");
		return loadPaths[0]; //this should be beginning, and should contain pointers to the other links
	}

	private void start(Path path) {
		ui.inform(path.read());
		if (path.isEmpty()) { fill(path); }
		if (path.isDeathNode() || path.isWinNode()) {
			ui.inform(path.gameOverMessage());
			Option[] gameOverOptions = {Option.MAIN_MENU, Option.RESTART, Option.EXIT};
			UIResponse response = ui.prompt(gameOverOptions);
			Option option = response.getOption();
			if (option == Option.MAIN_MENU) {
				mainMenu();
			} else if (option == Option.RESTART) {
				start(beginning);
			} else if (option == Option.EXIT) {
				quit();
			} else {
				throw new IllegalArgumentException("Unlisted option passed to start.");
			}
		} else {
			if (running) {
				Path nextPath = ui.decide(path.getNextPaths());
				start(nextPath);
			}
		}
	}

	// TODO: Refactoring here for abstraction and DRY.
	private void fill(Path path) {
		Option[] emptyPathOptions = {Option.CREATE_NEW_PATH, Option.MAKE_DEATH_NODE, Option.MAKE_WIN_NODE, Option.EXIT};
		UIResponse response = ui.prompt(emptyPathOptions);
		Option option = response.getOption();
		if (option == Option.CREATE_NEW_PATH) {
			String pathText = response.getArgs().remove(0);
			path.modify(pathText);
			for (String choiceText : response.getArgs()) {
				PathChoice choice = new PathChoice(choiceText);
				path.add(choice);
			}
			ui.inform(path.read());
		} else if (option == Option.MAKE_DEATH_NODE) {
			String pathText = response.getArgs().remove(0);
			path.modify(pathText);
			path.makeDeathNode();
			ui.inform(path.read());
		} else if (option == Option.MAKE_WIN_NODE) {
			String pathText = response.getArgs().remove(0);
			path.modify(pathText);
			path.makeWinNode();
			ui.inform(path.read());
		} else if (option == Option.EXIT) {
			quit();
		} else {
			throw new IllegalArgumentException("Unlisted option passed to fill.");
		}
	}

	// TODO: Should use StringBuilder, current implementation is an anti-pattern.
	// TODO: Set a new delimiter, such a semicolon. Make sure to scrub inputs for semicolons.
	private void save(Path current, ArrayList<String> saveList, int outsideCounter, int numChoices, int numChoicesPrev) {
		String line = (outsideCounter+numChoicesPrev-1) + "," + current.read();
		if (current.isDeathNode()) { line += ",lose"; }
		if (current.isWinNode()) { line += ",win"; }
		int choiceCounter = 1;
		numChoicesPrev = numChoices;
		for (PathChoice choice : current.getNextPaths().keySet()) {
			numChoices = current.getNextPaths().keySet().size();
			line += "," + choice.getText() + "," + (outsideCounter+choiceCounter+numChoicesPrev-1);
			Path nextPath = current.getNextPaths().get(choice);
			save(nextPath, saveList, outsideCounter+choiceCounter, numChoices, numChoicesPrev);
			choiceCounter++;
		}
		saveList.add(line);
		// Should execute only once, at the end of the recursive stack
		if (current == beginning) {
			for (String s : saveList) {
				log(s, gameFile);
			}
		}
	}

	private void create(String gameFile) {
		ui.inform("Creating gamefile: " + gameFile + "\n\n");
	}
	
	private void quit() {
		ui.inform("\nWould you like to save your game before quitting?");
		Option[] saveOptions = {Option.YES, Option.NO};
		UIResponse response = ui.prompt(saveOptions);
		Option option = response.getOption();
		if (option == Option.YES) {
			ui.inform("Saving gamefile: " + gameFile + "\n");
			save(beginning, new ArrayList<String>(), 0, 1, 1);
		}
		running = false;
	}

	private boolean alreadyExists(String gameFile) {
		String current_dir;
		try {
			current_dir = new java.io.File(".").getCanonicalPath();
			java.nio.file.Path filePath = Paths.get(current_dir + "\\src\\" + gameFile);
			return Files.exists(filePath, LinkOption.NOFOLLOW_LINKS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalStateException("Should not reach here.");
		}
	}
	
	private void log(String event, String logfile) {
		logger = Logger.getInstance(logfile);
		logger.log(event);
	}

	public static void main(String[] args) {
		Game game = new Game();
		while (game.running) {
			game.mainMenu();
			System.out.println("Testing outer loop. Probably shouldn't hit here unless about to exit.");
		}
	}
	
}