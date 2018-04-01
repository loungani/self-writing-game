package game;

import java.util.List;

public class Game {
	public static void main(String[] args) {
		
		Game game = new Game();
		UserInterface ui = new CommandLineUserInterface();
		
		ui.addOption("Start new game.", Option.START_GAME);
		ui.addOption("Load saved game.", Option.LOAD_SAVE);
		ui.addOption("Exit program.", Option.EXIT);
		
		Option[] mainMenuOptions = {Option.START_GAME, Option.LOAD_SAVE, Option.EXIT};
		UIResponse response = ui.prompt(mainMenuOptions);
		Option option = response.getOption();
		
		if (option == Option.START_GAME) {
			/*UIResponse =*/
		}
		
	}

}
