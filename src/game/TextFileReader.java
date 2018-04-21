package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TextFileReader implements GameReader {

	@Override
	public TreeNode<Path> read(File file) {
		List<String> lines = new ArrayList<String>();
		Scanner in = null;
		try {
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (in.hasNextLine()) {
			String s = in.nextLine();
			lines.add(s);
			// TODO: Parse
		}
		
		// Iterate over once to create an array of nodes
		// The nodes have paths with text, but no children or choice text yet
		ArrayList<TreeNode<Path>> arr = new ArrayList<TreeNode<Path>>();
		for (String line : lines) {
			String[] subStrings = line.split(";");
			Path p;
			TreeNode<Path> node;
			if (subStrings.length >= 2) {
				p = new Path();
				p.setPathText(subStrings[1]);
			} else {
				throw new IllegalStateException("subStrings.length should never be: " + subStrings.length);
			}
			if (subStrings.length == 3) {
				if (subStrings[2].equals("lose-")) {
					p.makeDeathNode();
				} else if (subStrings[2].equals("win+")) {
					p.makeWinNode();
				} else {
					throw new IllegalStateException("Not win or lose node argument, instead is: " + subStrings[2]);
				}
			}
			node = new TreeNode<Path>(p);
			arr.add(node);
		}
		
		// Iterate over the second time to connect the nodes
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			String[] subStrings = line.split(";");
			TreeNode<Path> current = arr.get(i);
			if (subStrings.length > 3) {
				for (int j=2; j < subStrings.length-1; j+=2) {
					int childIndex = Integer.parseInt(subStrings[j+1]);
					TreeNode<Path> child = arr.get(childIndex);
					child.getData().setChoiceText(subStrings[j]);
					current.addChildNode(child);
				}
			}
		}
		
		return arr.get(0); // head of the tree
	}
	
	

}
