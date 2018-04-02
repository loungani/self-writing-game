package game;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class CommandLineUserInterface implements UserInterface {
	private PrintStream out;
	private Scanner in;
	Map<Option, String> promptions; // a map from each option to its respective prompt
	
	public CommandLineUserInterface() {
		out = System.out;
		in = new Scanner(System.in);
		promptions = new TreeMap<Option, String>();
	}
	
	@Override
	public void addOption(String text, Option option) {
		promptions.put(option, text);
	}
	
	/**
	 * Prompts for all options added to the UI.
	 * @return a UIResponse with selected option and associated arguments
	 */
	@Override
	public UIResponse prompt() {
		return prompt((Option[]) promptions.keySet().toArray());
	}
	
	/**
	 * Prompts for a subset of options.
	 * @param a subset of options as an array
	 * @return a UIResponse with selected option and associated arguments
	 */
	@Override
	public UIResponse prompt(Option[] options) {
		List<String> prompts = new ArrayList<String>();
		for (Option option : options) { prompts.add(promptions.get(option)); }
		
		for (int i = 0; i < prompts.size(); i++) {
			inform("Option " + (i+1) + ") " + prompts.get(i) + "\n");
		}
		inform("Choose option (enter number): \n");
		int optionNum = getInputInt();
		while (optionNum < 1 || optionNum > options.length) {
			inform("Invalid option. Please choose between 1 to " + options.length);
			optionNum = getInputInt();
		}
		optionNum--; // Computers like to start arrays at 0, humans like to start lists at 1
		Option option = options[optionNum];
		return prompt(option);
	}

	@Override
	public UIResponse prompt(Option option) {
		List<String> args = new ArrayList<String>();
		UIResponse response;
		
		// TODO: package args and such
		if (option == Option.START_GAME) {
			inform("Starting new game.\n");
		} else if (option == Option.LOAD_SAVE) {
			inform("Loading saved game.");
		} else if (option == Option.EXIT) {
			inform("Exit game.");
		} else if (option == Option.NEW_GAMEFILE) {
			inform("Enter name for new gamefile.");
			args.add(getInputString());
		} else if (option == Option.RETURN) {
			inform("Returning to previous menu.");
		} else if (option == Option.CREATE_NEW_PATH) {
			inform("Enter text that player will see:");
			String currentPathText = getInputString();
			args.add(currentPathText);
			inform("Enter at least one possible choice for players:");
			Boolean needInput = true;
			while (needInput) {
				inform("New choice text:");
				String newPathText = getInputString();
				args.add(newPathText);
				inform("Finished adding choice?");
				Boolean finishedAddingChoices = getYesOrNo();
				if (finishedAddingChoices) {
					needInput = false;
				}
			}
		} else if (option == Option.YES) {
			// Do nothing
		} else if (option == Option.NO) {
			// Do nothing
		} else {
			throw new IllegalArgumentException("Option didn't match with any possible options. Should be unreachable.");
		}
		response = new UIResponse(option, args);
		return response;
	}

	private Boolean getYesOrNo() {
		if (!promptions.containsKey(Option.YES)) {
			promptions.put(Option.YES, "Yes.");
		}
		if (!promptions.containsKey(Option.NO)) {
			promptions.put(Option.NO, "No");
		}
		Option[] yesOrNoOptions = {Option.YES, Option.NO};
		UIResponse response = prompt(yesOrNoOptions);
		Option option = response.getOption();
		if (option == Option.YES) {
			return true;
		} else if (option == Option.NO) {
			return false;
		} else {
			throw new IllegalArgumentException("Y/N didn't match with any possible options. Should be unreachable.");
		}
	}

	@Override
	public void inform(String message) {
		out.print(message + "\n");
	}
	
	
	/*
	 * (non-Javadoc)
	 * TODO: Currently has exact same functionality as prompt.
	 * 
	 * @see game.UserInterface#decide(java.util.Map)
	 */
	@Override
	public Path decide(Map<PathChoice, Path> nextPaths) {
		List<String> prompts = new ArrayList<String>();
		List<PathChoice> choices = new ArrayList<PathChoice>();
		Set<PathChoice> setOfChoices = nextPaths.keySet();
		for (PathChoice choice : setOfChoices) { 
			prompts.add(choice.getText()); 
			choices.add(choice);
		}
		for (int i = 0; i < prompts.size(); i++) {
			inform("Option " + (i+1) + ") " + prompts.get(i) + "\n");
		}
		inform("Choose option (enter number): \n");
		int optionNum = getInputInt();
		while (optionNum < 1 || optionNum > choices.size()) {
			inform("Invalid option. Please choose between 1 to " + choices.size());
			optionNum = getInputInt();
		}
		optionNum--; // Computers like to start arrays at 0, humans like to start lists at 1
		PathChoice option = choices.get(optionNum);
		Path selected = nextPaths.get(option);
		return selected;
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
	
	private String getInputString() {
		// might need to throw one away / clear buffer
		String s = in.nextLine(); 
		if (s.equals("")) {
			return in.nextLine();
		} else {
			return s;
		}
	}


}
