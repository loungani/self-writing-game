package game;

import java.util.Map;
import java.util.TreeMap;

public class Path {
	private Map<PathChoice, Path> paths;
	private String pathText;
	private Boolean deathNode;
	private Boolean winNode;
	
	public Path() {
		paths = new TreeMap<PathChoice, Path>();
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
	
	public Boolean isEmpty() {
		if (deathNode || winNode) {
			return false;
		}
		return paths.isEmpty();
	}
	
	public String read() {
		return pathText;
	}

	public void modify(String newPathText) {
		pathText = newPathText;
	}

	public void add(PathChoice choice) {
		paths.put(choice, new Path());
	}
	
	public Map<PathChoice, Path> getNextPaths() {
		return paths;
	}

}
