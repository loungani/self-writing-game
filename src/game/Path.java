package game;

public class Path {
	private String choiceText;
	private String pathText;
	private Boolean deathNode;
	private Boolean winNode;
	
	public Path() {
		choiceText = "No choice text set";
		pathText = "Looks like you have nothing here!";
		deathNode = false;
		winNode = false;
	}
	
	public void makeDeathNode() {
		deathNode = true;
	}
	
	public Boolean isDeathNode() {
		return deathNode;
	}
	
	public void makeWinNode() {
		winNode = true;
	}
	
	public Boolean isWinNode() {
		return winNode;
	}
	
	public String gameOverMessage() {
		if (deathNode && winNode) { throw new IllegalStateException("Path is both win and lose node."); }
		if (deathNode) { return "\nSorry! You lose."; }
		if (winNode) { return "\nCongratulations! You win."; }
		throw new IllegalArgumentException("gameOverMessage called without win or lose node.");
	}
	
	public String getPathText() {
		return pathText;
	}
	
	public String getChoiceText() {
		return choiceText;
	}

	public void setPathText(String pathText) {
		this.pathText = pathText;
	}
	
	public void setChoiceText(String choiceText) {
		this.choiceText = choiceText;
	}

}
