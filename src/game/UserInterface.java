package game;

import java.util.List;

public interface UserInterface {
	
	// subclasses must implement these methods
	public UIResponse prompt();
	public UIResponse prompt(Option[] options);
	public UIResponse prompt(Option option);
	public void addOption(String text, Option option);
	public void inform(String message);
	public TreeNode<Path> decide(List<TreeNode<Path>> nodes);

}