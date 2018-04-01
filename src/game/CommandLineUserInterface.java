package game;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class CommandLineUserInterface implements UserInterface {
	private PrintStream out;
	private Scanner in;
	Map<Option, String> promptions; // prompts mapped from options
	
	public CommandLineUserInterface() {
		out = System.out;
		in = new Scanner(System.in);
		promptions = new TreeMap<Option, String>();
	}

	@Override
	public UIResponse prompt() {
		return prompt((Option[]) promptions.keySet().toArray());
	}
	
	@Override
	public UIResponse prompt(Option[] options) {
		List<String> prompts = new ArrayList<String>();
		for (Option option : options) { prompts.add(promptions.get(option)); }
		
		for (int i = 0; i < prompts.size(); i++) {
			inform("Option " + (i+1) + ") " + prompts.get(i) + "\n");
		}
		inform("Choose option (enter number): ");
		int optionNum = getInputInt();
		while (optionNum < 1 || optionNum > options.length) {
			inform("Invalid option. Please choose between 1 to " + options.length);
			optionNum = getInputInt();
		}
		optionNum--; // Computers like to start arrays at 0, humans like to start lists at 1
		Option option = options[optionNum];
		return prompt(option);
	}

	private int getInputInt() {
		Boolean needInput = true;
		while (needInput) {
			try {
				int next = 0;
				if (!in.hasNextInt()) {
					inform("You entered: " + in.next() + ". Stop trying to break the program "
							+ "and enter a valid integer.\n");
				}
				next = in.nextInt();
				return next;
			} catch (InputMismatchException e) { /* TODO: Some kind of logging. This hits whenever spaces included. */ }
		}
		throw new NullPointerException("Escaped the input loop in getInputInt()");
	}

	@Override
	public UIResponse prompt(Option option) {
		List<String> args = new ArrayList<String>();
		UIResponse response;
		
		if (option == Option.START_GAME) {
			inform("Starting new game.\n");
		} else if (option == Option.LOAD_SAVE) {
			inform("Loading saved game.\n");
		} else if (option == Option.EXIT) {
			inform("Exit game.");
		} else {
			throw new IllegalArgumentException("Option didn't match with any possible options. Should be unreachable.");
		}
		// TODO: args and such
		response = new UIResponse(option, args);
		return response;
	}

	@Override
	public void addOption(String text, Option option) {
		promptions.put(option, text);
	}

	@Override
	public void inform(String message) {
		out.print(message);
	}

}
