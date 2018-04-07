package game;

import java.util.ArrayList;

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

	private void loadSaveFile() {
		// TODO Auto-generated method stub
		
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
		log(gameFile + " opened.", gameFile);
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
		// TODO Auto-generated method stub
		return false;
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