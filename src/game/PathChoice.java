package game;

public class PathChoice implements Comparable<PathChoice> {
	private String choiceText;
	
	public PathChoice(String choiceText) {
		this.choiceText = choiceText;
	}
	
	public String getText() {
		return choiceText;
	}

	@Override
	public int compareTo(PathChoice p) {
		return choiceText.compareTo(p.getText());
	}

}
