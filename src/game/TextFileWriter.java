package game;

import java.util.ArrayList;
import java.util.List;

public class TextFileWriter implements GameWriter {
	
	private Logger logger;

	@Override
	public void save(String gameFile, TreeNode<Path> node) {
		ArrayList<TreeNode<Path>> arr = node.toArray();
		for (int i = 0; i < arr.size(); i++) {
			StringBuilder sb = new StringBuilder();
			sb.append(i + ";");
			Path current = arr.get(i).getData();
			sb.append(current.getPathText() + ";");
			if (current.isDeathNode()) { sb.append("lose-"); }
			if (current.isWinNode()) { sb.append("win+"); }
			List<TreeNode<Path>> children = arr.get(i).children;
			for (TreeNode<Path> child : children) {
				sb.append(child.getData().getChoiceText() + ";");
				sb.append(arr.indexOf(child) + ";");
			}
			sb.deleteCharAt(sb.length()-1); // remove last semicolon
			log(sb.toString(), gameFile);
		}
	}

	@Override
	public void log(String event, String logfile) {
		logger = Logger.getInstance(logfile);
		logger.log(event);
	}

}
