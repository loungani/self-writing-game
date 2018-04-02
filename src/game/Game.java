package game;

import java.util.List;

public class Game {
	private Boolean running;
	private UserInterface ui;
	
	public Game() {
		running = true;
		ui = new CommandLineUserInterface();
		ui.addOption("Start new game.", Option.START_GAME);
		ui.addOption("Load saved game.", Option.LOAD_SAVE);
		ui.addOption("Exit program.", Option.EXIT);
		ui.addOption("Create new gamefile:", Option.NEW_GAMEFILE);
		ui.addOption("Return to previous menu.", Option.RETURN);
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
				String gameFile = response.getArgs().get(0);
				if (alreadyExists(gameFile)) {
					ui.inform("Sorry, a gamefile with this name already exists.");
				} else {
					create(gameFile);
					save(gameFile);
					needInput = false;
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
	}
	
	private void loadSaveFile() {
		// TODO Auto-generated method stub
		
	}

	private void save(String gameFile) {
		// TODO Auto-generated method stub
		
	}

	private void create(String gameFile) {
		// TODO Auto-generated method stub
		
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
			System.out.println("Testing outer loop. Probably shouldn't hit here.");
		}
	}
		

}
