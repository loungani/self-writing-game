package game;

import java.io.File;

public class Game {
	private Boolean running;
	private UserInterface ui;
	private File gameFile;
	
	public Game() {
		running = true;
		ui = new CommandLineUserInterface();
		ui.addOption("Start new game.", Option.START_GAME);
		ui.addOption("Load saved game.", Option.LOAD_SAVE);
		ui.addOption("Exit program.", Option.EXIT);
		ui.addOption("Create new gamefile.", Option.NEW_GAMEFILE);
		ui.addOption("Return to previous menu.", Option.RETURN);
		ui.addOption("Create new path.", Option.CREATE_NEW_PATH);
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
		Option[] gameFileOptions = {Option.NEW_GAMEFILE, Option.RETURN};
		Boolean needInput = true;
		while (needInput) {
			UIResponse response = ui.prompt(gameFileOptions);
			Option option = response.getOption();
			if (option == Option.NEW_GAMEFILE) {
				String gameFileName = response.getArgs().get(0);
				if (alreadyExists(gameFileName)) {
					ui.inform("Sorry, a gamefile with this name already exists.\n");
				} else {
					needInput = false;
					this.gameFile = create(gameFileName);
					save(gameFile);
					start(new Path());
					System.out.println("Here2");
				}
			} else if (option == Option.RETURN) {
				// TODO: return to previous menu?
				mainMenu();
				needInput = false;
			} else {
				// TODO: Error logging
				throw new IllegalArgumentException("Unlisted option passed to createNewGameFile.");
			}
		}
		System.out.println("Here3");
	}
	
	private void start(Path path) {
		ui.inform(path.read());
		if (path.isEmpty()) {
			Option[] emptyPathOptions = {Option.CREATE_NEW_PATH, Option.EXIT};
			UIResponse response = ui.prompt(emptyPathOptions);
			Option option = response.getOption();
			if (option == Option.CREATE_NEW_PATH) {
				String pathText = response.getArgs().remove(0);
				path.modify(pathText);
				for (String choiceText : response.getArgs()) {
					PathChoice choice = new PathChoice(choiceText);
					path.add(choice);
				}
			} else if (option == Option.EXIT) {
				quit();
			} else {
				throw new IllegalArgumentException("Unlisted option passed to start.");
			}
		}
		System.out.println("Here0");
		// Here's the problem
		Path nextPath = ui.decide(path.getNextPaths());
		start(nextPath);
		System.out.println("Here1");
	}

	private void loadSaveFile() {
		// TODO Auto-generated method stub
		
	}

	private void save(File gameFile) {
		// ui.inform("Saving gamefile: " + gameFile + "\n");
		// TODO Auto-generated method stub
		
	}

	private File create(String gameFile) {
		ui.inform("Creating gamefile: " + gameFile + "\n");
		// TODO Auto-generated method stub
		return null;
	}
	
	private void quit() {
		running = false;
	}

	private boolean alreadyExists(String gameFile) {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String[] args) {
		Game game = new Game();
		while (game.running) {
			game.mainMenu();
			System.out.println("Testing outer loop. Probably shouldn't hit here unless about to exit.");
		}
	}
		

}
