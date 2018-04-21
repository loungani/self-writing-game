package game;

import java.io.File;

public interface GameReader {

	TreeNode<Path> read(File file);

}
