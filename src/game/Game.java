package game;

public class Game {
	public static void main(String[] args) {
		
		Game game = new Game();
		UserInterface ui = new CommandLineUserInterface();
		
		ui.addOption("Start new game.", Option.START_GAME);
		ui.addOption("Load saved game.", Option.LOAD_SAVE);
	}

}
