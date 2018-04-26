package game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GraphicalUserInterface implements UserInterface {
	private JFrame frame;
	private JPanel status_panel;
	private JLabel status;
	private JTextField game_text;
	Map<Option, String> promptions; // a map from each option to its respective prompt
	
	public GraphicalUserInterface() {
		// promptions
		promptions = new TreeMap<Option, String>();
		
		// Game frame
		frame = new JFrame("Choose Your Own Adventure -- Creator");
		Dimension d = frame.getPreferredSize();
		d.setSize(600, 600);
		frame.setPreferredSize(d);
		frame.setLocation(300, 100);
		
		// Status panel
		status_panel = new JPanel();
		frame.add(status_panel, BorderLayout.SOUTH);
		status = new JLabel("Running...");
		status_panel.add(status);
		
		// Game updates
		game_text = new JTextField("                   ");
		game_text.setEnabled(false);
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
		int optionNum = getOptionNum(prompts, options.length);
		Option option = options[optionNum];
		return prompt(option);
	}

	private int getOptionNum(List<String> prompts, int totalOptions) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < prompts.size(); i++) {
			sb.append("Option " + (i+1) + ") " + prompts.get(i) + "\n");
		}
		inform(sb.toString());
		int optionNum = getInputInt("Choose option (enter number):");
		while (optionNum < 1 || optionNum > totalOptions) {
			optionNum = getInputInt("Invalid option. Please choose between 1 to " + totalOptions);
		}
		optionNum--; // Computers like to start arrays at 0, humans like to start lists at 1
		return optionNum;
	}

	@Override
	public UIResponse prompt(Option option) {
		List<String> args = new ArrayList<String>();
		UIResponse response;
		
		if (option == Option.START_GAME) {
			inform("Starting new game.\n");
		} else if (option == Option.LOAD_SAVE) {
			inform("Loading saved game.");
		} else if (option == Option.EXIT) {
			inform("Exit game.");
		} else if (option == Option.NEW_GAMEFILE || option == Option.LOAD_GAMEFILE) {
			args.add(getInputString("Enter name for gamefile."));
		} else if (option == Option.MAIN_MENU) {
			inform("Returning to main menu.");
		} else if (option == Option.RESTART) {
			inform("Restarting the game!\n");
		} else if (option == Option.CREATE_NEW_PATH) {
			String currentPathText = getInputString("Enter text that player will see:");
			args.add(currentPathText);
			inform("Enter at least one possible choice for players:\n");
			Boolean needInput = true;
			while (needInput) {
				String newPathText = getInputString("New choice text:");
				args.add(newPathText);
				inform("Finished adding choice?\n");
				Boolean finishedAddingChoices = getYesOrNo();
				if (finishedAddingChoices) {
					needInput = false;
				}
			}
		} else if (option == Option.MAKE_DEATH_NODE) {
			String deathPathText = getInputString("Enter the text that players will see upon losing the game.");
			args.add(deathPathText);
		} else if (option == Option.MAKE_WIN_NODE) {
			String deathPathText = getInputString("Enter the text that players will see upon winning the game.");
			args.add(deathPathText);
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

	private String getInputString(String message) {
		return JOptionPane.showInputDialog(message);
	}
	
	private int getInputInt(String message) {
		Boolean needInput = true;
		while (needInput) {
			try {
				String integer = JOptionPane.showInputDialog(message);
				Integer optionNum = Integer.parseInt(integer);
				return optionNum;
			} catch (NumberFormatException e) {
				inform("Please enter a valid integer.");
				// TODO: Some kind of logging?
			}
		}
		throw new IllegalStateException("Should not reach this statement in getInputInt");
	}

	private Boolean getYesOrNo() {
		if (!promptions.containsKey(Option.YES)) {
			promptions.put(Option.YES, "Yes.");
		}
		if (!promptions.containsKey(Option.NO)) {
			promptions.put(Option.NO, "No.");
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

	/*
	 * (non-Javadoc)
	 * TODO: Currently has exact same functionality as prompt.
	 * 
	 * @see game.UserInterface#decide(java.util.Map)
	 */
	@Override
	public TreeNode<Path> decide(List<TreeNode<Path>> nodes) {
		List<String> prompts = new ArrayList<String>();
		List<TreeNode<Path>> choices = new ArrayList<TreeNode<Path>>();
		
		for (TreeNode<Path> node : nodes) {
			prompts.add(node.getData().getChoiceText());
			choices.add(node);
		}
		
		int optionNum = getOptionNum(prompts, choices.size());
		TreeNode<Path> selected = choices.get(optionNum);
		return selected;
	}

	@Override
	public void inform(String message) {
		JOptionPane.showMessageDialog(frame, message);
	}
}
