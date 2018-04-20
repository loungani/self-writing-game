package game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

class Test {

	@org.junit.jupiter.api.Test
	void BasicIteratorTest() {
		TreeNode<Path> node = new TreeNode<Path>(new Path());
		TreeNode<Path> child1 = node.addChild(new Path());
		TreeNode<Path> child2 = node.addChild(new Path());
		
		Iterator<TreeNode<Path>> iter = node.iterator();
		assertTrue(iter.hasNext());
		assertEquals(node, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(child1, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(child2, iter.next());
		assertTrue(!iter.hasNext());
	}

}


