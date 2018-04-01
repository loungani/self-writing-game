package game;
public interface UserInterface {
	
	// subclasses must implement these methods
	public UIResponse prompt();
	public UIResponse prompt(Option option);
	public void addOption(String text, Option option);
	public void inform(String message);

	
}