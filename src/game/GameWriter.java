package game;

public interface GameWriter {
	public void save(String gameFile, TreeNode<Path> p);
	public void log(String event, String logfile);
}
