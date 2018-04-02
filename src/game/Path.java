package game;

import java.util.Map;
import java.util.TreeMap;

public class Path {
	private Map<PathChoice, Path> paths;
	private String pathText;
	
	public Path() {
		paths = new TreeMap<PathChoice, Path>();
		pathText = "Looks like you have nothing here!";
	}
	
	public Boolean isEmpty() {
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
