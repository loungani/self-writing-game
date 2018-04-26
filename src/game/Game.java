package game;

import java.io.File;
import java.io.FileNotFoundException;
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
	private TreeNode<Path> beginning;
	private Logger logger;
	private String gameFile;
	
	public Game() {
		running = true;
		ui = new GraphicalUserInterface();
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
					ui.inform("Creating gamefile: " + gameFile + "\n\n");
					beginning = new TreeNode<Path>(new Path());
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

	private TreeNode<Path> readGame(File file) {
		GameReader gr = new TextFileReader();
		TreeNode<Path> beginning = gr.read(file);
		return beginning;
	}

	private void start(TreeNode<Path> node) {
		Path path = node.getData();
		ui.inform(path.getPathText());
		if (node.children.isEmpty() && !path.isDeathNode() && !path.isWinNode()) { fill(node); }
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
				TreeNode<Path> next = ui.decide(node.children);
				start(next);
			}
		}
	}

	// TODO: Refactoring here for abstraction and DRY.
	private void fill(TreeNode<Path> node) {
		Path path = node.getData();
		Option[] emptyPathOptions = {Option.CREATE_NEW_PATH, Option.MAKE_DEATH_NODE, Option.MAKE_WIN_NODE, Option.EXIT};
		UIResponse response = ui.prompt(emptyPathOptions);
		Option option = response.getOption();
		if (option == Option.CREATE_NEW_PATH) {
			String pathText = response.getArgs().remove(0);
			path.setPathText(pathText);
			for (String choiceText : response.getArgs()) {
				Path newPath = new Path();
				newPath.setChoiceText(choiceText);
				TreeNode<Path> child = node.addChild(newPath);
			}
			ui.inform(path.getPathText());
		} else if (option == Option.MAKE_DEATH_NODE) {
			String pathText = response.getArgs().remove(0);
			path.setPathText(pathText);
			path.makeDeathNode();
			ui.inform(path.getPathText());
		} else if (option == Option.MAKE_WIN_NODE) {
			String pathText = response.getArgs().remove(0);
			path.setPathText(pathText);
			path.makeWinNode();
			ui.inform(path.getPathText());
		} else if (option == Option.EXIT) {
			quit();
		} else {
			throw new IllegalArgumentException("Unlisted option passed to fill.");
		}
	}
	
	private void quit() {
		ui.inform("\nWould you like to save your game before quitting?");
		Option[] saveOptions = {Option.YES, Option.NO};
		UIResponse response = ui.prompt(saveOptions);
		Option option = response.getOption();
		if (option == Option.YES) {
			ui.inform("Saving gamefile: " + gameFile + "\n");
			save(beginning);
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
	
	private void save(TreeNode<Path> beginning) {
		GameWriter fw = new TextFileWriter();
		fw.save(gameFile, beginning);
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		while (game.running) {
			game.mainMenu();
			System.out.println("Exiting program.");
		}
	}
	
}